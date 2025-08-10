package codeoptimizer

import codeoptimizer.Utils.elementFirstType
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

class FilterMapOptimizer extends PluginPhase:

  override val phaseName: String      = "filterMapOptimizer"
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

        val pos        = tree.span
        val sourceFile = ctx.source.file.name
        val lineNumber = ctx.source.offsetToLine(pos.start) + 1
        println(s"[${getClass.getSimpleName}] Optimizing filter→forall at $sourceFile:$lineNumber")

        val listOpsSym      = requiredModule("codeoptimizer.ListOps")
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
