package codeoptimizer
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

abstract class AbstractIterableTest[C <: IterableOnce[Int]](iterableName: String) extends AnyFunSuiteLike:
  def filterMap(l: C, pred: Int => Boolean, mapper: Int => Int): C
  def mapFilter(l: C, mapper: Int => Int, pred: Int => Boolean): C
  def withFilterForeach(l: C, pred: Int => Boolean, f: Int => Unit): Unit
  def filterForall(l: C, pred: Int => Boolean, all: Int => Boolean): Boolean
  def mapFind(l: C, mapper: Int => Int, pred: Int => Boolean): Option[Int]
  def createIterable(i: Int): C

  for i <- 0 to 10 do
    for j <- 0 to i + 1 do
      test(s"filterMap $iterableName of $i size and $j filter"):
        val l    = createIterable(i)
        val lops = filterMap(l, _ >= j, _ * 2)
        val n    = l.iterator.filter(_ >= j).map(_ * 2).toSeq
        lops should be(n)

      test(s"mapFilter $iterableName of $i size and $j filter"):
        val l    = createIterable(i)
        val lops = mapFilter(l, _ * 2, _ >= j * 2)
        val n    = l.iterator.map(_ * 2).filter(_ >= j * 2).toSeq
        lops should be(n)

      test(s"withFilterForeach $iterableName of $i size and $j filter"):
        val l    = createIterable(i)
        var sum1 = 0
        withFilterForeach(l, _ >= j, sum1 += _)
        var sum2 = 0
        l.iterator.withFilter(_ >= j).foreach(sum2 += _)
        sum1 should be(sum2)

      test(s"filterForall $iterableName of $i size and $j filter"):
        val l = createIterable(i)
        filterForall(l, _ >= j, _ >= j) should be(true)
        filterForall(l, _ >= j, _ < 0) should be(!l.iterator.exists(_ >= j))

      test(s"mapFind $iterableName of $i size and $j find"):
        val l        = createIterable(i)
        val expected = if i == 0 || j >= i then None else Some(j * 2)
        mapFind(l, _ * 2, _ == j * 2) should be(expected)
