package codeoptimizer

import dotty.tools.dotc.ast.*
import dotty.tools.dotc.ast.Trees.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Types.*

object Utils:
  def elementFirstType(e: Tree[Type])(using Context): Option[Type] = e.tpe.widen match
    case AppliedType(seqType, typeArgs) => Some(typeArgs.head)
    case _                              => None

  def reportOptimization(clazz: Class[?], optimization: String, tree: Tree[?])(using ctx: Context): Unit =
    val pos        = tree.span
    val sourceFile = ctx.source.file.name
    val lineNumber = ctx.source.offsetToLine(pos.start) + 1
    println(s"[${clazz.getSimpleName}] Optimizing $optimization at $sourceFile:$lineNumber")

  def reportMsg(clazz: Class[?], message: String, tree: Tree[?])(using ctx: Context): Unit =
    val pos        = tree.span
    val sourceFile = ctx.source.file.name
    val lineNumber = ctx.source.offsetToLine(pos.start) + 1
    println(s"[${clazz.getSimpleName}] $message at $sourceFile:$lineNumber")
