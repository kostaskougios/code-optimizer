package codeoptimizer

object SeqOps:
  def filterMap[A, B](xs: Seq[A], pred: A => Boolean, mapper: A => B): Seq[B] =
    xs match
      case l: List[A] =>
        ListOps.filterMap(l, pred, mapper)
      case x          => throw IllegalStateException(s"NYI filterMap for ${x.getClass}")

  def filterForall[A](xs: Seq[A], pred: A => Boolean, all: A => Boolean): Boolean =
    xs match
      case l: List[A] =>
        ListOps.filterForall(l, pred, all)
      case x          => throw IllegalStateException(s"NYI filterForall for ${x.getClass}")

  def withFilterForeach[A, U](s: Seq[A], pred: A => Boolean, f: A => U): Unit =
    s match
      case l: List[A] =>
        ListOps.withFilterForeach(l, pred, f)
      case x          => throw IllegalStateException(s"NYI withFilterForeach for ${x.getClass}")
