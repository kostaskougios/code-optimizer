package codeoptimizer

import dotty.tools.dotc.*
import dotty.tools.dotc.ast.Trees
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Names.Name
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*

// a useful type hierarchy for Tree:
// https://www.scala-lang.org/api/3.x/scala/quoted/Quotes$reflectModule.html

abstract class AbstractOptimizer(using Context):
  def transformApply(tree: Apply)(using Context): Apply

  private val IterableClass = requiredClass("scala.collection.Iterable").typeRef.appliedTo(TypeBounds.empty)

  def scanApply(tree: dotty.tools.dotc.ast.Trees.Tree[Type])(using Context): Option[Rec] =
    val calls     = scanSeqApply(tree)
    val seqStarts = calls.dropWhile(c => !(c.selectOn.tpe <:< IterableClass))
    if seqStarts.isEmpty then None
    else Some(Rec(seqStarts.head.selectOn, seqStarts))

  def scanSeqApply(tree: dotty.tools.dotc.ast.Trees.Tree[Type])(using Context): List[Call] =
    tree match
      case Apply(Select(app, call), callParams)                       => scanSeqApply(app) :+ Call(app, call, callParams, Nil)
      case Apply(TypeApply(Select(app, call), callTypes), callParams) => scanSeqApply(app) :+ Call(app, call, callParams, callTypes)
      case _                                                          => Nil

case class Rec(
    seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type],
    calls: List[Call]
):
  def +(call: Call) = copy(calls = calls :+ call)

case class Call(
    selectOn: Trees.Tree[Type],
    name: Name,
    callParams: List[Trees.Tree[Type]],
    callTypes: List[Trees.Tree[Type]]
)
