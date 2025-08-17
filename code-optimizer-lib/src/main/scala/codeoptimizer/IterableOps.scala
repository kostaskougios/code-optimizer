package codeoptimizer

import codeoptimizer.Utils.notYetOptimized

object IterableOps:
  def filterMap[A, B](xs: Iterable[A], pred: A => Boolean, mapper: A => B): Iterable[B] =
    xs match
      case s: Seq[A] => SeqOps.filterMap(s, pred, mapper)
      case _         => xs.filter(pred).map(mapper).to(Iterable)

  def filterForall[A](xs: Iterable[A], pred: A => Boolean, all: A => Boolean): Boolean =
    xs match
      case s: Seq[A] => SeqOps.filterForall(s, pred, all)
      case _         => xs.filter(pred).forall(all)

  private var withFilterForeachNotOptimized                                        = true
  def withFilterForeach[A, U](s: Iterable[A], pred: A => Boolean, f: A => U): Unit =
    s match
      case s: Seq[A] =>
        SeqOps.withFilterForeach(s, pred, f)
      case x         =>
        if withFilterForeachNotOptimized then
          withFilterForeachNotOptimized = false
          notYetOptimized(x, "withFilterForeach")
        s.withFilter(pred).foreach(f)

  private var mapFindNotOptimized                                                  = true
  def mapFind[A, B](s: Iterable[A], mapper: A => B, pred: B => Boolean): Option[B] =
    s match
      case s: Seq[A] =>
        SeqOps.mapFind(s, mapper, pred)
      case x         =>
        if mapFindNotOptimized then
          mapFindNotOptimized = true
          notYetOptimized(x, "mapFind")
        s.map(mapper).find(pred)
