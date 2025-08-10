// Use Scala 3.x — must match your target Scala version
scalaVersion := "3.7.2"

// Name + organization so we can `publishLocal`
name := "code-optimizer"
organization := "org.kkougios"

// This is a compiler plugin, so we depend on compiler internals
libraryDependencies ++= Seq(
  "org.scala-lang" %% "scala3-compiler" % scalaVersion.value % Provided
)

// Package options
Compile / packageBin / mainClass := None
Compile / packageBin / artifactName := { (_, _, _) => s"${name.value}.jar" }

// Optional: easier to run `package` to get the jar
Compile / packageBin / target := baseDirectory.value / "target"
