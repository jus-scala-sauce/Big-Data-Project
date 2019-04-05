name := "Consumer"

/*
version := "0.1"
scalaVersion := "2.11.8"
//2.12.8
val sparkVersion = "2.1.0"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.7"



libraryDependencies += "org.apache.kafka" % "kafka_2.12" % "2.1.0"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.4"
// libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % sparkVersion




// libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.0.0"
*/

version := "0.1"

scalaVersion := "2.12.8"//"2.11.8"
val sparkVersion = "2.4.0"//"2.1.0"
val jacksonVersion = "2.6.7.1"//"2.9.7"

//dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion
//dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % jacksonVersion
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.4"
libraryDependencies += "org.apache.kafka" % "kafka_2.12" % "2.1.0" //"2.11"
// libraryDependencies += "com.google.code.gson" % "gson" % "2.7"


libraryDependencies += "org.apache.spark" % "spark-core_2.12" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-streaming_2.12" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.12" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-sql_2.12" % sparkVersion
libraryDependencies += "org.apache.commons" % "commons-email" % "1.5"