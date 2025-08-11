import sbtassembly.AssemblyPlugin
import sbtassembly.AssemblyPlugin.autoImport.*

scalaVersion := "3.7.2"

name         := "code-optimizer"
organization := "org.kkougios"
version      := "0.1.0"

libraryDependencies ++= Seq(
  "org.scala-lang"    %% "scala3-compiler" % scalaVersion.value % Provided,
  "xyz.matthieucourt" %% "layoutz"         % "0.1.0"
)

// Package options
Compile / packageBin / mainClass    := None
Compile / packageBin / artifactName := { (_, _, _) => s"${name.value}.jar" }

// Optional: easier to run `package` to get the jar
Compile / packageBin / target := baseDirectory.value / "target"

lazy val root = (project in file("."))
  .enablePlugins(AssemblyPlugin)

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case _                             => MergeStrategy.first
}
