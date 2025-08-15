package codeoptimizer

import dotty.tools.dotc.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*

class StatisticsCollector3CallsAfterIterable(using Context) extends AbstractOptimizer:
  private val IterableClass = requiredClass("scala.collection.Iterable").typeRef.appliedTo(TypeBounds.empty)

  override def transformApply(tree: Apply)(using Context): Apply =
    tree match
      case Apply(
            Select(
              Apply(
                Select(
                  Apply(
                    Select(seqExpr, call1),
                    List(call1Param)
                  ),
                  call2
                ),
                List(call2Param)
              ),
              call3
            ),
            List(call3Param)
          ) if seqExpr.tpe <:< IterableClass =>

        val iterableName = seqExpr.tpe.widenTermRefExpr.typeSymbol.name.show
        Statistics.inc(s"$iterableName.${call1.mangledString}.${call2.mangledString}.${call3.mangledString}")
        tree
      case _ =>
        tree
