

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.sql.SaveMode




object Main extends App {


  val conf = new SparkConf().setAppName("kafkAlert").setMaster("local[*]")
  val ssc = new StreamingContext(conf, Seconds(20))//Minutes(10))//
  val spark = SparkSession.builder().appName("Spark SQL").getOrCreate()
  import spark.implicits._
  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
    "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
    "group.id" -> "kafkAlert",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )

  val topics = Array("beeData")
  val stream = KafkaUtils.createDirectStream[String, String](
    ssc,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )


  stream.foreachRDD(rddRaw => {
    val rdd = rddRaw.map(_.value.toString)
    val df = spark.read.json(rdd)

    if(!df.isEmpty) {
      df.coalesce(1).write.mode(SaveMode.Append).csv("/home/romain/HDFakeSystem")
    }
  })




  ssc.start()
  ssc.awaitTermination()

}

