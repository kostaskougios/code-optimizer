package codeoptimizer

import codeoptimizer.Utils.{elementFirstType, reportOptimization}
import dotty.tools.dotc.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*

class ListWithFilterForeachOptimizer(using Context) extends AbstractOptimizer:
  private def method1       = "withFilter"
  private def method2       = "foreach"
  private def replacement   = "withFilterForeach"
  private val IterableClass = requiredClass("scala.collection.Iterable").typeRef.appliedTo(TypeBounds.empty)
  private val SeqClass      = defn.SeqClass.typeRef.appliedTo(TypeBounds.empty)
  private val ListClass     = defn.ListClass.typeRef.appliedTo(TypeBounds.empty)

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
          )
          if call1.mangledString == method1
            && call2.mangledString == method2
            && seqExpr.tpe <:< IterableClass =>

        val ops = if seqExpr.tpe <:< ListClass then "ListOps" else if seqExpr.tpe <:< SeqClass then "SeqOps" else "IterableOps"
        (for elementType <- elementFirstType(seqExpr) yield
          val opsSym    = requiredModule(s"codeoptimizer.$ops")
          val methodSym = opsSym.info.decl(termName(replacement))

          reportOptimization(getClass, s"$method1→$method2 to $ops.$replacement", tree)

          Apply(
            TypeApply(
              Select(ref(opsSym), methodSym.name),
              List(TypeTree(elementType), call2Type)
            ),
            List(call1Param, call2Param, seqExpr)
          ).withSpan(tree.span)
        ).getOrElse(tree)
      case _ =>
        tree
