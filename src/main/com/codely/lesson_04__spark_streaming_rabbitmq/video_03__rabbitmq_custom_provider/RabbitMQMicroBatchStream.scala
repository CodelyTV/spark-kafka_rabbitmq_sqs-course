package com.codely.lesson_04__spark_streaming_rabbitmq.video_03__rabbitmq_custom_provider

import org.apache.spark.sql.connector.read.{InputPartition, PartitionReaderFactory}
import org.apache.spark.sql.connector.read.streaming.{MicroBatchStream, Offset}
import org.apache.spark.sql.types.StructType

import java.util

case class RabbitMQMicroBatchStream(
    schema: StructType,
    properties: util.Map[String, String],
    checkpointLocation: String
) extends MicroBatchStream {

  // Simulated offset
  private var currentOffset: Long = 0

  override def latestOffset(): Offset = {
    currentOffset += 1
    RabbitMQOffset(currentOffset)
  }

  override def planInputPartitions(start: Offset, end: Offset): Array[InputPartition] = {
    Array(
      RabbitMQInputPartition(properties)
    )
  }

  override def createReaderFactory(): PartitionReaderFactory = {
    RabbitMQPartitionReaderFactory(schema)
  }

  override def initialOffset(): Offset = new RabbitMQOffset(0)

  override def commit(end: Offset): Unit = {}

  override def deserializeOffset(json: String): Offset = {
    RabbitMQOffset.fromJson(json)
  }

  override def stop(): Unit = {}
}
