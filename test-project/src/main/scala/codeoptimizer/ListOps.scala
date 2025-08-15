package codeoptimizer

object ListOps:
  def filterMap[A, B](pred: A => Boolean, mapper: A => B, l: List[A]): List[B] =
    if l.isEmpty then Nil
    else
      var remain = l
      val p1     = pred(remain.head)
      remain = remain.tail
      if remain.isEmpty then if p1 then mapper(l.head) :: Nil else Nil
      else
        val p2 = pred(remain.head)
        remain = remain.tail
        if remain.isEmpty then
          if p1 && p2 then mapper(l.head) :: mapper(l.tail.head) :: Nil
          else if p1 then mapper(l.head) :: Nil
          else if p2 then mapper(l.tail.head) :: Nil
          else Nil
        else
          val b = List.newBuilder[B]
          if p1 then b += mapper(l.head)
          if p2 then b += mapper(l.tail.head)
          while !remain.eq(Nil) do
            if pred(remain.head) then b += mapper(remain.head)
            remain = remain.tail

          b.result()
