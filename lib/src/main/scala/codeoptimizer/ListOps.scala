package codeoptimizer

object ListOps:
  def filterMap[A, B](l: List[A], pred: A => Boolean, mapper: A => B): List[B] =
    if l.isEmpty then Nil
    else
      var remain = l
      val b      = List.newBuilder[B]
      while !(remain eq Nil) do
        val h = remain.head
        if pred(h) then b += mapper(h)
        remain = remain.tail

      b.result()

  def withFilterForeach[A, U](l: List[A], pred: A => Boolean, f: A => U): Unit =
    var these = l
    while !these.isEmpty do
      val h = these.head
      if pred(h) then f(h)
      these = these.tail

  def filterForall[A](l: List[A], pred: A => Boolean, all: A => Boolean): Boolean =
    if l.isEmpty then true
    else
      var these = l
      while !these.isEmpty do
        val h = these.head
        if pred(h) && !all(h) then return false
        these = these.tail
      true
