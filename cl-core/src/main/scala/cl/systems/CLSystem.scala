package cl.systems

import cl.{BasicCombinator, Term}

/** A CL system.
  *
  * Consists of all basic combinators of this system along with their associated contractum.
  */
trait CLSystem {

  def name: String

  def basicCombinators: List[BasicCombinator]

  /** Given a Weak Redex of this system, returns its Associated Contractum.
    *
    * @return Associated Contractum for Weak Redexes, undefined otherwise.
    */
  def associatedContractum: PartialFunction[Term, Term]

}

object CLSystem {

  object Implicits {

    implicit def SK: CLSystem = SKSystem

    implicit def SKI: CLSystem = SKISystem

  }

}
