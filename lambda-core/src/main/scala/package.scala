package object lambda {

  /** Lambda Term ADT, either
    * - an Atom,
    * - an Application,
    * - or an Abstraction.
    */
  sealed trait Term

  /** Atoms, either
    *   - a Variable,
    *   - or an Atomic Constant.
    */
  sealed trait Atom extends Term

  /** Atomic constant.
    *
    * It's not sealed so that it can be extended for applied systems.
    */
  trait AtomicConstant extends Atom {
    def name: Char
  }

  /** A Variable, with a lower case letter by convention.
    *
    * @param name Variable name, should be a lower case letter.
    */
  case class Var(name: Char) extends Atom { require(name.isLower) }

  /** Application (MN).
    *
    * An alias of this, $ can be used, especially for infix pattern matching:
    * {{{
    *   term match {
    *     case _M $ _N ⇒ ...
    *   }
    * }}}
    *
    * @param M Left Term, aka operator.
    * @param N Right Term, aka argument.
    */
  case class Application(M: Term, N: Term) extends Term

  val $ = Application

  /** Abstraction (λx.M).
    *
    * An alias of this, λ can be used in one of the following ways:
    * {{{
    *   λ(x) { M }
    *   λ(x)(M)
    * }}}
    *
    * @param x Binding variable.
    * @param M Body.
    */
  case class Abstraction(x: Var, M: Term) extends Term

  val λ = Abstraction.curried

  object Atom {
    def unapply(atom: Atom): Option[Char] = atom match {
      case Var(name)            ⇒ Some(name)
      case AtomicConstant(name) ⇒ Some(name)
    }
  }

  object AtomicConstant {
    def unapply(constant: AtomicConstant): Option[Char] = Some(constant.name)
  }

}
