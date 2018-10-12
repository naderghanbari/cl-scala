package cl

trait Container { self: Term =>

  final def ⊇(that: Term) = that ⊆ self

  final def ⊆(that: Term): Boolean =
    if (that.length < self.length) false
    else that match {
      case `self` => true
      case u $ _ if self ⊆ u => true
      case _ $ v if self ⊆ v => true
      case _ => false
    }

}
