package object cl {

  /** CL Term ADT.
    *
    * A Term is either an Atom or an Application, denote by $ in this DSL.
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

  /** CL term denoting an application. Using $ as suggested by the literature (same as in Haskell as well).
    *
    * Note that this is not the same as the function application operator, which is also defined as $ (a la
    * cons operator, ::, in Scala's standard library).
    *
    * @param u Applying term, or the left term.
    * @param v Applied term, ot the right term.
    */
  case class $(u: Term, v: Term) extends Term

  object Atom {
    def unapply(atom: Atom): Option[Char] = atom match {
      case Var(name)            => Some(name)
      case AtomicConstant(name) => Some(name)
    }
  }

  object AtomicConstant {
    def unapply(constant: AtomicConstant): Option[Char] = constant match {
      case BasicCombinator(name) => Some(name)
    }
  }

  case object I extends BasicCombinator
  case object K extends BasicCombinator
  case object S extends BasicCombinator

  object BasicCombinator {
    def unapply(basic: BasicCombinator): Option[Char] = basic match {
      case I => Some('I')
      case K => Some('K')
      case S => Some('S')
    }
  }

}
