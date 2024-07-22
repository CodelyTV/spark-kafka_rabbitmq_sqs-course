package com.codely.lesson_03_spark_streaming_sqs.z_practical_exercise

import io.circe.generic.auto._
import io.circe.parser._
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.DStream

object SQSReceiverSparkApp extends App {
  private val sqsEndpoint = "http://localhost:4566"
  private val region      = "us-east-1"
  private val queueUrl    = "http://localhost:4566/000000000000/send_welcome_email_on_user_registered"

  val conf = new SparkConf().setAppName("SQSReceiverSparkApp").setMaster("local[*]")

  val ssc = new StreamingContext(conf, Seconds(5))

  val receiver = new SQSSparkReceiver(sqsEndpoint, region, queueUrl)

  val messages: DStream[String] = ssc.receiverStream(receiver)

  case class Detail(detail_type: String, user_id: String, email: String, timestamp: String)
  case class Event(detail: Detail)

  val filteredMessages = messages.flatMap { message =>
    decode[Event](message) match {
      case Right(event) if event.detail.detail_type == "userRegistered" => Some(event)
      case _                                                            => None
    }
  }

  val eventsDStream = filteredMessages.map { event =>
    (event.detail.user_id, event.detail.timestamp)
  }

  val windowedCounts = eventsDStream
    .map { case (userId, _) => (userId, 1) }
    .reduceByKeyAndWindow(_ + _, Seconds(300))

  windowedCounts.print()

  ssc.start()
  ssc.awaitTermination()
}
