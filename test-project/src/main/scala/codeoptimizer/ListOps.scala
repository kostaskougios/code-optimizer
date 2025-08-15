package codeoptimizer

object ListOps:
  def filterMap[A, B](pred: A => Boolean, mapper: A => B, l: List[A]): List[B] =
    if l.isEmpty then Nil
    else
      var remain = l
      val h1     = remain.head
      val p1     = pred(h1)
      remain = remain.tail
      if remain.isEmpty then if p1 then mapper(h1) :: Nil else Nil
      else
        val h2 = remain.head
        val p2 = pred(h2)
        remain = remain.tail
        if remain.isEmpty then
          if p1 && p2 then mapper(h1) :: mapper(h2) :: Nil
          else if p1 then mapper(h1) :: Nil
          else if p2 then mapper(h2) :: Nil
          else Nil
        else
          val b = List.newBuilder[B]
          if p1 then b += mapper(h1)
          if p2 then b += mapper(h2)
          while remain ne Nil do
            val h = remain.head
            if pred(h) then b += mapper(h)
            remain = remain.tail

          b.result()
