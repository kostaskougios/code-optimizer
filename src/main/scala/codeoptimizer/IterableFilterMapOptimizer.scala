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
  lazy val IterableClass = requiredClass("scala.collection.Iterable").typeRef.appliedTo(TypeBounds.empty)
  override protected def seqExprTypeCheck(seqExpr: Tree)(using Context): Boolean =
    println(seqExpr.tpe.show)
    seqExpr.tpe <:< IterableClass
  override protected def firstCall                                               = "filter"
  override protected def secondCall                                              = "map"
  override protected def implClass: String                                       = "IterableOps"
  override protected def implMethod: String                                      = "filterMap"
