import sbt._

object Dependencies {
  private val prod = Seq(
    "com.github.nscala-time" %% "nscala-time"          % "2.34.0",
    "com.lihaoyi"            %% "pprint"               % "0.9.0",
    "org.apache.spark"       %% "spark-core"           % "3.5.0" % Provided,
    "org.apache.spark"       %% "spark-sql"            % "3.5.0" % Provided,
    "org.apache.spark"       %% "spark-streaming"      % "3.5.0",
    "org.apache.spark"       %% "spark-hive"           % "3.5.0",
    "org.apache.spark"       %% "spark-sql-kafka-0-10" % "3.5.0",
    "org.apache.hadoop"       % "hadoop-aws"           % "3.2.2",
    "com.rabbitmq"            % "amqp-client"          % "5.23.0",
    "com.typesafe"            % "config"               % "1.4.3",
    "io.delta"               %% "delta-spark"          % "3.1.0",
    "io.spray"               %% "spray-json"           % "1.3.6",
    "io.circe"               %% "circe-core"           % "0.14.10",
    "io.circe"               %% "circe-generic"        % "0.14.10",
    "io.circe"               %% "circe-parser"         % "0.14.10"
  )
  private val test = Seq(
    "org.scalatest" %% "scalatest" % "3.2.19",
    "org.scalamock" %% "scalamock" % "6.0.0"
  ).map(_ % Test)

  val all: Seq[ModuleID] = prod ++ test
}
