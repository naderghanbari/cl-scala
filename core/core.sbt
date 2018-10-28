name := "cl-scala-core"

val TestDependencies = Seq(
  "org.scalatest"  %% "scalatest"  % Versions.ScalaTest,
  "org.scalacheck" %% "scalacheck" % Versions.ScalaCheck
)

libraryDependencies ++= TestDependencies.map(_ % Test)
