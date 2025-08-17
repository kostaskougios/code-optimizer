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

  private def collectStats(tree: Apply)(using Context): Unit =
    tree match
      // 1nd call has type args
      case Apply(Select(Apply(TypeApply(Select(seqExpr, call1), call1Params), call1Types), call2), call2Params) if seqExpr.tpe <:< IterableClass =>
        recordStats(seqExpr, List(call1, call2))

      // 2st call has type arg
      case Apply(TypeApply(Select(Apply(Select(seqExpr, call1), call1Params), call2), call2Types), call2Params) if seqExpr.tpe <:< IterableClass =>
        recordStats(seqExpr, List(call1, call2))
      // both calls have type args
      case Apply(TypeApply(Select(Apply(TypeApply(Select(seqExpr, call1), call1Params), call1Types), call2), call2Types), call2Params)
          if seqExpr.tpe <:< IterableClass =>
        recordStats(seqExpr, List(call1, call2))

      // no calls have type args
      case Apply(Select(Apply(Select(seqExpr, call1), call1Params), call2), call2Params) if seqExpr.tpe <:< IterableClass =>
        recordStats(seqExpr, List(call1, call2))

      // Calls that might be before iterable calls.
      case Apply(Select(app: Apply, call), callParams)                       => collectStats(app)
      case Apply(TypeApply(Select(app: Apply, call), callTypes), callParams) => collectStats(app)
      case _                                                                 =>

  private def recordStats(seqExpr: Tree, calls: List[Name])(using Context): Unit =
    val iterableName = seqExpr.tpe.widenTermRefExpr.typeSymbol.name.show
    val callsStr     = calls.map(_.mangledString).mkString(".")
    reportMsg(getClass, s"${iterableName}.$callsStr", seqExpr)
    Statistics.inc(s"$iterableName.$callsStr")

  override def transformApply(tree: Apply)(using Context): Apply =
    collectStats(tree)
    tree
