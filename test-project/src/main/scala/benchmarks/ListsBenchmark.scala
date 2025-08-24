package benchmarks

object ListsBenchmark:
  private val largeList   = (1 to 1000).toList
  private val smallList   = List(1, 2, 3)
  private val emptyList   = List.empty[Int]
  private val listOfOne   = List(5)
  private val listOfTwo   = List(5, 10)
  private val listOfThree = List(5, 10, 15)

  def runWithFilterForeachLarge(): Int =
    var x = 0
    var i = 0
    while i < 1000 do
      largeList
        .withFilter(_ > 500)
        .foreach: i =>
          x += i
      i += 1
    x

  def runWithFilterForeachSmall(): Int =
    var x = 0
    var i = 0
    while i < 600000 do
      listOfThree
        .withFilter(_ > 7)
        .foreach: i =>
          x += i
      i += 1
    x

  def runFilterMapLarge(): List[Int] =
    var i = 0
    var r = List.empty[Int]
    while i < 1000 do
      r = largeList.filter(_ > 500).map(_ * 2)
      i += 1
    r

  def runFilterMapSmall(): List[Int] =
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

  def runFilterMapListOfTwo(): List[Int] =
    var i = 0
    var r = List.empty[Int]
    while i < 700000 do
      r = listOfTwo.filter(_ >= 7).map(_ * 2)
      i += 1
    r

  def runFilterMapListOfThree(): List[Int] =
    var i = 0
    var r = List.empty[Int]
    while i < 600000 do
      r = listOfThree.filter(_ >= 7).map(_ * 2)
      i += 1
    r

  def runFilterForallLarge(): Boolean =
    var i = 0
    var b = false
    while i < 2000 do
      b = largeList
        .filter(_ > 500)
        .forall: i =>
          i > 500
      i += 1
    b

  def runFilterForallSmall(): Boolean =
    var i = 0
    var b = false
    while i < 300000 do
      b = smallList
        .filter(_ > 500)
        .forall: i =>
          i > 500
      i += 1
    b

  def runMapFindLarge(): Option[Int] =
    var i = 0
    var b = Option.empty[Int]
    while i < 2000 do
      b = largeList
        .map(_ * 2)
        .find(_ == 500)
      i += 1
    b

  def runMapFindSmall(): Option[Int] =
    var i = 0
    var b = Option.empty[Int]
    while i < 300000 do
      b = smallList
        .map(_ * 2)
        .find(_ == 6)
      i += 1
    b

  def runMapFilterLarge(): List[Int] =
    var i = 0
    var r = List.empty[Int]
    while i < 1000 do
      r = largeList.map(_ * 2).filter(_ > 500)
      i += 1
    r

  def runMapFilterSmall(): List[Int] =
    var i = 0
    var r = List.empty[Int]
    while i < 300000 do
      r = smallList.map(_ * 2).filter(_ >= 2)
      i += 1
    r
