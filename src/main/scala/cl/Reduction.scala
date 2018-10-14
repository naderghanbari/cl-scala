package cl

import cl.Reduction._

trait Reduction { self: Term =>

  /** Left to right (incomplete) Weak Contraction relation.
    *
    * TODO: Implement the Tree, or Positioning for Sub-Terms so that all possible Contractions are taken
    * into account, not just the one with the leftmost Weak Redex being Contracted.
    *
    * Returns true if U ▹1w V where U is this Term (self).
    *
    * @param V Subject Term.
    * @return True if this Term Weakly Contracts to the Subject Term.
    */
  def ▹|(V: Term): Boolean = contractLeftMost(self).contains(V)

  /** "Weakly Reduces to" relation.
    *
    * TODO: Incomplete: just accepts the Weak Normal form and not the intermediate results of  ▹1w.
    *
    * Returns true if U ▹w V where U is this Term (self).
    *
    * @param V Subject Term.
    * @return True if this Term Weakly Reduces to the Subject Term.
    */
  def ▹(V: Term): Boolean = reduceToWeakNormalForm(self) == V

}

object Reduction {

  /** Given a Weak Redex, returns its Associated Contractum (in a pure system).
    * This is a partial function, so it is undefined for Terms that are not weak Redexes.
    *
    * * (I) IX ▹1w X,
    * * (K) KXY ▹1w X,
    * * (S) SXYZ ▹1w XZ(YZ).
    *
    * @return Associated Contractum of the provided Term.
    */
  val associatedContractum: PartialFunction[Term, Term] = {
    case I $ _X => _X
    case K $ _X $ _ => _X
    case S $ _X $ _Y $ _Z => _X $ _Z $ (_Y $ _Z)
  }

  /** Given a Term determines if it is a Weak Redex or not.
    *
    * @return True if the provided Term is a Weak Redex, false otherwise.
    */
  val isWeakRedex: Term => Boolean = associatedContractum.isDefinedAt

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
  def contractLeftMost(U: Term): Option[Term] = contractPartial.lift(U)

  private lazy val contractPartial: PartialFunction[Term, Term] =
    associatedContractum
      .orElse {
        case _W $ _X if contractPartial.isDefinedAt(_W) => contractPartial(_W) $ _X
        case _W $ _X if contractPartial.isDefinedAt(_X) => _W $ contractPartial(_X)
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
  def reduceToWeakNormalForm(U: Term): Term = {
    def recurse(T: Term): Option[Term] = contractLeftMost(T).flatMap(recurse).orElse(Some(T))
    recurse(U).getOrElse(U)
  }

}
