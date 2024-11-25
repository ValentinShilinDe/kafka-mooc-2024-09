package akka_akka_streams.AkkaDataStreams

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source, Zip}

object AkkaStreamGraph {
  implicit  val system = ActorSystem("zcxvf")
  implicit  val mat = ActorMaterializer()

  val  graph = GraphDSL.create() { implicit  builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._
    //1. source
    val input = builder.add(Source(1 to 100))
    val inc = builder.add(Flow[Int].map(x=>x+1))
    val mult = builder.add(Flow[Int].map(x=>x*10))
    val output = builder.add(Sink.foreach[(Int, Int)](println))

    // broadcast, zip
    val broadcast = builder.add(Broadcast[Int](2))
    val zip = builder.add(Zip[Int, Int])

    //3.shape
    input ~> broadcast
    broadcast.out(0) ~> inc ~> zip.in0
    broadcast.out(1) ~> mult ~> zip.in1

    zip.out ~> output

    //4 close shape
    ClosedShape


  }

  def main(args: Array[String]): Unit = {
    RunnableGraph.fromGraph(graph).run()
  }
}