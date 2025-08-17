package codeoptimizer

import codeoptimizer.Utils.reportMsg
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

  def scanApply(tree: Apply)(using Context): Option[Rec] =
    val calls     = scanSeqApply(tree)
    val seqStarts = calls.dropWhile(c => !(c.selectOn.tpe <:< IterableClass))
    if seqStarts.isEmpty then None
    else Some(Rec(tree, seqStarts.head.selectOn, seqStarts))

  def scanSeqApply(tree: Trees.Tree[Type])(using Context): List[Call] =
    tree match
      case Apply(Select(app, call), callParams)                       => scanSeqApply(app) :+ Call(app, call, callParams, Nil)
      case Apply(TypeApply(Select(app, call), callTypes), callParams) => scanSeqApply(app) :+ Call(app, call, callParams, callTypes)
      case _                                                          => Nil

  private def recordStats(rec: Rec): Unit =
    val iterableName = rec.seqExpr.tpe.widenTermRefExpr.typeSymbol.name.show
    val callsStr     = rec.calls.map(_.name.mangledString).mkString(".")
    reportMsg(getClass, callsStr, rec.tree)
    Statistics.inc(s"$iterableName.$callsStr")

  override def transformApply(tree: Apply)(using Context): Apply =
    scanApply(tree) match
      case Some(rec) if rec.calls.size > 1 => recordStats(rec)
      case _                               =>
    tree

case class Rec(
    tree: Apply,
    seqExpr: Trees.Tree[Type],
    calls: List[Call]
):
  def +(call: Call) = copy(calls = calls :+ call)

case class Call(
    selectOn: Trees.Tree[Type],
    name: Name,
    callParams: List[Trees.Tree[Type]],
    callTypes: List[Trees.Tree[Type]]
)
