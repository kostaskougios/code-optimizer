package codeoptimizer

import dotty.tools.dotc.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*

class StatisticsCollector(using Context) extends AbstractOptimizer:
  private val IterableClass = requiredClass("scala.collection.Iterable").typeRef.appliedTo(TypeBounds.empty)
  private val SeqClass      = defn.SeqClass.typeRef.appliedTo(TypeBounds.empty)

  override def transformApply(tree: Apply)(using Context): Apply =
    tree match
      case Apply(
            TypeApply(
              Select(
                Apply(
                  Select(seqExpr, call1),
                  List(call1Param)
                ),
                call2
              ),
              List(call2Type)
            ),
            List(call2Param)
          ) if seqExpr.tpe <:< IterableClass =>

        val iterableName = seqExpr.tpe.widenTermRefExpr.typeSymbol.name.show
        Statistics.inc(s"$iterableName.${call1.mangledString}.${call2.mangledString}")
        tree
      case _ =>
        tree
