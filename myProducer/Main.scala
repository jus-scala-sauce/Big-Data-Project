import java.util
import java.util.Calendar
import java.util.Properties

import play.api.libs.json._
import java.io.FileInputStream
import scala.io.Source
import java.util.Properties

import org.apache.kafka.clients.producer._



import org.apache.kafka.clients.producer._





object Main extends App {




  def sendOneKafka(myMessage: JsValue): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")

    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    val TOPIC="beeData"
    val record = new ProducerRecord(
                                    TOPIC,
                                    ((myMessage \ "id" ).get, (myMessage \ "timestamp" ).get).toString(),//myMessage().,
                                    myMessage.toString()
                                     )


    producer.send(record)
    producer.close()
  }

  def sendManyKafka(line: Int = 0, file: JsValue): Unit = {
    if(line < file.as[JsArray].value.size){
      //println(file{line})
      sendOneKafka(file{line})
      sendManyKafka(line+1, file)
    }
  }

   val stream = "/home/romain/IdeaProjects/Producer/src/main/scala/data.json"
  for(i <- Source.fromFile(stream).getLines()){
    //print(i)
    sendManyKafka(0,Json.parse(i))
  }
  // val test = try {  Json.parse(stream) } finally { stream.close() }




}
