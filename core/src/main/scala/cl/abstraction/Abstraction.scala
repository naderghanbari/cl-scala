package cl.abstraction

import cl._

/** Abstraction algorithm.
  *
  * Given a variable x and a term M, returns [x].M
  */
trait Abstraction extends ((Var, Term) => Term) {

  /** Applies the abstraction.
    *
    * @param x Variable to abstract away.
    * @param M Term
    * @return [x].M
    */
  override def apply(x: Var, M: Term): Term

}

object Abstraction {

  object Implicits {

    implicit def primitive: Abstraction = PrimitiveAbstraction

    implicit def weak: Abstraction = WeakAbstraction

    implicit def eta: Abstraction = EtaAbstraction

  }

}

/** Syntactic sugar for [x,y,...].M but square brackets are reserved so | is used.
  *
  * Usage:
  * {{{
  *   import cl.abstraction._                         // Abstraction DSL entry point
  *   import Abstractions.Implicits.etaAbstraction    // Or other strategies
  *   val x = ....                                    // Some Var
  *   val M =                                         // Some CL Term
  *   |(x, y)| M                                      // [x,y].M
  *   val bracketXY = |(x, y)|                        // Same as val abstractionXY = Bracket(x, y)
  *   bracketXY.apply(M)                              // Apply the Bracket, i.e. do the actual Abstraction
  * }}}
  *
  * @param x  variable to abstract away.
  * @param xs more variable to abstract away.
  */
case class |(x: Var, xs: Var*) {

  def | : Bracket = Bracket(x, xs: _*)

}
