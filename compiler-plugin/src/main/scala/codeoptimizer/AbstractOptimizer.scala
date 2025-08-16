package codeoptimizer

import dotty.tools.dotc.*
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

  case class Rec(seqExpr: dotty.tools.dotc.ast.Trees.Tree[Type], calls: List[Name]):
    def +(n: Name) = copy(calls = calls :+ n)

  def scanApply(tree: dotty.tools.dotc.ast.Trees.Tree[Type])(using Context): Option[Rec] =
    tree match
      case Apply(
            Select(seqExpr, call1),
            callParams
          ) if seqExpr.tpe <:< IterableClass =>
        Some(Rec(seqExpr, call1 :: Nil))
      case Apply(
            Select(
              app,
              call
            ),
            callParams
          ) =>
        scanApply(app) match
          case Some(rec) => Some(rec + call)
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
          case Some(rec) => Some(rec + call)
          case None      => None
      case _ => None
