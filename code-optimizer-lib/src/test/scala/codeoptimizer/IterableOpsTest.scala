package codeoptimizer
import org.scalatest.matchers.should.Matchers.*

import scala.collection.immutable.IntMap

class IterableOpsTest extends AbstractIterableTest[Iterable[Int]]("Iterable"):
  override def filterMap(l: Iterable[Int], pred: Int => Boolean, mapper: Int => Int)           = IterableOps.filterMap(l, pred, mapper)
  override def mapFilter(l: Iterable[Int], mapper: Int => Int, pred: Int => Boolean)           = IterableOps.mapFilter(l, mapper, pred)
  override def withFilterForeach(l: Iterable[Int], pred: Int => Boolean, f: Int => Unit): Unit = IterableOps.withFilterForeach(l, pred, f)
  override def filterForall(l: Iterable[Int], pred: Int => Boolean, all: Int => Boolean)       = IterableOps.filterForall(l, pred, all)
  override def mapFind(l: Iterable[Int], mapper: Int => Int, pred: Int => Boolean)             = IterableOps.mapFind(l, mapper, pred)

  override def createIterable(i: Int): Iterable[Int] = (0 until i).map(j => j -> j).toMap.values // make sure we get an iterable and not a seq

  val iterable1 = IntMap(1 -> 10, 2 -> 20, 3 -> 30).values // create a non-list iterable

  test("filterMap"):
    IterableOps.filterMap(iterable1, _ > 1, _ * 2) should be(Iterable(20, 40, 60))
    IterableOps.filterMap(iterable1, _ > 10, _ * 2) should be(Iterable(40, 60))
    IterableOps.filterMap(iterable1, _ > 40, _ * 2) should be(Iterable.empty)

  test("filterForall"):
    IterableOps.filterForall(iterable1, _ > 10, _ > 10) should be(true)
    IterableOps.filterForall(iterable1, _ > 10, _ < 30) should be(false)
    IterableOps.filterForall(iterable1, _ > 10, _ <= 20) should be(false)
    IterableOps.filterForall(iterable1, _ > 100, _ <= 200) should be(true)

  test("filterForeach"):
    var r = 0
    IterableOps.withFilterForeach(iterable1, _ >= 20, r += _)
    r should be(50)

class IterableButSeqOpsTest extends IterableOpsTest:
  override def createIterable(i: Int): Iterable[Int] = (0 until i).toList
