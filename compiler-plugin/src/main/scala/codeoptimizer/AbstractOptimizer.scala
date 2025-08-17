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

trait AbstractOptimizer(using Context):
  def transformApply(tree: Apply)(using Context): Apply
