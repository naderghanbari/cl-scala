package cl.systems

import cl._

object SKSystem extends CLSystem {

  val name = "SK"

  val K = BasicCombinator('K')
  val S = BasicCombinator('S')

  def basicCombinators: List[BasicCombinator] = List(K, S)

  /** Given a SKI Weak Redex, returns its Associated Contractum.
    *
    * - KXY ▹1w X,
    * - SXYZ ▹1w XZ(YZ).
    *
    * @return Associated Contractum for Weak Redexes, undefined otherwise.
    */
  def associatedContractum: PartialFunction[Term, Term] = {
    case K ^ _X ^ _       ⇒ _X
    case S ^ _X ^ _Y ^ _Z ⇒ _X(_Z) ^ _Y(_Z)
  }

}
