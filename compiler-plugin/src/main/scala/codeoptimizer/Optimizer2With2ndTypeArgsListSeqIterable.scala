package codeoptimizer

import codeoptimizer.Utils.elementFirstType
import codeoptimizer.Utils.reportOptimization
import dotty.tools.dotc.*
import dotty.tools.dotc.ast.Trees
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*

abstract class Optimizer2With2ndTypeArgsListSeqIterable(using Context) extends Optimizer:
  def method1: String
  def method2: String
  def replacement: String
  private val IterableClass = requiredClass("scala.collection.Iterable").typeRef.appliedTo(TypeBounds.empty)
  private val SeqClass      = defn.SeqClass.typeRef.appliedTo(TypeBounds.empty)
  private val ListClass     = defn.ListClass.typeRef.appliedTo(TypeBounds.empty)

  override def transformApply(
      tree: Apply,
      seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type],
      calls: List[Call]
  )(using Context): Apply =
    val ops = if seqExpr.tpe <:< ListClass then "ListOps" else if seqExpr.tpe <:< SeqClass then "SeqOps" else "IterableOps"
    (for elementType <- elementFirstType(seqExpr) yield
      val opsSym    = requiredModule(s"codeoptimizer.$ops")
      val methodSym = opsSym.info.decl(termName(replacement))

      reportOptimization(getClass, s"$method1→$method2 to $ops.$replacement", tree)

      val call1Params = calls.head.callParams
      val call2Params = calls(1).callParams
      val call2Types  = calls(1).callTypes
      Apply(
        TypeApply(
          Select(ref(opsSym), methodSym.name),
          List(TypeTree(elementType), call2Types.head)
        ),
        List(call1Params.head, call2Params.head, seqExpr)
      ).withSpan(tree.span)
    ).getOrElse(tree)
