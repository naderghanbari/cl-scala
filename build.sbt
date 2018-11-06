name := "cl-scala"

ThisBuild / scalaVersion := Versions.Scala

val `cl-core` = project

val `cl-lang` = project
  .dependsOn(`cl-core` % "test->test")

val `cl-eval` = project
  .dependsOn(`cl-core`, `cl-lang`)
  .dependsOn(`cl-core` % "test->test", `cl-lang` % "test->test")

val `cl-repl` = project
  .dependsOn(`cl-eval`)
  .dependsOn(`cl-eval` % "test->test")

Global / fork                              := true
Global / cancelable                        := true
`cl-repl` / Compile / run / connectInput   := true
`cl-repl` / Compile / run / outputStrategy := Some(StdoutOutput)
`cl-repl` / Compile / run / mainClass      := Some("cl.repl.Repl")
run in Compile                             := (`cl-repl` / Compile / run).evaluated
