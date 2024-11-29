package zio

import zio.Console.printLine
import zio.stream.{ZPipeline, ZSink, ZStream}

object zioStreamingIntroduction extends App {
  val emptyStream = ZStream.empty

  val oneIntValueStream = ZStream.succeed(4)

  val oneListValueStream = ZStream.succeed(List(1,2,3,4))

  val rangeInitIntStream = ZStream.range(1,10)

  val infinitIntStream = ZStream.iterate(0)(_+1)

  val stream = ZStream(1,2,3)

  val unit = ZStream.unit

  val never = ZStream.never


  val s1 = ZStream.fromChunk(Chunk(1,2,3))
  val s2 = ZStream.fromChunks(Chunk(1,2,3), Chunk(1,2,3))

  val s3 = ZStream(1,2,3) ++ ZStream.fail("error") ++ ZStream(4,5)

  val s4 = ZStream(1,2,3)
  val stream1 = s3.orElse(s4)

  val stream3 = s3.catchSome{
    case "Error" => s4
    case "Error1" => s4
  }

  val sum = ZStream(1,2,3).run(ZSink.sum)

  val s7 = ZStream(1,2,3,4).runFold(0)(_+_)

  ZStream(1,2,3).foreach(printLine(_))

  val stream5 = ZStream.iterate(0)(_+1)
  val s8 = stream5.take(5)
  val s9 = stream5.takeWhile(_ < 5)
  val s10 = stream5.takeUntil( _ < 5)
  val s11 = s3.takeRight(10)


  val initStream = ZStream.fromIterable(0 to 10)
  val stringStream = initStream.map(_.toString)

  val s17 = ZStream.range(1,11).filter(_%2==0)
  val s18 = for {
    i <- ZStream.range(1,11).take(10)
    if i%2 == 0
  } yield i



}