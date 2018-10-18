package cl.abstraction

import cl._

/** Abstraction Bracket over variable x. Semantic building block of the abstraction DSL.
  *
  * Users can use the DSL without worrying about this class. See Abstraction for more details.
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
  * @param x variable to abstract away.
  */
case class Bracket(x: Var) extends AnyVal {

  def apply(M: Term)(implicit abstraction: Abstraction): Term = abstraction(x, M)

}
