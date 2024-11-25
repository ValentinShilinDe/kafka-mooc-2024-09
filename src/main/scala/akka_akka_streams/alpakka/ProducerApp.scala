package akka_akka_streams.alpakka

import akka.Done
import akka.actor.ActorSystem
import scala.util.{Failure, Success}
import akka.kafka.ProducerSettings
import akka.stream.scaladsl.Source
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.ProducerRecord
import akka.kafka.scaladsl.Producer
import org.apache.kafka.common.serialization.StringSerializer
import scala.concurrent.Future
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.{Level, Logger}



object ProducrApp extends App {
  implicit  val system: ActorSystem = ActorSystem("producer")
  implicit val mat =ActorMaterializer()
  implicit val ec = system.dispatcher


  val config = ConfigFactory.load()
  val producerConf = config.getConfig("akka.kafka.producer")
  val producerSettings = ProducerSettings(producerConf, new StringSerializer(), new StringSerializer())

  val producer: Future[Done] =
    Source(1 to 100)
      .map(value => {
        println(value)
        new ProducerRecord[String, String]("test", value.toString)})
      .runWith(Producer.plainSink(producerSettings))

  producer onComplete {
    case Success(_) => println("Done"); system.terminate()
    case Failure(err) => println(err.toString); system.terminate()
  }


}
