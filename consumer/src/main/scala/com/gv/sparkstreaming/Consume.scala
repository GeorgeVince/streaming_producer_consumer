package com.gv.sparkstreaming

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.sql._
import org.apache.log4j._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession

import scala.annotation.meta.companionClass

object Consume {

  case class RiderEntry(name:String, address:String, event_time:String, event_type:String)


     def parseLog(x:Row) : Option[RiderEntry] = {
     
     val matcher:Matcher = logPattern.matcher(x.getString(0)); 
     if (matcher.matches()) {
       val timeString = matcher.group(4)
       return Some(LogEntry(
           matcher.group(1),
           matcher.group(2),
           matcher.group(3),
           parseDateField(matcher.group(4)).getOrElse(""),
           matcher.group(5),
           matcher.group(6),
           matcher.group(7),
           matcher.group(8),
           matcher.group(9)
           ))
     } else {
       return None
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

    val ds = dsRaw.selectExpr("CAST(value AS STRING)")
    
    val dsFormatted2 = dsRaw.flatMap(parseLog).select("status", "dateTime")

    val dsFormatted = ds.writeStream
      .format("console")
      .queryName("Write to console")
      .trigger(
        org.apache.spark.sql.streaming.Trigger.ProcessingTime("1 second")
      )
      .start()

    dsFormatted.awaitTermination()

    spark.stop()
  }
}
