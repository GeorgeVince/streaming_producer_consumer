package com.gv.sparkstreaming

import org.apache.spark._
import org.apache.log4j._
import scala.annotation.meta.companionClass

object Consume {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "HelloWorld")

    val lines = sc.textFile("data/hello.txt")
    val numLines = lines.count()

    println("Hello world! The u.data file has " + numLines + " lines.")

    sc.stop()
  }
}
