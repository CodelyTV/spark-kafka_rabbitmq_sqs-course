package com.codely.lesson_04__spark_streaming_rabbitmq.video_03__rabbitmq_custom_provider

import org.apache.spark.sql.connector.read.Scan
import org.apache.spark.sql.connector.read.streaming.MicroBatchStream
import org.apache.spark.sql.types.StructType
import java.util

class RabbitMQScan(schema: StructType, properties: util.Map[String, String]) extends Scan {
  override def readSchema(): StructType = schema

  override def toMicroBatchStream(checkpointLocation: String): MicroBatchStream = {
    RabbitMQMicroBatchStream(schema, properties, checkpointLocation)
  }
}
