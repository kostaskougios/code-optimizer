package lists

object ListsBenchmark:
  private val largeList = (1 to 1000).toList
  private val smallList = List(1, 2, 3)

  def runFilterMap(): List[Int] =
    var i = 0
    var r = List.empty[Int]
    while i < 1000 do
      r = largeList.filter(_ > 500).map(_ * 2)
      i += 1
    r

  def runFilterMapSmallList(): List[Int] =
    var i = 0
    var r = List.empty[Int]
    while i < 300000 do
      r = smallList.filter(_ >= 2).map(_ * 2)
      i += 1
    r
