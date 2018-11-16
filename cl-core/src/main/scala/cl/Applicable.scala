package cl

/** Enrichment of CL Terms with an application operator and an
  * infix left-associative alias of it, namely `^`.
  */
trait Applicable { self: Term ⇒

  /** Left associative application.
    *
    * Usage:
    * {{{
    *   val (U, V, M) = .... // Some CL Terms
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
    *   val (U, V, M) = .... // Some CL Terms
    *   U ^ V                // Equivalent to (UV) in the official syntax
    *   V ^ U(M)             // V(UM)
    * }}}
    *
    * @param arg Argument.
    * @return Application of this to arg.
    */
  def ^(arg: Term) = Application(self, arg)

  /** Right-associative higher-precedence alias for application, similar to `$` in Haskell.
    *
    * Usage:
    * {{{
    *   val (U, V, M) = .... // Some CL Terms
    *   U ^: V ^: M          // U(V(M))
    * }}}
    *
    * @param arg Argument.
    * @return Application of this to arg.
    */
  def ^:(arg: Term) = Application(self, arg)

}
