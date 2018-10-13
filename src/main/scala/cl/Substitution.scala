package cl

trait Substitution { self: Term =>

  /** Substitution rule [U/x]: i.e. rule for substituting U for occurrences of x.
    *
    * @param U Term to substitute for x.
    * @param x Variable to be substituted by Term U.
    */
  case class /(U: Term, x: Var) {

    /** Applies this substitution rule to the provided Term, i.e. does the actual substitution.
      *
      * @param Y Term to do the substitution in.
      * @return Result of substitution, i.e. [U/x]Y .
      */
    def apply(Y: Term): Term = Y match {
      case `x` => U
      case a: Atom => a
      case v $ w => apply(v) $ apply(w)
    }

  }

  /** Syntactic sugar to define/do a substitution.
    *
    * Usage:
    * {{{
    *   val x = ....          // Variable
    *   val U = ....          // Term
    *   val Y =               // Term
    *   (U / x)(Y)            // [U/x]Y, i.e. result of substituting U for x in Y
    *   val rule = (U / x)    // Define the rule to be applied later, potentially to multiple terms
    *   rule(Y)               // Result of substituting wrt to the rule in Y
    *   rule.apply(Y)         // Alternative syntax.
    * }}}
    *
    * @param x variable to be substituted by this (self) term.
    * @return Substitution rule.
    */
  def /(x: Var): / = /(self, x)

}
