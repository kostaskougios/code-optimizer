package codeoptimizer

import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Names.Name
import dotty.tools.dotc.core.Types.*

class StatisticsCollectorForIterable(using Context) extends AbstractOptimizer:
  private def recordStats(seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type], calls: List[Call]): Unit =
    val iterableName = seqExpr.tpe.widenTermRefExpr.typeSymbol.name.show
    Statistics.inc(s"$iterableName.${calls.map(_.name.mangledString).mkString(".")}")

  override def transformApply(tree: Apply)(using Context): Apply =
    scanApply(tree) match
      case Some(rec) if rec.calls.size > 1 => recordStats(rec.seqExpr, rec.calls)
      case _                               =>
    tree
