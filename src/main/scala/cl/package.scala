package object cl {

  /** A CL-Term.
    *
    * Is either an Atom or an Application, denote by $ in this DSL.
    *
    * Mixed-in enrichment classes will be type-classes very soon (hopefully by the end of tomorrow!).
    * The best way to appreciate type-classes is to not use them first and try to re-invent the wheel! Once
    * you have more than a couple mix-ins, you realize why type-classes exists in Haskell and why they make the
    * code much more concise and spotlight-focused!
    */
  sealed trait Term
    extends Show
      with Sizable
      with Scope
      with Occurrence
      with Applicable
      with Substitution

  /** Atoms of the syntax.
    *
    * An atom is either a variable, or an atomic constant.
    */
  sealed trait Atom extends Term

  object Atom {
    def unapply(atom: Atom): Option[Char] = atom match {
      case Var(name) => Some(name)
      case AtomicConstant(name) => Some(name)
    }
  }

  /** A variable, with a lower case letter by convention.
    *
    * @param name Variable name, should be a lower case letter.
    */
  case class Var(name: Char) extends Atom {
    require(name.isLower)
  }


  /** Atomic constants of the syntax.
    *
    * An atomic constant is either a basic combinator or a predefined constant (applied systems).
    * This system is pure, hence the only atomic constants are basic combinators.
    */
  sealed trait AtomicConstant extends Atom

  object AtomicConstant {
    def unapply(constant: AtomicConstant): Option[Char] = constant match {
      case BasicCombinator(name) => Some(name)
    }
  }

  /** Hierarchy of basic combinators, aka the three and only atomic constants of the galaxy.
    *
    * In order of appearance:
    *  - `I` the Identity combinator, might be kicked off soon, as others figured they can survive without I!
    *  - `K` the constant combinator
    *  - `S` the Schönfinkel or strong composition combinator.
    */
  sealed abstract class BasicCombinator extends AtomicConstant

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

  /** CL term denoting an application. Using $ as suggested by the literature (same as in Haskell as well).
    *
    * Note that this is not the same as the function application operator, which is also defined as $ (a la
    * cons operator, ::, in Scala's standard library).
    *
    * @param u Applying term, or the left term.
    * @param v Applied term, ot the right term.
    */
  case class $(u: Term, v: Term) extends Term

  /** A combinator. Needs implementation: should it inherit from Term or just compose over it?
    *
    * @param name Combinator name, should be an upper case letter mindful of its basic comrades!
    */
  case class Combinator(name: Char) {
    require(name.isUpper && name != 'I' && name != 'K' && name != 'S')
  }

}
