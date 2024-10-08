object DataColelction {
  def main(args: Array[String]): Unit = {
    val l: List[String] = List("1", "2", "3")
    val collection1 = "line 1" :: "line 2" :: "line 3" :: "line 3" :: Nil
    val colelction2 = collection1.toSet
    colelction2.foreach(println)
    val collection3 = collection1.groupBy(x=>x).map(x=>x._1)
    collection3.foreach(println)

    println("!!!!!!!!!!")
    val iter = collection1.iterator
    while(iter.hasNext)
      println(iter.next)

    val demoCollection = 1 :: 2 :: 3 :: Nil
    println(s"fold result: ${demoCollection.fold(0)((z,i)=>z+i)}")
    println(s"fold left result: ${demoCollection.foldLeft(0)((z,i)=>z+i)}")
    println(s"reduce result: ${demoCollection.reduce((z,i)=>z+i)}")

    val test = List(1,2,3,4,5) :: List(1,50,3) :: List(1,2) :: Nil

    test.filter(x => x.sum > 10).foreach(x =>println(x.mkString(",")))

  }


}


