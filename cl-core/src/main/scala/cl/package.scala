package object cl {

  /** CL Term ADT.
    *
    * A Term is either an Atom or an Application, denoted by `^` in this DSL.
    */
  sealed trait Term
      extends Applicable
      with Show
      with Sizable
      with Scope
      with Occurrence
      with Substitution
      with Reduction
      with WeakEquality

  /** Atoms of the syntax.
    *
    * An atom is either a variable, or an atomic constant.
    */
  sealed trait Atom extends Term

  /** Atomic constants of the syntax.
    *
    * An atomic constant is either a basic combinator or a predefined constant (applied systems).
    * This system is pure, hence the only atomic constants are basic combinators.
    */
  sealed trait AtomicConstant extends Atom

  /** Hierarchy of basic combinators, aka the three and only atomic constants of the galaxy.
    *
    * In order of appearance:
    *  - `I` the Identity combinator, might be kicked off soon, as others figured they can survive without I!
    *  - `K` the constant combinator
    *  - `S` the Schönfinkel or strong composition combinator.
    */
  sealed trait BasicCombinator extends AtomicConstant

  /** A variable, with a lower case letter by convention.
    *
    * @param name Variable name, should be a lower case letter.
    */
  case class Var(name: Char) extends Atom { require(name.isLower) }

  /** Application (UV).
    *
    * An alias of this, `^` can be used, especially for infix pattern matching:
    * {{{
    *   M match {
    *     case _U `^` _V ⇒ ...
    *   }
    * }}}
    *
    * @param U Left Term, aka operator.
    * @param V Right Term, aka argument.
    */
  case class Application(U: Term, V: Term) extends Term

  val ^  = Application
  val ^: = Application

  object Atom {
    def unapply(atom: Atom): Option[Char] = atom match {
      case Var(name)            ⇒ Some(name)
      case AtomicConstant(name) ⇒ Some(name)
    }
  }

  object AtomicConstant {
    def unapply(constant: AtomicConstant): Option[Char] = constant match {
      case BasicCombinator(name) ⇒ Some(name)
    }
  }

  case object I extends BasicCombinator
  case object K extends BasicCombinator
  case object S extends BasicCombinator

  object BasicCombinator {
    def unapply(basic: BasicCombinator): Option[Char] = basic match {
      case I ⇒ Some('I')
      case K ⇒ Some('K')
      case S ⇒ Some('S')
    }
  }

}
