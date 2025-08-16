package codeoptimizer
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
  val twoCallOptimizers                                          = List(WithFilterForeachOptimizer(), FilterMapOptimizer())
    .map: o =>
      ((o.method1, o.method2), o)
    .toMap
  override def transformApply(tree: Apply)(using Context): Apply =
    scanApply(tree) match
      case Some(rec) =>
        rec.calls match
          case call1 :: call2 :: _ =>
            twoCallOptimizers
              .get((call1.name.mangledString, call2.name.mangledString))
              .map: optimizer =>
                optimizer.transformApply(tree, rec.seqExpr, rec.calls)
              .getOrElse(tree)
          case _                   => tree
      case _         => tree

trait Optimizer:
  def transformApply(
      tree: Apply,
      seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type],
      calls: List[Call]
  )(using Context): Apply
