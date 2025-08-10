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
  protected def seqExprTypeCheck(seqExpr: Tree)(using Context): Boolean
  override def transformApply(tree: Apply)(using Context): Tree =
    tree match
      case Apply(
            Select(
              Apply(
                Select(seqExpr, call1),
                List(call1Param)
              ),
              call2
            ),
            List(call2Param)
          )
          if call1.mangledString == firstCall
            && call2.mangledString == secondCall
            && seqExprTypeCheck(seqExpr) =>

        reportOptimization(getClass, s"$firstCall→$secondCall", tree)

        val elementType     = elementFirstType(seqExpr)
        val listOpsSym      = requiredModule(s"codeoptimizer.$implClass")
        val filterForallSym = listOpsSym.info.decl(termName(implMethod))

        Apply(
          TypeApply(
            Select(ref(listOpsSym), filterForallSym.name),
            List(TypeTree(elementType))
          ),
          List(call1Param, call2Param, seqExpr)
        ).withSpan(tree.span)
      case _ =>
        tree
