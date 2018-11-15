package cl

trait Substitution { self: Term ⇒

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
  def /(x: Var): Substitution.Rule = Substitution.Rule(self, x)

}

object Substitution {

  /** Substitution rule [U/x]: i.e. rule for substituting U for occurrences of x.
    *
    * @param U Term to substitute for x.
    * @param x Variable to be substituted by Term U.
    */
  case class Rule(U: Term, x: Var) {

    /** Applies this substitution rule to the provided Term, i.e. does the actual substitution.
      *
      * Note that this function could delegate to the more general simultaneous substitution
      * with n = 1, i.e. calling `Simultaneous(this)(y)` but for the sake of remaining loyal to the book
      * the implementation is kept here.
      *
      * @param Y Term to do the substitution in.
      * @return Result of substitution, i.e. [U/x]Y .
      */
    def apply(Y: Term): Term = Y match {
      case `x`     ⇒ U
      case a: Atom ⇒ a
      case v ^ w   ⇒ apply(v) ^ apply(w)
    }

    /** Syntactic sugar for making simultaneous substitution rules.
      * Read it as `comma` which means `at the same time with` when it comes to substitution rules.
      *
      * @param next Next simultaneous rule to chain to this one.
      * @return Simultaneous substitution rule including this and the next.
      */
    def ~(next: Rule): Simultaneous = Simultaneous(this, next)

  }

  /** Simultaneous Substitution rule [U1/x1,...,Un/xn]: i.e. rule for substituting all Un for occurrences of
    * corresponding xn.
    *
    * Usage:
    * {{{
    *     val rule = (U / x) ~ (V / y)    // [U/x,V/y]
    *     rule(Y)                         // Result of substituting wrt to the simultaneous rule in Y.
    *     rule.apply(Y)                   // Alternative syntax.
    * }}}
    *
    * Note: Individual substitution rules must not share variables, so that the simultaneous
    * substitution is unambiguous (mutually distinct x1,...,xn condition).
    *
    * @param rules individual substitution rules mutually distinct in their "variable to substitute".
    */
  case class Simultaneous(rules: Rule*) {

    val rulesMap = rules.groupBy(_.x).mapValues(_.head.U)
    require(rulesMap.keys.size == rules.size, "Simultaneous rules must be mutually distinct in xn")

    /** Applies this simultaneous substitution rule to the provided Term, i.e. does the actual substitution.
      *
      * @param Y Term to do the substitutions in.
      * @return Result of substitutions, i.e. [U1/x1,...,Un/xn]Y .
      */
    def apply(Y: Term): Term = Y match {
      case x: Var if rulesMap.contains(x) ⇒ rulesMap(x)
      case a: Atom                        ⇒ a
      case v ^ w                          ⇒ apply(v) ^ apply(w)
    }

    /** Syntactic sugar to keep adding substitution rules to this simultaneous substitution rule.
      * Preserves the order.
      * Read it as `comma` which means `at the same time with` when it comes to substitution rules.
      *
      * @param next Next simultaneous rule to chain to the current ones.
      * @return Simultaneous substitution rule including all existing rules and next.
      */
    def ~(next: Rule): Simultaneous = Simultaneous(rules :+ next: _*)

  }

}
