name := "cl-scala"

ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.12.7"

lazy val core =
  project
    .settings(name := "cl-scala-core")
    .settings(
      libraryDependencies ++= Seq(
        "org.scalatest"  %% "scalatest"  % "3.0.5"  % Test,
        "org.scalacheck" %% "scalacheck" % "1.14.0" % Test
      )
    )

lazy val repl =
  project
    .settings(name := "cl-scala-repl")
    .dependsOn(core % "compile; test->test")
    .aggregate(core)
    .settings(
      libraryDependencies ++= Seq(
        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1"
      )
    )
