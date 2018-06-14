import org.apache.spark.sql.{SaveMode, SparkSession}
import org.json4s.{DefaultFormats, Formats, JsonFormat}
import org.json4s.jackson.JsonMethods

case class ConvertConfig(input: String, output: String, sets: Array[String], partitions: Int)

/**
  * Simple Spark Application to read in the yelp data-set and convert it to parquet
  */
object Convert extends App {
  // rudimentary way to handle options for the application with json under version control
  if (args.length >= 1) {
    println("Usage: spark-submit yelping.jar [options.json]")
    System.exit(1)
  }
  val optionsFile = args.headOption.getOrElse("options.json")

  implicit val format: Formats = DefaultFormats
  val config: ConvertConfig = JsonMethods.parse(getClass.getResourceAsStream(optionsFile)).extract[ConvertConfig]

  // the main up is straight forward, loading each of the supplied set and converting it into parquet
  val spark: SparkSession = SparkSession.builder().getOrCreate()
  for (set <- config.sets) {
    println(s"processing $set ...")
    
    val df = spark.read.json(s"${config.input}/$set.json")
    df.repartition(config.partitions).write.mode(SaveMode.Overwrite).parquet(s"${config.output}/$set")
  }  
}
