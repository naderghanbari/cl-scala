package lambda

/** Enrichment of Lambda Terms with an application operator and an
  * infix alias of it, namely `$`.
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

  /** Alias for application (a la Haskell and FP literature).
    * Due to Scala's spec, has higher precedence than the normal `apply` method and
    * can be used to break an otherwise left-associative chain of applications.
    *
    * Usage:
    * {{{
    *   val (U, V, M) = .... // Some Lambda Terms
    *   U $ V                // Equivalent to (UV) in the official syntax
    *   V $ U(M)             // V(UM)
    * }}}
    *
    * @param arg Argument.
    * @return Application of this to arg.
    */
  def $(arg: Term) = Application(self, arg)

}
