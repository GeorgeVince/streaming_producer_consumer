ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

lazy val root = (project in file("."))
  .settings(
    name := "SparkStreaming"
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.1.2",
  "org.apache.spark" %% "spark-sql" % "3.1.2",
  "org.apache.spark" %% "spark-streaming" % "3.1.2",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.1.2",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.1.2"
)
