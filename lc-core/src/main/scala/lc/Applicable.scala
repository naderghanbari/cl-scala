package lc

/** Enrichment of Lambda Terms with an application operator and a left-associative
  * infix alias of it, namely `^`.
  */
trait Applicable { self: Term ⇒

  /** Left associative application.
    *
    * Usage:
    * {{{
    *   val (U, V, M) = .... // Some Lambda Terms
    *   U(V)                 // Equivalent to (UV) in the official syntax
    *   V(U)(M)              // VUM
    * }}}
    *
    * @param arg Argument.
    * @return Application of this to arg.
    */
  def apply(arg: Term) = Application(self, arg)

  /** Left-associative higher-precedence alias for application.
    *
    * Usage:
    * {{{
    *   val (U, V, M) = .... // Some Lambda Terms
    *   U ^ V                // Equivalent to (UV) in the official syntax
    *   V ^ U ^ M            // (VU)M
    * }}}
    *
    * @param arg Argument.
    * @return Application of this to arg.
    */
  def ^(arg: Term) = Application(self, arg)

  /** Right-associative higher-precedence alias for application (similar to $ in Haskell).
    *
    * Usage:
    * {{{
    *   val (U, V, M) = .... // Some Lambda Terms
    *   U ^: V               // Equivalent to (UV) in the official syntax
    *   V ^: U ^: M          // V(UM)
    * }}}
    *
    * @param arg Argument.
    * @return Application of this to arg.
    */
  def ^:(arg: Term) = Application(self, arg)

}
