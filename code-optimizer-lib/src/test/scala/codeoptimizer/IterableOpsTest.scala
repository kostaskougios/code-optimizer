package codeoptimizer
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

import scala.collection.immutable.IntMap

class IterableOpsTest extends AnyFunSuiteLike:
  val iterable1 = IntMap(1 -> 10, 2 -> 20, 3 -> 30).values // create a non-list iterable
  test("filterMap"):
    IterableOps.filterMap(iterable1, _ > 1, _ * 2) should be(Iterable(20, 40, 60))
    IterableOps.filterMap(iterable1, _ > 10, _ * 2) should be(Iterable(40, 60))
    IterableOps.filterMap(iterable1, _ > 40, _ * 2) should be(Iterable.empty)
