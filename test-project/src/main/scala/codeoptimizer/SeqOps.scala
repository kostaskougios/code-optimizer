package codeoptimizer

object SeqOps:
  def filterMap[A, B](pred: A => Boolean, mapper: A => B, xs: Seq[A]): Seq[B] =
    xs.iterator.filter(pred).map(mapper).toSeq

  def filterForall[A](pred: A => Boolean, all: A => Boolean, xs: Seq[A]): Boolean =
    xs.iterator.filter(pred).forall(all)
