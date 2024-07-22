package com.codely.lesson_04__spark_streaming_rabbitmq.video_03__rabbitmq_custom_provider

import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.connector.read.{InputPartition, PartitionReader, PartitionReaderFactory}
import org.apache.spark.sql.types.StructType

case class RabbitMQPartitionReaderFactory(schema: StructType) extends PartitionReaderFactory {
  override def createReader(partition: InputPartition): PartitionReader[InternalRow] =

    RabbitMQPartitionReader(schema, partition.asInstanceOf[RabbitMQInputPartition])
}
