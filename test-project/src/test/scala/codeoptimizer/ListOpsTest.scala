package codeoptimizer

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class ListOpsTest extends AnyFunSuiteLike:
  for i <- 0 to 10 do
    test(s"filterMap list of $i size"):
      val l = (0 to i - 1).toList
      println(l)
      ListOps.filterMap[Int, Int](_ > i / 2, _ * 2, l) should be(l.filter(_ > i / 2).map(_ * 2))
