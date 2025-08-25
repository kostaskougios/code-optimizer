package codeoptimizer

class SeqOpsTest           extends AbstractIterableTest[Seq[Int]]("Seq"):
  override def filterMap(l: Seq[Int], pred: Int => Boolean, mapper: Int => Int)                          = SeqOps.filterMap(l, pred, mapper)
  override def mapFilter(l: Seq[Int], mapper: Int => Int, pred: Int => Boolean)                          = SeqOps.mapFilter(l, mapper, pred)
  override def withFilterForeach(l: Seq[Int], pred: Int => Boolean, f: Int => Unit): Unit                = SeqOps.withFilterForeach(l, pred, f)
  override def filterForall(l: Seq[Int], pred: Int => Boolean, all: Int => Boolean)                      = SeqOps.filterForall(l, pred, all)
  override def mapFind(l: Seq[Int], mapper: Int => Int, pred: Int => Boolean)                            = SeqOps.mapFind(l, mapper, pred)
  override def mapPartition(l: Seq[Int], mapper: Int => Int, pred: Int => Boolean): (Seq[Int], Seq[Int]) = SeqOps.mapPartition(l, mapper, pred)

  override def createIterable(i: Int): Seq[Int] = (0 until i).toVector
class SeqOpsButForListTest extends SeqOpsTest:
  override def createIterable(i: Int): Seq[Int] = (0 until i).toList
