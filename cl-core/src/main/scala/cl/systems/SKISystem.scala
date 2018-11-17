package cl.systems

import cl._

object SKISystem extends CLSystem {

  val name = "SKI"

  val I = BasicCombinator('I')
  val K = BasicCombinator('K')
  val S = BasicCombinator('S')

  def basicCombinators: List[BasicCombinator] = List(I, K, S)

  /** Given a SKI Weak Redex, returns its Associated Contractum.
    *
    * - IX ▹1w X,
    * - KXY ▹1w X,
    * - SXYZ ▹1w XZ(YZ).
    *
    * @return Associated Contractum for Weak Redexes, undefined otherwise.
    */
  def associatedContractum: PartialFunction[Term, Term] = {
    case I ^ _X           ⇒ _X
    case K ^ _X ^ _       ⇒ _X
    case S ^ _X ^ _Y ^ _Z ⇒ _X(_Z) ^ _Y(_Z)
  }

}
