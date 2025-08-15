package codeoptimizer

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class ListOpsTest extends AnyFunSuiteLike:
  for i <- 0 to 10 do
    for j <- 0 to i + 1 do
      test(s"filterMap list of $i size and $j filter"):
        val l    = (0 until i).toList
        val lops = ListOps.filterMap[Int, Int](_ >= j, _ * 2, l)
        val n    = l.filter(_ >= j).map(_ * 2)
        // println(s"$l , $lops , $n")
        lops should be(n)
