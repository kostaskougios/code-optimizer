package codeoptimizer
import codeoptimizer.Utils.reportOptimization
import dotty.tools.dotc.*
import dotty.tools.dotc.ast.Trees
import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Names.Name
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*

class IterableOptimizer(using Context) extends AbstractOptimizer:
  private val iterableDotCall1DotCall2TypeOptimizers = List(WithFilterForeachOptimizer(), FilterMapOptimizer())
    .map: o =>
      ((o.method1, o.method2), o)
    .toMap
  private val IterableClass                          = requiredClass("scala.collection.Iterable").typeRef.appliedTo(TypeBounds.empty)

  private def transformSeqApply(tree: Trees.Tree[Type])(using Context): (Boolean, Trees.Tree[Type]) =
    tree match
      case seqApply @ Apply(Select(Apply(TypeApply(Select(seqExpr, call2), call2Types), call2Params), call1), call1Params) if seqExpr.tpe <:< IterableClass =>
        reportOptimization(getClass, s"Found $call1→$call2", tree)

        iterableDotCall1DotCall2TypeOptimizers
          .get((call1.mangledString, call2.mangledString))
          .map: optimizer =>
            (true, optimizer.transformApply(seqApply, seqExpr, call1Params, call2Params, call2Types))
          .getOrElse((false, tree))

      case Apply(Select(app, call), callParams) =>
        reportOptimization(getClass, s"Step ${app.tpe.show}.$call", tree)
        transformSeqApply(app) match
          case (true, uApp) => (true, Apply(Select(uApp, call), callParams))
          case _            => (false, tree)

      case Apply(TypeApply(Select(app, call), callTypes), callParams) =>
        transformSeqApply(app) match
          case (true, uApp) => (true, Apply(TypeApply(Select(uApp, call), callTypes), callParams))
          case _            => (false, tree)
      case _                                                          => (false, tree)

  override def transformApply(tree: Apply)(using Context): Apply =
    transformSeqApply(tree) match
      case (true, uTree) => uTree.asInstanceOf[Apply]
      case _             => tree
