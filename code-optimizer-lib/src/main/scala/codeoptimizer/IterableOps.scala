package codeoptimizer

object IterableOps:
  def filterMap[A, B](xs: Iterable[A], pred: A => Boolean, mapper: A => B): Iterable[B] =
    xs match
      case s: Seq[A] => SeqOps.filterMap(s, pred, mapper)
      case _         => xs.filter(pred).map(mapper).to(Iterable)

  def filterForall[A](xs: Iterable[A], pred: A => Boolean, all: A => Boolean): Boolean =
    xs match
      case s: Seq[A] => SeqOps.filterForall(s, pred, all)
      case _         => xs.filter(pred).forall(all)

  def withFilterForeach[A, U](s: Iterable[A], pred: A => Boolean, f: A => U): Unit =
    s match
      case s: Seq[A] =>
        SeqOps.withFilterForeach(s, pred, f)
      case x         => throw IllegalStateException(s"NYI withFilterForeach for ${x.getClass}")
