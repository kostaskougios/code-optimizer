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
  val twoCallOptimizers                                          = Map(
    ("withFilter", "foreach") -> WithFilterForeachOptimizer()
  )
  override def transformApply(tree: Apply)(using Context): Apply =
    scanApply(tree) match
      case Some(rec) =>
        rec.calls match
          case List(call1, call2) =>
            twoCallOptimizers
              .get((call1.name.mangledString, call2.name.mangledString))
              .map: optimizer =>
                optimizer.transformApply(tree, rec.seqExpr, call1.callParams, call1.callTypes, call2.callParams, call2.callTypes)
              .getOrElse(tree)
          case _                  => tree
      case _         => tree

trait Optimizer:
  def transformApply(
      tree: Apply,
      seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type],
      call1Params: List[Trees.Tree[Type]],
      call1Types: List[Trees.Tree[Type]],
      call2Params: List[Trees.Tree[Type]],
      call2Types: List[Trees.Tree[Type]]
  )(using Context): Apply
