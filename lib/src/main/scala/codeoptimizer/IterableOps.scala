package codeoptimizer

object IterableOps:
  def filterMap[A, B](pred: A => Boolean, mapper: A => B, xs: Iterable[A]): Iterable[B] =
    xs match
      case s: Seq[A] => SeqOps.filterMap(pred, mapper, s)
      case _         => xs.filter(pred).map(mapper).to(Iterable)

  def filterForall[A](pred: A => Boolean, all: A => Boolean, xs: Iterable[A]): Boolean =
    xs match
      case s: Seq[A] => SeqOps.filterForall(pred, all, s)
      case _         => xs.filter(pred).forall(all)
