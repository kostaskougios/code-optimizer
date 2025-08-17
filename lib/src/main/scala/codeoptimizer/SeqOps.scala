package codeoptimizer

object SeqOps:
  def filterMap[A, B](pred: A => Boolean, mapper: A => B, xs: Seq[A]): Seq[B] =
    xs match
      case l: List[A] =>
        ListOps.filterMap(pred, mapper, l)
      case x          => throw IllegalStateException(s"NYI filterMap for ${x.getClass}")

  def filterForall[A](pred: A => Boolean, all: A => Boolean, xs: Seq[A]): Boolean =
    xs match
      case l: List[A] =>
        ListOps.filterForall(pred, all, l)
      case x          => throw IllegalStateException(s"NYI filterForall for ${x.getClass}")
