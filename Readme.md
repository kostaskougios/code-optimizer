run
`sbt assembly`

then add the generated jar as a scalac plugin i.e.

```
ThisBuild / scalacOptions ++= Seq(
  "-Xplugin:/Users/**USER**/projects/code-optimizer/target/scala-3.7.2/code-optimizer-assembly-0.1.0.jar"
)
```
