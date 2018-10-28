name := "cl-scala"

ThisBuild / scalaVersion := Versions.Scala

val core = project

val repl = project.dependsOn(core % "compile;test->test")
