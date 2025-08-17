package codeoptimizer
import dotty.tools.dotc.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*

class IterableOptimizer(using Context) extends AbstractOptimizer:
  private val iterableDotCall1DotCall2TypeOptimizers = List(WithFilterForeachOptimizer(), FilterMapOptimizer())
    .map: o =>
      ((o.method1, o.method2), o)
    .toMap

  private val iterableDotCall1DotCall2Optimizers = List(FilterForallOptimizer())
    .map: o =>
      ((o.method1, o.method2), o)
    .toMap

  private val iterableDotCall1TypeDotCall2Optimizers = List(MapFindOptimizer())
    .map: o =>
      ((o.method1, o.method2), o)
    .toMap

  private val IterableClass = requiredClass("scala.collection.Iterable").typeRef.appliedTo(TypeBounds.empty)

  private def transformIterableApply(tree: Apply)(using Context): (Boolean, Apply) =
    tree match
      // seq . call1 . call2 [X]
      case seqApply @ Apply(TypeApply(Select(Apply(Select(seqExpr, call1), call1Params), call2), call2Types), call2Params) if seqExpr.tpe <:< IterableClass =>
        iterableDotCall1DotCall2TypeOptimizers
          .get((call1.mangledString, call2.mangledString))
          .map: optimizer =>
            (true, optimizer.transformApply(seqApply, seqExpr, call1Params, call2Params, call2Types))
          .getOrElse((false, tree))
      // seq . call1 [X] . call2
      case seqApply @ Apply(Select(Apply(TypeApply(Select(seqExpr, call1), call1Params), call1Types), call2), call2Params)                                  =>
        iterableDotCall1TypeDotCall2Optimizers
          .get((call1.mangledString, call2.mangledString))
          .map: optimizer =>
            (true, optimizer.transformApply(seqApply, seqExpr, call1Params, call1Types, call2Params))
          .getOrElse((false, tree))
      // seq . call1 . call2
      case seqApply @ Apply(
            Select(Apply(Select(seqExpr, call1), call1Params), call2),
            call2Params
          ) if seqExpr.tpe <:< IterableClass =>
        iterableDotCall1DotCall2Optimizers
          .get((call1.mangledString, call2.mangledString))
          .map: optimizer =>
            (true, optimizer.transformApply(seqApply, seqExpr, call1Params, call2Params))
          .getOrElse((false, tree))

      case Apply(Select(app: Apply, call), callParams) =>
        transformIterableApply(app) match
          case (true, uApp) => (true, Apply(Select(uApp, call), callParams))
          case _            => (false, tree)

      case Apply(TypeApply(Select(app: Apply, call), callTypes), callParams) =>
        transformIterableApply(app) match
          case (true, uApp) => (true, Apply(TypeApply(Select(uApp, call), callTypes), callParams))
          case _            => (false, tree)
      case _                                                                 => (false, tree)

  override def transformApply(tree: Apply)(using Context): Apply =
    transformIterableApply(tree) match
      case (true, uTree) => uTree
      case _             => tree
