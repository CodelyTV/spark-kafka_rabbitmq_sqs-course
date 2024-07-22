package com.codely.lesson_04__spark_streaming_rabbitmq.video_03__rabbitmq_custom_provider

import org.apache.spark.sql.connector.catalog.{Table, TableProvider}
import org.apache.spark.sql.connector.expressions.Transform
import org.apache.spark.sql.sources.DataSourceRegister
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.util.CaseInsensitiveStringMap

import java.util

class RabbitMQSourceProvider extends TableProvider with DataSourceRegister {

  override def inferSchema(options: CaseInsensitiveStringMap): StructType = {
    StructType(
      Array(StructField("value", StringType))
    )
  }

  override def getTable(
      schema: StructType,
      partitioning: Array[Transform],
      properties: util.Map[String, String]
  ): Table = {
    RabbitMQTable(schema, properties)
  }

  override def shortName(): String = "rabbitmq"
}
