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

class FilterForallOptimizer extends PluginPhase:

  override val phaseName: String      = "FilterForallOptimizer"
  override val runsAfter: Set[String] = Set("typer")

  override def transformApply(tree: Apply)(using Context): Tree =
    tree match
      case Apply(
            Select(
              Apply(Select(seqExpr, filterName), List(pred)),
              forallName
            ),
            List(forallPred)
          )
          if filterName.mangledString == "filter"
            && forallName.mangledString == "forall"
            && seqExpr.tpe <:< defn.SeqClass.typeRef.appliedTo(TypeBounds.empty) =>

        val elementType = elementFirstType(seqExpr)

        reportOptimization(getClass, "filter→forall", tree)

        val listOpsSym      = requiredModule("codeoptimizer.SeqOps")
        val filterForallSym = listOpsSym.info.decl(termName("filterForall"))

        Apply(
          TypeApply(
            Select(ref(listOpsSym), filterForallSym.name),
            List(TypeTree(elementType))
          ),
          List(pred, forallPred, seqExpr)
        ).withSpan(tree.span)
      case _ =>
        tree
