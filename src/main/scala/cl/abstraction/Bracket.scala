package cl.abstraction

import cl._

/** Abstraction Bracket over variable x. Semantic building block of the abstraction DSL.
  *
  * Users can use the DSL without worrying about this class. See Abstraction for more details.
  *
  * Usage:
  * {{{
  *   import cl.abstraction.Bracket                   // Direct (non-DSL) use of Bracket
  *   import cl.abstraction.Implicits.EtaAbstraction  // Or other strategies
  *   val x = ....                                    // Some Var
  *   val M =                                         // Some CL Term
  *   val bracketX = Bracket(x)                       // Same as val abstractionX = Bracket(x)
  *   bracketX.apply(M)                               // Apply the Bracket, i.e. do the actual Abstraction
  * }}}
  *
  * @param x variable to abstract away.
  */
case class Bracket(x: Var) {

  /** Abstracts by delegating to the provided implicit Abstraction strategy.
    *
    * @param M           Term to abstract x away from.
    * @param abstraction Abstraction strategy.
    * @return Abstracted term.
    */
  def apply(M: Term)(implicit abstraction: Abstraction) = abstraction(x, M)

}
