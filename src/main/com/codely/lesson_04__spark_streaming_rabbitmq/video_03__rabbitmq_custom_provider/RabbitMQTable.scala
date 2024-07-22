package com.codely.lesson_04__spark_streaming_rabbitmq.video_03__rabbitmq_custom_provider

import org.apache.spark.sql.connector.catalog.{SupportsRead, Table, TableCapability}
import org.apache.spark.sql.connector.read.ScanBuilder
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.util.CaseInsensitiveStringMap

import java.util

case class RabbitMQTable(schema: StructType, tableProperties: util.Map[String, String])
    extends Table
    with SupportsRead {

  override def name(): String = "rabbitmq_table"

  override def capabilities(): util.Set[TableCapability] = util.EnumSet.of(TableCapability.MICRO_BATCH_READ)

  override def newScanBuilder(options: CaseInsensitiveStringMap): ScanBuilder =
    RabbitMQScanBuilder(schema, tableProperties)
}
