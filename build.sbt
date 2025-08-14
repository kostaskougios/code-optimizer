import sbtassembly.AssemblyPlugin

val MyScalaVersion        = "3.7.2"
val CompilerPluginVersion = "0.1.0"

ThisBuild / scalaVersion := MyScalaVersion

ThisBuild / name         := "code-optimizer"
ThisBuild / organization := "org.kkougios"
ThisBuild / version      := CompilerPluginVersion

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
    libraryDependencies ++= Seq(
      "xyz.matthieucourt" %% "layoutz" % "0.1.0"
    ),
    scalacOptions ++= Seq(
      s"-Xplugin:compiler-plugin/target/scala-$MyScalaVersion/compiler-plugin-assembly-$CompilerPluginVersion.jar"
    )
  )
