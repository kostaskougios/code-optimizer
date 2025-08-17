package codeoptimizer
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

/**
 * The ops here will be replaced by the compiler plugin, and so we will test if the replacement
 * code works as expected.
 */
class DoesNotCorruptCodeTest extends AnyFunSuiteLike:
  test("filterMap"):
    (1 to 5).toList.filter(_ > 2).map(_ * 2).toSet should be(Set(3 * 2, 4 * 2, 5 * 2))
    (1 to 5).filter(_ > 2).map(_ * 2).toSet should be(Set(3 * 2, 4 * 2, 5 * 2))

  test("withFilterForeach"):
    var i = 0
    (1 to 5).toList.withFilter(_ > 2).foreach(i += _)
    i should be(3 + 4 + 5)

  test("filterForall"):
    (1 to 5).toList.filter(_ > 2).forall(_ > 2) should be(true)
    (1 to 5).toList.filter(_ > 2).forall(_ < 2) should be(false)

    (1 to 5).filter(_ > 2).forall(_ > 2) should be(true)
    (1 to 5).filter(_ > 2).forall(_ < 2) should be(false)

    Iterable(1, 2, 3, 4, 5).filter(_ > 2).forall(_ > 2) should be(true)
    Iterable(1, 2, 3, 4, 5).filter(_ > 2).forall(_ < 2) should be(false)
