package com.codely.lesson_04__spark_streaming_rabbitmq.video_03__rabbitmq_custom_provider

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger

object RabbitMQStreamingApp extends App {

  val spark = SparkSession.builder
    .appName("RabbitMQ Spark Streaming Example")
    .master("local[*]")
    .getOrCreate()

  val rabbitMQOptions = Map(
    "queue.name"        -> "spark_queue",
    "rabbitmq.host"     -> "localhost",
    "rabbitmq.port"     -> "5672",
    "rabbitmq.username" -> "codely",
    "rabbitmq.password" -> "codely"
  )

  val df = spark.readStream
    .format(
      "rabbitmq"
    )
    .options(rabbitMQOptions)
    .load()

  df.printSchema()

  val query = df.writeStream
    .outputMode("append")
    .format("console")
    .trigger(Trigger.ProcessingTime("10 seconds"))
    .start()

  query.awaitTermination()

}
