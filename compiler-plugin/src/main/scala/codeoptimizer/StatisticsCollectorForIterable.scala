package codeoptimizer

import dotty.tools.dotc.*
import dotty.tools.dotc.ast.Trees
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Names.Name
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*

class StatisticsCollectorForIterable(using Context) extends AbstractOptimizer:
  private val IterableClass = requiredClass("scala.collection.Iterable").typeRef.appliedTo(TypeBounds.empty)

  def scanApply(tree: Trees.Tree[Type])(using Context): Option[Rec] =
    val calls     = scanSeqApply(tree)
    val seqStarts = calls.dropWhile(c => !(c.selectOn.tpe <:< IterableClass))
    if seqStarts.isEmpty then None
    else Some(Rec(seqStarts.head.selectOn, seqStarts))

  def scanSeqApply(tree: dotty.tools.dotc.ast.Trees.Tree[Type])(using Context): List[Call] =
    tree match
      case Apply(Select(app, call), callParams)                       => scanSeqApply(app) :+ Call(app, call, callParams, Nil)
      case Apply(TypeApply(Select(app, call), callTypes), callParams) => scanSeqApply(app) :+ Call(app, call, callParams, callTypes)
      case _                                                          => Nil

  private def recordStats(seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type], calls: List[Call]): Unit =
    val iterableName = seqExpr.tpe.widenTermRefExpr.typeSymbol.name.show
    Statistics.inc(s"$iterableName.${calls.map(_.name.mangledString).mkString(".")}")

  override def transformApply(tree: Apply)(using Context): Apply =
    scanApply(tree) match
      case Some(rec) if rec.calls.size > 1 => recordStats(rec.seqExpr, rec.calls)
      case _                               =>
    tree

case class Rec(
    seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type],
    calls: List[Call]
):
  def +(call: Call) = copy(calls = calls :+ call)

case class Call(
    selectOn: Trees.Tree[Type],
    name: Name,
    callParams: List[Trees.Tree[Type]],
    callTypes: List[Trees.Tree[Type]]
)
