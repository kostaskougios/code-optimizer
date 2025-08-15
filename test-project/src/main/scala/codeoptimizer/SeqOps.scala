package codeoptimizer

object SeqOps:
  def filterMap[A, B](pred: A => Boolean, mapper: A => B, xs: Seq[A]): Seq[B] =
    xs match
      case l: List[A] =>
        if l.isEmpty then Nil
        else
          var remain = l
          val p1     = pred(remain.head)
          val m1     = if p1 then mapper(remain.head) else null.asInstanceOf[B]
          remain = l.tail
          if remain.isEmpty then if m1 == null then Nil else m1 :: Nil
          else
            val b = List.newBuilder[B]
            if m1 != null then b += m1
            while !remain.eq(Nil) do
              if pred(remain.head) then b += mapper(remain.head)
              remain = remain.tail

            b.result()

  def filterForall[A](pred: A => Boolean, all: A => Boolean, xs: Seq[A]): Boolean =
    xs.iterator.filter(pred).forall(all)
