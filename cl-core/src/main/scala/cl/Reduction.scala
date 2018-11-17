package cl

import cl.Reduction._
import cl.systems.CLSystem

trait Reduction { self: Term ⇒

  /** Determines if this Term is a Weak Redex or not.
    *
    * Does memoize this attribute: much better time complexity for the price of a bit more space (a Boolean and a flag).
    */
  def isWeakRedex(implicit system: CLSystem): Boolean = system.associatedContractum isDefinedAt self

  /** Determines if this Term is a Weak Normal Form or not.
    *
    * Does memoize this attribute so that the Weak Reduction can rely on it for faster performance.
    */
  def isWeakNormalForm(implicit system: CLSystem): Boolean = !(contractPartial isDefinedAt self)

}

object Reduction {

  private[Reduction] def contractPartial(implicit system: CLSystem): PartialFunction[Term, Term] =
    system.associatedContractum
      .orElse {
        case _W ^ _X if !_W.isWeakNormalForm ⇒ contractPartial(system)(_W) ^ _X
        case _W ^ _X if !_X.isWeakNormalForm ⇒ _W ^ contractPartial(system)(_X)
      }

  /** Reduces a Term to its Weak Normal Form.
    *
    * NOTE: If the Term has no Weak Normal Form, that probably means an Stack overflow happens!.
    * Use of the word "probably" in the above sentence is just due to my unawareness of a theorem around this,
    * I'll fix this as I move forward, probably with removing the probably as this clearly has a relation to
    *
    * @param U Term to reduce to Weak Normal Form.
    * @return Weak Normal form of the input Term, or "who knows what" if that Term has none!
    */
  def reduceToWeakNormalForm(U: Term)(implicit system: CLSystem): Term = {
    def recurse(T: Term): Option[Term] = contractLeftMost(T).flatMap(recurse).orElse(Some(T))
    recurse(U).getOrElse(U)
  }

  /** Contracts the provided Term a Weak Contraction, i.e. replacing left-most occurrence of a Weak Redex by its
    * Associate Contractum.
    *
    * Has left-precedence, i.e. leftmost Weak Redex Contracts first.
    *
    * If there is no occurrence of Weak Redexes, None is returned.
    *
    * @param U Term to Weakly Contract.
    * @return Changed Term, i.e. Term with a replacement rule having been applied, None otherwise.
    */
  def contractLeftMost(U: Term)(implicit system: CLSystem): Option[Term] = contractPartial.lift(U)

}
