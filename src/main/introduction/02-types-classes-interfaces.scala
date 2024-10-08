trait Show[A] {
  def show(a: A): String
}

object  Show {
  def show[A](a: A)(implicit sh: Show[A]): String = sh.show(a)

  implicit val igfhnjw: Show[Int] =
    new Show[Int] {
      def show(a: Int): String = s"int $a"
    }

  implicit val stringCanShow: Show[String] =
    new Show[String] {
      def show(a: String): String = s"str $a"
    }


  def main(args: Array[String]) : Unit = {
    println(show(20))
    println(show("zsadf"))
  }


}