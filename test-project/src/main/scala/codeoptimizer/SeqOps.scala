package codeoptimizer

object SeqOps:
  def filterMap[A, B](pred: A => Boolean, mapper: A => B, xs: Seq[A]): Seq[B] =
    xs match
      case l: List[A] =>
        if l.isEmpty then Nil
        else
          val b      = List.newBuilder[B]
          var remain = l
          while !remain.isEmpty do
            if pred(remain.head) then b += mapper(remain.head)
            remain = remain.tail

          b.result()

  def filterForall[A](pred: A => Boolean, all: A => Boolean, xs: Seq[A]): Boolean =
    xs.iterator.filter(pred).forall(all)
