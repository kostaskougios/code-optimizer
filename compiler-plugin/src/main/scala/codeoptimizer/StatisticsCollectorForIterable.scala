package codeoptimizer

import dotty.tools.dotc.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Names.Name
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*

class StatisticsCollectorForIterable(using Context) extends AbstractOptimizer:
  private val IterableClass = requiredClass("scala.collection.Iterable").typeRef.appliedTo(TypeBounds.empty)

  private def recordStats(seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type], calls: List[Name]): Unit =
    val iterableName = seqExpr.tpe.widenTermRefExpr.typeSymbol.name.show
    Statistics.inc(s"$iterableName.${calls.map(_.mangledString).mkString(".")}")

  case class Rec(seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type], calls: List[Name]):
    def +(n: Name) = copy(calls = calls :+ n)

  def scanApply(tree: dotty.tools.dotc.ast.Trees.Tree[Type])(using Context): Option[Rec] =
    tree match
      case Apply(
            Select(seqExpr, call1),
            callParams
          ) if seqExpr.tpe <:< IterableClass =>
        Some(Rec(seqExpr, call1 :: Nil))
      case Apply(
            Select(
              app,
              call
            ),
            callParams
          ) =>
        scanApply(app) match
          case Some(rec) => Some(rec + call)
          case None      => None
      case Apply(
            TypeApply(
              Select(
                app,
                call
              ),
              callTypes
            ),
            callParams
          ) =>
        scanApply(app) match
          case Some(rec) => Some(rec + call)
          case None      => None
      case _ => None

  override def transformApply(tree: Apply)(using Context): Apply =
    scanApply(tree) match
      case Some(rec) => recordStats(rec.seqExpr, rec.calls)
      case _         =>
    tree
