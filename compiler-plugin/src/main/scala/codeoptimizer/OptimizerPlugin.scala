package codeoptimizer

import dotty.tools.dotc.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.plugins.*

class OptimizerPlugin extends StandardPlugin:
  override val name: String        = "OptimizerPlugin"
  override val description: String = "Optimizes scala collection operations"

  override def initialize(options: List[String])(using Context): List[PluginPhase] = List(
    new PluginPhase:
      override val phaseName: String      = "seq-optimizer"
      override val runsAfter: Set[String] = Set("typer")

      val optimizers = List(
        StatisticsCollector(),
        SeqFilterForallOptimizer(),
        IterableFilterMapOptimizer()
      )

      override def transformApply(tree: Apply)(using Context): Tree =
        optimizers.foldLeft(tree): (tree, opt) =>
          opt.transformApply(tree)

      override def runOn(units: List[CompilationUnit])(using runCtx: Context): List[CompilationUnit] =
        val r = super.runOn(units)
        println(Statistics.toStatisticsString)
        r
  )
