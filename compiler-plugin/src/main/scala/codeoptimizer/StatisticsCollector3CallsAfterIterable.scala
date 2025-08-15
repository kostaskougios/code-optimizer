package codeoptimizer

import dotty.tools.dotc.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Names.Name
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*

class StatisticsCollector3CallsAfterIterable(using Context) extends AbstractOptimizer:
  private val IterableClass = requiredClass("scala.collection.Iterable").typeRef.appliedTo(TypeBounds.empty)

  private def recordStats(seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type], call1: Name, call2: Name): Unit =
    val iterableName = seqExpr.tpe.widenTermRefExpr.typeSymbol.name.show
    Statistics.inc(s"$iterableName.${call1.mangledString}.${call2.mangledString}")

  override def transformApply(tree: Apply)(using Context): Apply =
    tree match
      case Apply(
            Select(
              Apply(
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
              ),
              call3
            ),
            List(call3Param)
          ) if seqExpr.tpe <:< IterableClass =>

        recordStats(seqExpr, call1, call2)
        tree
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

        recordStats(seqExpr, call1, call2)
        tree
      case _ =>
        tree
