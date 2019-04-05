name := "Producer"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "org.apache.kafka" % "kafka_2.12" % "2.1.0"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.4"


libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.11"
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
libraryDependencies += "play" % "play_2.10" % "2.1.0"

