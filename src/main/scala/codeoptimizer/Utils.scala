package codeoptimizer

import dotty.tools.dotc.ast.*
import dotty.tools.dotc.ast.Trees.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Types.*

object Utils:
  def elementFirstType(e: Tree[Type])(using Context) = e.tpe.widen match
    case AppliedType(seqType, typeArgs) => typeArgs.head
