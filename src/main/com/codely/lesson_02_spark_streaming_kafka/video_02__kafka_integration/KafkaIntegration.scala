package com.codely.lesson_02_spark_streaming_kafka.video_02__kafka_integration

import com.codely.lesson_02_spark_streaming_kafka.video_02__kafka_integration.commons.Schemas
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{avg, col, explode, from_json, to_timestamp, window}
import org.apache.spark.sql.streaming.OutputMode

object KafkaIntegration extends App {

  val spark = SparkSession
    .builder()
    .appName("kafkaIntegration")
    .master("local[*]")
    .getOrCreate()

  spark.sparkContext.setLogLevel("WARN")

  import spark.implicits._

  val kafkaDF = spark.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("subscribe", "topic-events")
    .load()
    .select(
      from_json(col("value").cast("string"), Schemas.purchasedSchema)
        .as("value")
    )
    .select("value.*")

  val avgSpendingPerUserDF = kafkaDF
    .withColumn(
      "timestamp",
      to_timestamp($"timestamp", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    )
    .withWatermark("timestamp", "1 hours")
    .select(explode($"products").as("product"), $"timestamp")
    .select(
      $"timestamp",
      ($"product.price" * $"product.quantity").alias("totalSpent")
    )
    .groupBy(window($"timestamp", "24 hours"))
    .agg(avg("totalSpent").alias("AvgSpending"))

  avgSpendingPerUserDF.writeStream
    .format("console")
    .outputMode(OutputMode.Update())
    .option("numRows", 100)
    .option("truncate", "false")
    .start()
    .awaitTermination()
}
