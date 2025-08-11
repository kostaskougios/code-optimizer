package codeoptimizer

import codeoptimizer.Utils.elementFirstType
import codeoptimizer.Utils.reportOptimization
import dotty.tools.dotc.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.NameOps.*
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.StdNames.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.plugins.*
import dotty.tools.dotc.transform.*

class IterableFilterMapOptimizer(using Context) extends AbstractOptimizer:
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
          )
          if call1.mangledString == "filter"
            && call2.mangledString == "map"
            && seqExpr.tpe <:< IterableClass =>

        val ops         = if seqExpr.tpe <:< SeqClass then "SeqOps" else "IterableOps"
        val elementType = elementFirstType(seqExpr)
        val opsSym      = requiredModule(s"codeoptimizer.$ops")
        val methodSym   = opsSym.info.decl(termName("filterMap"))

        reportOptimization(getClass, s"filter→map to $ops.filterMap", tree)

        Apply(
          TypeApply(
            Select(ref(opsSym), methodSym.name),
            List(TypeTree(elementType), call2Type)
          ),
          List(call1Param, call2Param, seqExpr)
        ).withSpan(tree.span)
      case _ =>
        tree
