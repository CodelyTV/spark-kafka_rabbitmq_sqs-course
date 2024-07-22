package com.codely.lesson_03_spark_streaming_sqs.video_02__sqs_integration

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SQSReceiverSparkApp extends App {
  private val sqsEndpoint = "http://localhost:4566"
  private val region      = "us-east-1"
  private val queueUrl    = "http://localhost:4566/000000000000/send_welcome_email_on_user_registered"

  val conf =
    new SparkConf().setAppName("SQSReceiverSparkApp").setMaster("local[*]")

  val ssc = new StreamingContext(conf, Seconds(1))

  val receiver = new SQSSparkReceiver(sqsEndpoint, region, queueUrl)

  val messages = ssc.receiverStream(receiver)

  messages.print()

  ssc.start()

  ssc.awaitTermination()

}
