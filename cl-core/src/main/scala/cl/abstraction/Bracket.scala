package cl.abstraction

import cl._

/** Abstraction Bracket over one ore more variables.
  *
  * Semantic building block of the abstraction DSL.
  *
  * Users can alternatively use the symbolic DSL.
  *
  * Usage:
  * {{{
  *   import cl.abstraction._
  *   import Abstraction.Implicits.etaAbstraction  // Or other strategies
  *   val x = ....                                 // Some Var
  *   val M =                                      // Some CL Term
  *   val bracketX = Bracket(x)                    // Same as val abstractionX = Bracket(x)
  *   bracketX.apply(M)                            // Apply the Bracket, i.e. do the actual Abstraction
  * }}}
  *
  * @param x  variable to abstract away.
  * @param xs more variables.
  */
case class Bracket(x: Var, xs: Var*) {

  def apply(M: Term)(implicit abstraction: Abstraction): Term =
    (x +: xs).foldRight(M)(abstraction)

}
