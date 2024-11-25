package akka_akka_streams.AkkaDataStreams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Sink, Source}

object BackPressure extends App{
  val sourceFast = Source(1 to 1000)
  val flow = Flow[Int].map{el =>
    el +10
  }

  val sinkSlow = Sink.foreach[Int] { el =>
    Thread.sleep(1000)
    println(s"Sink insideL $el")
  }

  val flowWithBuffer = flow.buffer(10, overflowStrategy = OverflowStrategy.dropHead)

  implicit val system = ActorSystem("fxdcg")
  implicit  val mat = ActorMaterializer()

  sourceFast.via(flowWithBuffer).async
    .to(sinkSlow).run()



}