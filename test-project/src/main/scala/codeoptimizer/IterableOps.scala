package codeoptimizer

object IterableOps:
  def filterMap[A, B](pred: A => Boolean, mapper: A => B, xs: Iterable[A]): Iterable[B] =
    xs.iterator.filter(pred).map(mapper).to(Iterable)
