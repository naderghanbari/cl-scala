package cl.repl

object Commands {

  sealed trait Command

  case object Quit extends Command
  case object Refresh extends Command
  case object Blank extends Command

  case class Statement(input: String) extends Command

  private val Catalog = Map(
    ":q" -> Quit,
    ":r" -> Refresh,
    ""   -> Blank,
  )

  def classify(s: String): Command = Catalog.getOrElse(s, Statement(s))

}
