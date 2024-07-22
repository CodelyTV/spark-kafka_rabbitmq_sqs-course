package com.codely.lesson_04__spark_streaming_rabbitmq.video_02__rabbitmq_integration

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object RabbitMQStreamingApp extends App {
  val conf =
    new SparkConf().setAppName("RabbitMQStreamingApp").setMaster("local[*]")

  val ssc = new StreamingContext(conf, Seconds(5))

  val stream =
    ssc.receiverStream(new RabbitMQReceiver("spark_queue", "localhost", 5672, "codely", "codely"))

  stream.print()

  ssc.start()

  ssc.awaitTermination()
}
