package cl

trait Applicable { self: Term =>

  /** Syntactical sugar for syntactical Application (i.e. Application in the CL syntax)
    * of this term to the provided term.
    *
    * Usage:
    * {{{
    *   val U = .... // Term
    *   val V = .... // Term
    *   U $ V        // (UV)
    *   V $ U        // (VU)
    * }}}
    *
    * @param that Right hand side term, aka the Argument.
    * @return Application of this to that.
    */
  def $(that: Term) = cl.$(self, that)

}
