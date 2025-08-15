package codeoptimizer

import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Contexts.*

trait AbstractOptimizer:
  def transformApply(tree: Apply)(using Context): Apply

// a useful type hierarchy for Tree:
// https://www.scala-lang.org/api/3.x/scala/quoted/Quotes$reflectModule.html
