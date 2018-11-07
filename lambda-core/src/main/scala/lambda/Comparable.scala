package lambda

/** Enrichment of Lambda Terms with a syntactical equality
  * relation denoted by `≡`.
  */
trait Comparable { self: Term ⇒

  /** Syntactical identity of terms.
    *
    * Relies on Scala's inherent ADT capabilities (induction on structure equality).
    * The implementation might change to do the recursive structural comparison
    * manually.
    *
    * @param that Term to compare.
    * @return True if two terms are identical.
    */
  def ≡(that: Term): Boolean = this == that

}
