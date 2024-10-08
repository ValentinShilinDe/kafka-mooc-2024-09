object functions {
  def main(args: Array[String]): Unit = {
    def sum(x:Int, y: Int): Int = x + y
    println(sum(3,4))

    val evenNumberToString: PartialFunction[Any, String] = {
      case x if x.isInstanceOf[Int] && x.asInstanceOf[Int] % 2 == 0 => s"${x} is even"
    }

    println(evenNumberToString.isDefinedAt(2)) // true
    println(evenNumberToString.isDefinedAt(3)) // false

    if (evenNumberToString.isDefinedAt(4)) {
      println(evenNumberToString(4))
    }

    val numberToString: PartialFunction[Any, String] = evenNumberToString.orElse {
      case x if x.isInstanceOf[String] => s"${x} is String"
      case x => s"x is odd"
    }


    List("sdf", 14, 15, 3,9,"sdfg").collect(numberToString).foreach(println)

  }


}