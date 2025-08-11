scalaVersion := "3.7.2"

name         := "code-optimizer"
organization := "org.kkougios"

libraryDependencies ++= Seq(
  "org.scala-lang" %% "scala3-compiler" % scalaVersion.value % Provided
)

// Package options
Compile / packageBin / mainClass    := None
Compile / packageBin / artifactName := { (_, _, _) => s"${name.value}.jar" }

// Optional: easier to run `package` to get the jar
Compile / packageBin / target := baseDirectory.value / "target"
