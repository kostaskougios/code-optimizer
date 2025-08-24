package codeoptimizer

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class SeqOpsTest extends AnyFunSuiteLike:
  for i <- 0 to 10 do
    for j <- 0 to i + 1 do
      test(s"filterMap list of $i size and $j filter"):
        val l    = (0 until i).toList
        val lops = SeqOps.filterMap[Int, Int](l, _ >= j, _ * 2)
        val n    = l.filter(_ >= j).map(_ * 2)
        lops should be(n)

      test(s"mapFilter list of $i size and $j filter"):
        val l    = (0 until i).toList
        val lops = SeqOps.mapFilter[Int, Int](l, _ * 2, _ >= j * 2)
        val n    = l.map(_ * 2).filter(_ >= j * 2)
        lops should be(n)

      test(s"withFilterForeach list of $i size and $j filter"):
        val l    = (0 until i).toList
        var sum1 = 0
        SeqOps.withFilterForeach[Int, Unit](l, _ >= j, sum1 += _)
        var sum2 = 0
        l.withFilter(_ >= j).foreach(sum2 += _)
        sum1 should be(sum2)

      test(s"filterForall list of $i size and $j filter"):
        val l = (0 until i).toList
        SeqOps.filterForall[Int](l, _ >= j, _ >= j) should be(true)
        SeqOps.filterForall[Int](l, _ >= j, _ < 0) should be(!l.exists(_ >= j))

      test(s"mapFind list of $i size and $j find"):
        val l        = (0 until i).toList
        val expected = if i == 0 || j >= i then None else Some(j * 2)
        SeqOps.mapFind(l, _ * 2, _ == j * 2) should be(expected)
