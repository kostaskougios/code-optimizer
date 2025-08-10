package codeoptimizer

import dotty.tools.dotc.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.NameOps.*
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.StdNames.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.plugins.*
import dotty.tools.dotc.plugins.*
import dotty.tools.dotc.transform.*

class OptimizerPlugin extends StandardPlugin:
  override val name: String        = "OptimizerPlugin"
  override val description: String = "Optimizes scala collection operations"

  override def initialize(options: List[String])(using Context): List[PluginPhase] = List(
    new PluginPhase:
      override val phaseName: String      = "seq-optimizer"
      override val runsAfter: Set[String] = Set("typer")

      val optimizers = List(
        SeqFilterForallOptimizer(),
        IterableFilterMapOptimizer()
      )

      override def transformApply(tree: Apply)(using Context): Tree =
        optimizers.foldLeft(tree): (tree, opt) =>
          opt.transformApply(tree)
  )
