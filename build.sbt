import sbtassembly.AssemblyPlugin
import sbtassembly.AssemblyPlugin.autoImport.*

ThisBuild / scalaVersion := "3.7.2"

ThisBuild / name         := "code-optimizer"
ThisBuild / organization := "org.kkougios"
ThisBuild / version      := "0.1.0"

lazy val `compiler-plugin` = project
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang"    %% "scala3-compiler" % scalaVersion.value % Provided,
      "xyz.matthieucourt" %% "layoutz"         % "0.1.0"
    ),
    // Package options
    Compile / packageBin / mainClass    := None,
    Compile / packageBin / artifactName := { (_, _, _) => s"${name.value}.jar" },
    assembly / assemblyMergeStrategy    := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case _                             => MergeStrategy.first
    }
  )
  .enablePlugins(AssemblyPlugin)

lazy val `test-project` = project
  .settings(
    scalacOptions ++= Seq(
      "-Xplugin:/Users/kkougios/projects/code-optimizer/compiler-plugin/target/scala-3.7.2/compiler-plugin-assembly-0.1.0.jar"
    )
  )
