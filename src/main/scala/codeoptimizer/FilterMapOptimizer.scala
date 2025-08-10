package codeoptimizer

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
  println("*** Optimizer loaded.")

  override def transformApply(tree: Apply)(using Context): Tree =
    tree match
      // Match: <seq>.filter(pred).map(mapper)
      case Apply(
            Select(
              Apply(Select(seqExpr, filterName), List(pred)),
              mapName
            ),
            List(mapper)
          )
          if filterName.mangledString == "filter"
            && mapName.mangledString == "forall"
            && seqExpr.tpe <:< defn.SeqClass.typeRef.appliedTo(TypeBounds.empty) =>
        val pos        = tree.span
        val sourceFile = ctx.source.file.name
        val lineNumber = ctx.source.offsetToLine(pos.start)
        println(
          s"[FilterMapOptimizer] Found filter→forall at $sourceFile:$lineNumber"
        )

        val listOpsSym   = requiredModule("codeoptimizer.ListOps")
        val filterMapSym = listOpsSym.info.decl(termName("filterForall")).symbol

        Apply(
          Select(ref(listOpsSym), filterMapSym.name),
          List(pred, mapper, seqExpr)
        ).withSpan(tree.span)

      case _ =>
        tree
