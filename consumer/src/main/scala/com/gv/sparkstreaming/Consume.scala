package com.gv.sparkstreaming

import io.circe._, io.circe.parser.decode
import io.circe.generic.semiauto._

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.sql._
import org.apache.log4j._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession

import scala.annotation.meta.companionClass
import org.apache.spark.sql.types.LongType

object Consume {

  case class RiderEntry(
      name: String,
      address: String,
      event_time: String,
      event_type: String
  )

  def parseLog(x: Row): Option[RiderEntry] = {

    implicit val riderDec = deriveDecoder[RiderEntry]
    val r = decode[RiderEntry](x.getString(0))

    r match {
      case Right(rideEntry) => Some(rideEntry)
      case Left(l)          => None
    }

  }

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession.builder
      .appName("StructuredStreaming")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val dsRaw = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "riders")
      .load()

    val ds =
      dsRaw.selectExpr("CAST(value AS STRING)", "CAST(timestamp AS STRING)")

    val dsFormatted =
      ds.flatMap(parseLog).select("name", "address", "event_time", "event_type")

    dsFormatted.createOrReplaceTempView("logs")

    val dsStart = dsFormatted
      .filter(dsFormatted("event_type") === "START")
      .withColumnRenamed("event_time", "start_time")
      .withColumn("start_time", (col("start_time").cast("timestamp")))
      .withWatermark("start_time", "10 seconds")

    val dsEnd = dsFormatted
      .filter(dsFormatted("event_type") === "END")
      .withColumnRenamed("event_time", "end_time")
      .withColumn("end_time", (col("end_time").cast("timestamp")))
      .withWatermark("end_time", "10 seconds")
      .withColumnRenamed("name", "end_name")

    val dsCombined =
      dsStart.join(dsEnd, expr(""" name == end_name AND
                                   start_time <= end_time"""))

    val dsDifference = dsCombined.withColumn(
      "diffMinutes",
      col("end_time").cast(LongType) - col("start_time").cast(LongType)
    )

    // val dsOut = dsCombined.writeStream
    //   .outputMode("append")
    //   .format("csv")
    //   .option("checkpointLocation", "checkpoint/")
    //   .option("path", "hello.csv")
    //   .trigger(
    //     org.apache.spark.sql.streaming.Trigger.ProcessingTime("1 second")
    //   )
    //   .start()

    // dsOut.awaitTermination()

    val dsOut = dsStart.writeStream
      .format("console")
      .option("truncate", "false")
      .trigger(
        org.apache.spark.sql.streaming.Trigger.ProcessingTime("1 second")
      )
      .start()

    dsOut.awaitTermination()

    spark.stop()
  }
}
