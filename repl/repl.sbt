name := "cl-scala-repl"

val CompileDependencies = Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % Versions.ParserCombinators
)

libraryDependencies ++= CompileDependencies
