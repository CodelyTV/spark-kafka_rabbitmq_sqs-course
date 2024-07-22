package com.codely.lesson_04__spark_streaming_rabbitmq.video_03__rabbitmq_custom_provider

import com.rabbitmq.client.ConnectionFactory
import org.apache.spark.sql.connector.read.{InputPartition, PartitionReader}
import com.rabbitmq.client._
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.types.StructType
import org.apache.spark.unsafe.types.UTF8String

import scala.collection.mutable

case class RabbitMQPartitionReader(schema: StructType, partition: InputPartition) extends PartitionReader[InternalRow] {
  private val QUEUE_NAME        = partition.asInstanceOf[RabbitMQInputPartition].properties.get("queue.name")
  private val RABBITMQ_HOST     = partition.asInstanceOf[RabbitMQInputPartition].properties.get("rabbitmq.host")
  private val RABBITMQ_PORT     = partition.asInstanceOf[RabbitMQInputPartition].properties.get("rabbitmq.port").toInt
  private val RABBITMQ_USERNAME = partition.asInstanceOf[RabbitMQInputPartition].properties.get("rabbitmq.username")
  private val RABBITMQ_PASSWORD = partition.asInstanceOf[RabbitMQInputPartition].properties.get("rabbitmq.password")

  private val factory = new ConnectionFactory()
  factory.setHost(RABBITMQ_HOST)
  factory.setPort(RABBITMQ_PORT)
  factory.setUsername(RABBITMQ_USERNAME)
  factory.setPassword(RABBITMQ_PASSWORD)

  private val connection: Connection = factory.newConnection()
  private val channel: Channel       = connection.createChannel()

  private val messages: mutable.Queue[String] = mutable.Queue.empty[String]
  private val consumer: DefaultConsumer = new DefaultConsumer(channel) {
    override def handleDelivery(
        consumerTag: String,
        envelope: Envelope,
        properties: AMQP.BasicProperties,
        body: Array[Byte]
    ): Unit = {
      val message = new String(body, "UTF-8")
      messages.enqueue(message)
    }
  }

  channel.basicConsume(QUEUE_NAME, true, consumer)

  override def next(): Boolean = {
    messages.nonEmpty
  }

  override def get(): InternalRow = {
    val message = messages.dequeue()
    InternalRow(UTF8String.fromString(message))
  }

  override def close(): Unit = {
    channel.close()
    connection.close()
  }
}
