import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.expr
import org.apache.spark.sql.functions._
import org.apache.spark.sql

import org.apache.log4j.Logger
import org.apache.log4j.Level

object Main extends App{
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  val spark = SparkSession.builder
    .master("local")
    .appName("beeskito")
    .getOrCreate()

  val moskitos = spark.read.format("json")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("/home/romain/IdeaProjects/Prototype/src/main/scala/data.json")

  moskitos.show(5)

  val count = moskitos
    .filter("die == true")
    .select(col = "id", "cause")
    .count()
  println("\n" + "Number of dead device in total : " + count + "\n")

  val northCount = moskitos
    .filter("x >= 0 AND die == true")
    .select(col = "id", "cause")
    .count()
  println("\n" + "Number of dead device in the north hemisphere : " + northCount + "\n")

  val southCount = moskitos
    .filter("x < 0 AND die == true")
    .select(col = "id", "cause")
    .count()
  println("\n" + "Number of dead device in the south hemisphere : " + southCount + "\n")

  println("\n" + "Number of dead device according to causes : " + "\n")
  val deadCauses = moskitos
    .filter("die == true")
    .select(col = "id", "cause")
    .groupBy("cause")
    .count()
    .show()

  println("\n" + "Number of dead device according to weather : " + "\n")
  val deadWeathers = moskitos
    .filter("die == true")
    .select("id", "weather")
    .groupBy("weather")
    .count()
    .show()

  val fuelCount = moskitos
    .filter("cause == 'fuel' AND die == true")
    .select("id")
    .count()

  println("\n" + "Percentage of dead device because of fuel : " + (fuelCount.toFloat / count.toFloat * 100) + "%" + "\n")

  println("\n" + "Number of dead device according to temperature : " + "\n")

  val expression = "temperature > 15"
  val deadTemperatures = moskitos
    .filter("die == true")
    .withColumn("hot", expr(expression))
    .select("id", "hot")
    .groupBy("hot")
    .count()
    .show()

  println("\n" + "Quantities of diseases recolted : " + "\n")
  val disease = moskitos
    .filter("disease != 'empty' AND disease != 'none'")
    .select("id", "disease")
    .groupBy("disease")
    .count()
    .show()

  val points = moskitos
    .select(moskitos("x").cast(sql.types.IntegerType).as("x"), moskitos("y").cast(sql.types.IntegerType).as("y"))
    .groupBy("x", "y")
    .count()
    .orderBy(desc("count"))
    .show(10)
}