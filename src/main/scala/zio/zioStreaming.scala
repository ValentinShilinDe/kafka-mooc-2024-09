package zio

import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.consumer.{Consumer, ConsumerSettings, Subscription}
import zio.stream.ZStream
import zio.kafka.serde._
import ch.qos.logback.classic.{Level, Logger}
import org.slf4j.LoggerFactory

object MainApp extends ZIOAppDefault {
  LoggerFactory
    .getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
    .asInstanceOf[Logger]
    .setLevel(Level.ERROR)

  val producer =
    ZStream
      .repeatZIO(Random.nextIntBetween(0, Int.MaxValue))
      .schedule(Schedule.fixed(2.seconds))
      .mapZIO { random =>
        Producer.produce[Any, Long, String](
          topic = "random",
          key = random % 4,
          value = random.toString,
          keySerializer = Serde.long,
          valueSerializer = Serde.string
        )}.drain

  val consumer =
    Consumer
      .plainStream(Subscription.topics("random"), Serde.long, Serde.string)
      .tap(r=> Console.printLine(r.value))
      .map(_.offset)
      .aggregateAsync(Consumer.offsetBatches)
      .mapZIO(_.commit)
      .drain

  def producerLayer = ZLayer.scoped(
    Producer.make(ProducerSettings(List("localhost:29093")))
  )

  def consumerLayer = ZLayer.scoped(
    Consumer.make(ConsumerSettings(List("localhost:29093")).withGroupId("group"))
  )

  override def run: ZIO[Any, Throwable, Unit] =
    producer.merge(consumer)
      .runDrain
      .provide(producerLayer, consumerLayer)


}

