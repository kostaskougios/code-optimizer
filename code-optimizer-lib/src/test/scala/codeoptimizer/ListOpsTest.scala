package codeoptimizer

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class ListOpsTest extends AnyFunSuiteLike:
  for i <- 0 to 10 do
    for j <- 0 to i + 1 do
      test(s"filterMap list of $i size and $j filter"):
        val l    = (0 until i).toList
        val lops = ListOps.filterMap[Int, Int](l, _ >= j, _ * 2)
        val n    = l.filter(_ >= j).map(_ * 2)
        // println(s"$l , $lops , $n")
        lops should be(n)

      test(s"withFilterForeach list of $i size and $j filter"):
        val l    = (0 until i).toList
        var sum1 = 0
        ListOps.withFilterForeach[Int, Unit](l, _ >= j, sum1 += _)
        var sum2 = 0
        l.withFilter(_ >= j).foreach(sum2 += _)
        sum1 should be(sum2)

      test(s"filterForall list of $i size and $j filter"):
        val l = (0 until i).toList
        ListOps.filterForall[Int](l, _ >= j, _ >= j) should be(true)
        ListOps.filterForall[Int](l, _ >= j, _ < 0) should be(!l.exists(_ >= j))
