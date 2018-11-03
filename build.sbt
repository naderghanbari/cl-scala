name := "cl-scala"

ThisBuild / scalaVersion := Versions.Scala

val core = project

val lang = project
  .dependsOn(core % "test->test")

val eval = project
  .dependsOn(core, lang)
  .dependsOn(core % "test->test", lang % "test->test")

val repl = project
  .dependsOn(eval)
  .dependsOn(eval % "test->test")

Global / fork                         := true
Global / cancelable                   := true
repl / Compile / run / connectInput   := true
repl / Compile / run / outputStrategy := Some(StdoutOutput)
repl / Compile / run / mainClass      := Some("cl.repl.Repl")
run in Compile                        := (repl / Compile / run).evaluated
