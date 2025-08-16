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
    tree match
      case Apply(
            Select(seqExpr, call1),
            callParams
          ) if seqExpr.tpe <:< IterableClass =>
        Some(Rec(seqExpr, Call(call1, callParams, Nil) :: Nil))
      case Apply(
            TypeApply(
              Select(seqExpr, call1),
              callTypes
            ),
            callParams
          ) if seqExpr.tpe <:< IterableClass =>
        println(seqExpr.show)
        Some(Rec(seqExpr, Call(call1, callParams, callTypes) :: Nil))
      case Apply(
            Select(
              app,
              call
            ),
            callParams
          ) =>
        scanApply(app) match
          case Some(rec) => Some(rec + Call(call, callParams, Nil))
          case None      => None
      case Apply(
            TypeApply(
              Select(
                app,
                call
              ),
              callTypes
            ),
            callParams
          ) =>
        scanApply(app) match
          case Some(rec) => Some(rec + Call(call, callParams, callTypes))
          case None      => None
      case _ => None

case class Rec(
    seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type],
    calls: List[Call]
):
  def +(call: Call) = copy(calls = calls :+ call)

case class Call(
    name: Name,
    callParams: List[Trees.Tree[Type]],
    callTypes: List[Trees.Tree[Type]]
)
