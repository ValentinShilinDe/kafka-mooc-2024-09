package akka_akka_streams.alpakka

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.Sink
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory
import akka.kafka.scaladsl.Consumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.{Level, Logger}
import scala.util.{Failure, Success}



object ConsumerApp extends App {
  implicit  val system: ActorSystem = ActorSystem("consumer")
  implicit val mat =ActorMaterializer()
  implicit val ec = system.dispatcher

  LoggerFactory
    .getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
    .asInstanceOf[Logger]
    .setLevel(Level.ERROR)

  val config = ConfigFactory.load()
  val consumerConf = config.getConfig("akka.kafka.consumer")
  val consumerSettings = ConsumerSettings(consumerConf, new StringDeserializer(), new StringDeserializer())

  val consumer = Consumer.plainSource(consumerSettings, Subscriptions.topics("test"))
    .runWith(Sink.foreach(x=> {

      println(x)

    }))



  consumer onComplete {
    case Success(_) => println("Done"); system.terminate()
    case Failure(err) => println(err.toString); system.terminate()
  }


}
