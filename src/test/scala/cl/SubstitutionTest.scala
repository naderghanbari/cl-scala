package cl

import cl.generators.CLGen.{ applicationGen, termGen, varGen }
import org.scalatest.{ Matchers, WordSpec }
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{ forAll => ∀, _ }

class SubstitutionTest extends WordSpec with Matchers {

  "[U/x]x ≡ U                     ∀ U,x" in
    ∀(termGen, varGen) { (U, x) =>
      (U / x) (x) shouldEqual U
    }

  "[U/x]a ≡ a                     ∀ a ≠ x" in
    ∀(termGen, varGen, varGen) { (U, x, a) =>
      whenever(a != x) {
        (U / x) (a) shouldEqual a
      }
    }

  "[U/x](VW) ≡ ([U/x]V [U/x]W)    ∀ U,V" in
    ∀(termGen, varGen, applicationGen) { case (u, x, v $ w) =>
      (u / x) (v $ w) shouldEqual ((u / x) (v) $ (u / x) (w))
    }

}
