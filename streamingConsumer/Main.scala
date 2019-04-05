

import org.apache.commons.mail.{DefaultAuthenticator, SimpleEmail}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark._
import org.apache.spark.streaming._





object Main extends App {


  def sendMail(title: String, msg: String): Unit = {
    val email = new SimpleEmail()
    email.setHostName("smtp.googlemail.com")
    email.setSmtpPort(465)
    email.setAuthenticator(new DefaultAuthenticator("sparkalert25@gmail.com", "RLG251197"))
    email.setSSLOnConnect(true)
    email.setFrom("sparkalert25@gmail.com")
    email.setSubject(title)
    email.setMsg(msg)
    email.addTo("sparkalert25@gmail.com")
    email.send()

  }





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
    /*
    if(!df.isEmpty) {
      df.coalesce(1).write.mode(SaveMode.Append).csv("/home/romain/HDFakeSystem")
    }
    */

    if(!df.isEmpty) {
      // df.show()
      val countTotal = df
        .select("id")
        .distinct()
        .count()
      //println("\n"+"Nb = " + countTotal + "\n")

      val lowBattery = df
        .filter("fuel < 20")
        .filter("die == 'false'")
        .select(
          col = "id"
        )
        .distinct()
        .count()
      //println("\n" + "Number of device with low battery : " + lowBattery + "\n")

      val dangerousWeather = df
        .filter("weather == 'wind' OR weather == 'rain'")
        .filter("die == 'false'")
        .select(
          col = "id"
        )
        .distinct()
        .count()
      //println("\n" + "Number of device under bad weather condition: " + dangerousWeather + "\n")


      val deadInside = df
        .filter("die == 'true'")
        .select(
          col = "id"
        )
        .distinct()
        .count()
      //println("\n" + "Number of device that died recently: " + deadInside + "\n")

      //val myMsg = "\n" + "Number of device under bad weather condition: " + deadInside + "\n" + df.filter("die == 'true'").select( "id", "disease", "weather", "state", "cause", "lifetime", "temperature", "fuel").collect().map(_.toSeq).mkString("\n")

      if(lowBattery > (countTotal-deadInside) * 0.2) {
        sendMail("Low Fuel Alert",
          "\n" + "Number of device with low Fuel: " + dangerousWeather + "\n\n" +
            df.filter("fuel < 20 and die != true").filter("die == 'false'").select(col = "id").distinct().collect().map(_.toSeq).mkString("\n"))
      }
      if(dangerousWeather > (countTotal-deadInside) * 0.2) {
        sendMail("Weather Alert",
          "\n" + "Number of device under bad weather condition: " + dangerousWeather + "\n\n" +
            df.filter("weather == 'wind' OR weather == 'rain'").filter("die == 'false'").select("id").distinct().collect().map(_.toSeq).mkString("\n"))
      }
      if(deadInside > countTotal * 0.2) {
        sendMail("High number of dead device",
          "\n" + "Number of device that died recently: " + dangerousWeather + "\n\n" +
            df.filter("die == 'true'").select("id").distinct().collect().map(_.toSeq).mkString("\n"))
      }


    }
  })





  ssc.start()
  ssc.awaitTermination()

}

