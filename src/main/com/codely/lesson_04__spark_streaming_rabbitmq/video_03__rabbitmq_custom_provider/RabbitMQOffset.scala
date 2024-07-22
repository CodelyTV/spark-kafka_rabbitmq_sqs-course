package com.codely.lesson_04__spark_streaming_rabbitmq.video_03__rabbitmq_custom_provider

import org.apache.spark.sql.connector.read.streaming.Offset
import spray.json.DefaultJsonProtocol.LongJsonFormat
import spray.json._

case class RabbitMQOffset(offset: Long) extends Offset {
  override def json(): String = offset.toJson.toString()
}

object RabbitMQOffset {
  def fromJson(json: String): RabbitMQOffset = {
    val offset = json.parseJson.convertTo[Long]
    RabbitMQOffset(offset)
  }
}
