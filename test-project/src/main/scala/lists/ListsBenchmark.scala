package lists

object ListsBenchmark:
  private val l                 = (1 to 1000).toList
  def runFilterMap(): List[Int] =
    var i = 0
    var r = List.empty[Int]
    while i < 1000 do
      l.filter(_ > 500).map(_ * 2)
      i += 1
    r
