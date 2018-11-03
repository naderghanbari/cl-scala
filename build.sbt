name := "cl-scala"

ThisBuild / scalaVersion := Versions.Scala

val core = project

val lang = project
  .dependsOn(core % "test->test")

val eval = project
  .dependsOn(core, lang)
  .dependsOn(core % "test->test", lang % "test->test")

val repl = project
  .dependsOn(core, lang)
  .dependsOn(lang % "test->test")
