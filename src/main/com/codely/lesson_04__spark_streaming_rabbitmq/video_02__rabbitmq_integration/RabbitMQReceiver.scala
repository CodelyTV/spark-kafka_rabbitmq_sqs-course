package com.codely.lesson_04__spark_streaming_rabbitmq.video_02__rabbitmq_integration

import com.rabbitmq.client._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

class RabbitMQReceiver(queueName: String, host: String, port: Int, username: String, password: String)
    extends Receiver[String](StorageLevel.MEMORY_AND_DISK_2) {

  @transient var connection: Connection = _
  @transient var channel: Channel       = _

  def onStart(): Unit = {

    val factory = new ConnectionFactory()
    factory.setUsername(username)
    factory.setPassword(password)
    factory.setHost(host)
    factory.setPort(port)

    connection = factory.newConnection()
    channel = connection.createChannel()

    new Thread("SQS Receiver") {

      override def run(): Unit = {
        receive()
      }
    }.start()
  }

  private def receive(): Unit = {
    try {
      val consumer = new DefaultConsumer(channel) {
        override def handleDelivery(
            consumerTag: String,
            envelope: Envelope,
            properties: AMQP.BasicProperties,
            body: Array[Byte]
        ): Unit = {
          val message = new String(body, "UTF-8")
          store(message)
        }
      }
      channel.basicConsume(queueName, true, consumer)
    } catch {
      case e: Exception =>
        restart("Could not connect to RabbitMQ", e)
    }
  }

  def onStop(): Unit = {
    if (channel != null) {
      channel.close()
    }
    if (connection != null) {
      connection.close()
    }
  }
}
