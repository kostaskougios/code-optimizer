package lists

object ListsBenchmark:
  val r = List(1, 2, 3).filter(_ > 2).map(_ * 2)
  println(r)
