package com.codely.lesson_04__spark_streaming_rabbitmq.video_03__rabbitmq_custom_provider

import org.apache.spark.sql.connector.read.InputPartition

import java.util

case class RabbitMQInputPartition(properties: util.Map[String, String]) extends InputPartition {}
