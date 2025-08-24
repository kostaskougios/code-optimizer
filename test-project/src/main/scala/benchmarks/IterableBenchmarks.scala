package benchmarks
import scala.collection.immutable.IntMap

object IterableBenchmarks:
  private val iterableSmall = IntMap(1 -> 10, 2 -> 20, 3 -> 30).values // create a non-list iterable

  def runFilterMapSmall(): Iterable[Int] =
    var i = 0
    var r = Iterable.empty[Int]
    while i < 300000 do
      r = iterableSmall.filter(_ >= 2).map(_ * 2)
      i += 1
    r

  def runFilterForAllSmall(): Boolean =
    var i = 0
    var b = false
    while i < 300000 do
      b = iterableSmall.filter(_ >= 2).forall(_ < 50)
      i += 1
    b

  def runWithFilterForeachSmall(): Int =
    var i = 0
    var r = 0
    while i < 900000 do
      iterableSmall.withFilter(_ >= 2).foreach(r += _ * 2)
      i += 1
    r

  def runMapFilterSmall(): Iterable[Int] =
    var i = 0
    var r = Iterable.empty[Int]
    while i < 300000 do
      r = iterableSmall.map(_ * 2).filter(_ >= 2)
      i += 1
    r
