object scalainto extends App {
  val a: Int = 1 + 1
  val b: Unit = println("sdgf")

  val inc: Int => Int = x => x+1
  lazy val cond: Boolean = true

  val x1: String = if (cond) "1" else "2"
  val x2: Any = if (cond) println("sdsdf") else "2"
  val x = do {
    println("ddddd")
  } while(!cond)

  val l = List(1,2,3)
  l.foreach(println)

}