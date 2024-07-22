package com.codely.lesson_04__spark_streaming_rabbitmq.video_03__rabbitmq_custom_provider

import org.apache.spark.sql.connector.read.{Scan, ScanBuilder}
import org.apache.spark.sql.types.StructType
import java.util

case class RabbitMQScanBuilder(schema: StructType, properties: util.Map[String, String]) extends ScanBuilder {
  override def build(): Scan = new RabbitMQScan(schema, properties)
}
