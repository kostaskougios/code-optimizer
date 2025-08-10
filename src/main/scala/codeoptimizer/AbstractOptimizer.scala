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

abstract class AbstractOptimizer extends PluginPhase:

  override val phaseName: String      = getClass.getSimpleName
  override val runsAfter: Set[String] = Set("typer")

  protected def firstCall: String
  protected def secondCall: String
  protected def implClass: String
  protected def implMethod: String
  override def transformApply(tree: Apply)(using Context): Tree =
    tree match
      case Apply(
            Select(
              Apply(Select(seqExpr, filterName), List(pred)),
              forallName
            ),
            List(forallPred)
          )
          if filterName.mangledString == firstCall
            && forallName.mangledString == secondCall
            && seqExpr.tpe <:< defn.SeqClass.typeRef.appliedTo(TypeBounds.empty) =>

        val elementType = elementFirstType(seqExpr)

        reportOptimization(getClass, s"$firstCall→$secondCall", tree)

        val listOpsSym      = requiredModule(s"codeoptimizer.$implClass")
        val filterForallSym = listOpsSym.info.decl(termName(implMethod))

        Apply(
          TypeApply(
            Select(ref(listOpsSym), filterForallSym.name),
            List(TypeTree(elementType))
          ),
          List(pred, forallPred, seqExpr)
        ).withSpan(tree.span)
      case _ =>
        tree
