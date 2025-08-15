package codeoptimizer

object ListOps:
  def filterMap[A, B](pred: A => Boolean, mapper: A => B, l: List[A]): List[B] =
    if l.isEmpty then Nil
    else
      var remain = l
      val b      = List.newBuilder[B]
      while !(remain eq Nil) do
        val h = remain.head
        if pred(h) then b += mapper(h)
        remain = remain.tail

      b.result()
