package codeoptimizer
import scala.collection.mutable

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

  def mapFilter[A, B](l: List[A], mapper: A => B, pred: B => Boolean): List[B] =
    if l.isEmpty then Nil
    else
      var remain = l
      val b      = List.newBuilder[B]
      while !(remain eq Nil) do
        val h = mapper(remain.head)
        if pred(h) then b += h
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

  def mapFind[A, B](l: List[A], mapper: A => B, pred: B => Boolean): Option[B] =
    if l.isEmpty then None
    else
      var these = l
      while !these.isEmpty do
        val h = mapper(these.head)
        if pred(h) then return Some(h)
        these = these.tail
      None

  private val TupleOfNil = (Nil, Nil)

  def mapPartition[A, B](list: List[A], mapper: A => B, partitionPred: B => Boolean): (List[B], List[B]) =
    if list.isEmpty then TupleOfNil
    else
      val l     = List.newBuilder[B]
      val r     = List.newBuilder[B]
      var these = list
      while !these.isEmpty do
        val h = mapper(these.head)
        if partitionPred(h) then l += h
        else r += h
        these = these.tail
      (l.result(), r.result())
