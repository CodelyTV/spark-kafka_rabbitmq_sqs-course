package com.codely.lesson_02_spark_streaming_kafka.z_practical_exercise

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, explode, from_json, sum, to_timestamp, window}
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{ArrayType, DoubleType, IntegerType, StringType, StructType}

object KafkaIntegration extends App {

  val spark = SparkSession
    .builder()
    .appName("KafkaIntegration")
    .master("local[*]")
    .getOrCreate()

  spark.sparkContext.setLogLevel("WARN")

  import spark.implicits._

  val purchasedSchema = new StructType()
    .add("eventType", StringType)
    .add("timestamp", StringType)
    .add("userId", StringType)
    .add("transactionId", StringType)
    .add(
      "products",
      ArrayType(
        new StructType()
          .add("productId", StringType)
          .add("quantity", IntegerType)
          .add("description", StringType)
          .add("category", StringType)
          .add("price", DoubleType)
      )
    )
    .add("eventId", StringType)

  val kafkaDF = spark.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("subscribe", "topic-events")
    .load()
    .select(
      from_json(col("value").cast("string"), purchasedSchema)
        .as("value")
    )
    .select("value.*")

  val totalSpendingPerUserDF = kafkaDF
    .withColumn(
      "timestamp",
      to_timestamp($"timestamp", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    )
    .withWatermark("timestamp", "1 hour")
    .select($"userId", explode($"products").as("product"), $"timestamp")
    .select(
      $"userId",
      $"timestamp",
      ($"product.price" * $"product.quantity").alias("totalSpent")
    )
    .groupBy(window($"timestamp", "1 hour"), $"userId")
    .agg(sum("totalSpent").alias("TotalSpending"))

  totalSpendingPerUserDF.writeStream
    .format("console")
    .outputMode(OutputMode.Update())
    .option("numRows", 100)
    .option("truncate", "false")
    .start()
    .awaitTermination()
}
