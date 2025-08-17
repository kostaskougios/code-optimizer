import sbtassembly.AssemblyPlugin

val MyScalaVersion        = "3.7.2"
val CompilerPluginVersion = "0.1.0"

ThisBuild / scalaVersion := MyScalaVersion

ThisBuild / name         := "code-optimizer"
ThisBuild / organization := "org.kkougios"
ThisBuild / version      := CompilerPluginVersion

val ScalaTest = "org.scalatest"     %% "scalatest" % "3.2.19" % Test
val Layoutz   = "xyz.matthieucourt" %% "layoutz"   % "0.1.0"

lazy val `code-optimizer-compiler-plugin` = project
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang" %% "scala3-compiler" % scalaVersion.value % Provided,
      Layoutz
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

lazy val enableCompilerPlugin = settingKey[Boolean]("Enable the compiler plugin")

lazy val `code-optimizer-lib` = project
  .settings(
    libraryDependencies ++= Seq(
      ScalaTest
    )
  )

lazy val `test-project` = project
  .settings(
    libraryDependencies ++= Seq(
      Layoutz,
      ScalaTest
    ),
    enableCompilerPlugin := sys.props.get("enablePlugin").contains("true"),
    scalacOptions ++= {
      if (enableCompilerPlugin.value)
        Seq(
          s"-Xplugin:code-optimizer-compiler-plugin/target/scala-$MyScalaVersion/code-optimizer-compiler-plugin-assembly-$CompilerPluginVersion.jar"
        )
      else Nil
    },
    run / fork           := true
  )
  .dependsOn(`code-optimizer-lib`)
