package codeoptimizer

class ListOpsTest extends AbstractIterableTest[List[Int]]("List"):
  override def filterMap(l: List[Int], pred: Int => Boolean, mapper: Int => Int): List[Int] = ListOps.filterMap(l, pred, mapper)
  override def mapFilter(l: List[Int], mapper: Int => Int, pred: Int => Boolean)            = ListOps.mapFilter(l, mapper, pred)
  override def withFilterForeach(l: List[Int], pred: Int => Boolean, f: Int => Unit): Unit  = ListOps.withFilterForeach(l, pred, f)
  override def filterForall(l: List[Int], pred: Int => Boolean, all: Int => Boolean)        = ListOps.filterForall(l, pred, all)
  override def mapFind(l: List[Int], mapper: Int => Int, pred: Int => Boolean)              = ListOps.mapFind(l, mapper, pred)

  override def createIterable(i: Int): List[Int] = (0 until i).toList
