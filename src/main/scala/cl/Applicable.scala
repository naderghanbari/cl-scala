package cl

trait Applicable { self: Term =>

  def $(that: Term) = cl.$(self, that)

}
