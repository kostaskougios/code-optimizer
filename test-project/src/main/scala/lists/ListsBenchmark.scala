package lists

object ListsBenchmark:
  private val largeList = (1 to 1000).toList
  private val smallList = List(1, 2, 3)
  private val emptyList = List.empty[Int]
  private val listOfOne = List(5)

  def runFilterMapLarge(): List[Int] =
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

  def runFilterMapEmptyList(): List[Int] =
    var i = 0
    var r = List.empty[Int]
    while i < 1800000 do
      r = emptyList.filter(_ >= 2).map(_ * 2)
      i += 1
    r

  def runFilterMapListOfOne(): List[Int] =
    var i = 0
    var r = List.empty[Int]
    while i < 900000 do
      r = listOfOne.filter(_ >= 2).map(_ * 2)
      i += 1
    r
