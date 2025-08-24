package codeoptimizer

import codeoptimizer.Utils.notYetOptimized

object SeqOps:
  private var filterMapNotOptimized                                           = true
  def filterMap[A, B](xs: Seq[A], pred: A => Boolean, mapper: A => B): Seq[B] =
    xs match
      case l: List[A] =>
        ListOps.filterMap(l, pred, mapper)
      case x          =>
        if filterMapNotOptimized then
          filterMapNotOptimized = false
          notYetOptimized(x, "filterMap")
        xs.filter(pred).map(mapper)

  private var mapFilterNotOptimized                                           = true
  def mapFilter[A, B](xs: Seq[A], mapper: A => B, pred: B => Boolean): Seq[B] =
    xs match
      case l: List[A] =>
        ListOps.mapFilter(l, mapper, pred)
      case x          =>
        if mapFilterNotOptimized then
          mapFilterNotOptimized = false
          notYetOptimized(x, "filterMap")
        xs.map(mapper).filter(pred)

  private var filterForallNotOptimized                                            = true
  def filterForall[A](xs: Seq[A], pred: A => Boolean, all: A => Boolean): Boolean =
    xs match
      case l: List[A] =>
        ListOps.filterForall(l, pred, all)
      case x          =>
        if filterForallNotOptimized then
          filterForallNotOptimized = false
          notYetOptimized(x, "filterForall")
        xs.filter(pred).forall(all)

  private var withFilterForeachNotOptimized                                   = true
  def withFilterForeach[A, U](s: Seq[A], pred: A => Boolean, f: A => U): Unit =
    s match
      case l: List[A] =>
        ListOps.withFilterForeach(l, pred, f)
      case x          =>
        if withFilterForeachNotOptimized then
          withFilterForeachNotOptimized = false
          notYetOptimized(x, "withFilterForeach")
        s.withFilter(pred).foreach(f)

  private var mapFindNotOptimized                                             = true
  def mapFind[A, B](s: Seq[A], mapper: A => B, pred: B => Boolean): Option[B] =
    s match
      case l: List[A] =>
        ListOps.mapFind(l, mapper, pred)
      case x          =>
        if mapFindNotOptimized then
          mapFindNotOptimized = true
          notYetOptimized(x, "mapFind")
        s.map(mapper).find(pred)
