package akka_akka_streams.AkkaDataStreams

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object  AkkaStreams extends App {
  implicit val systme = ActorSystem("system")
  implicit val materializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 10)
  val flow = Flow[Int].map(x=>x+1)
  val sink = Sink.foreach[Int](println)

//  source.via(flow).to(sink).run()

  source.async
    .via(flow).async
    .via(flow).async
    .to(sink).run()


}