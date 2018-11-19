package cl

import CLGen.{applicationGen, termGen, varGen}
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll ⇒ ∀, _}
import org.scalatest.{Matchers, WordSpec}

class SubstitutionTest extends WordSpec with Matchers {

  import cl.systems.ski.SKI.{I, K, S}

  val SK = S(K)
  val KI = K(I)

  "[U/x]x ≡ U                     ∀ U,x" in ∀(termGen, varGen) { (U, x) ⇒ (U / x)(x) shouldEqual U
  }

  "[U/x]a ≡ a                     ∀ a ≠ x" in ∀(termGen, varGen, varGen) { (U, x, a) ⇒
    whenever(x != a) {
      (U / x)(a) shouldEqual a
    }
  }

  "[U/x](VW) ≡ ([U/x]V [U/x]W)    ∀ U,V" in ∀(termGen, varGen, applicationGen) {
    case (u, x, v ^ w) ⇒
      (u / x)(v ^ w) shouldEqual ((u / x)(v) ^ (u / x)(w))
  }

  "[(SK)/x,(KI)/y](yxx) ≡ KI(SK)(SK)" in ∀(varGen, varGen) { (x, y) ⇒
    whenever(x != y) {
      ((SK / x) ~ (KI / y)) apply y(x)(x) shouldEqual (KI ^ SK ^ SK)
    }
  }

  "[(SK)/x,(KI)/y, (Kx)/z](Kz(yx)x) ≡ K(Kx)(SK(KI))(KI)" in ∀(varGen, varGen, varGen) { (x, y, z) ⇒
    whenever(x != y && x != z && y != z) {
      ((KI / x) ~ (SK / y) ~ (K(x) / z)) apply (K ^ z ^ y(x) ^ x) shouldEqual (K ^ K(x) ^ SK(KI) ^ KI)
    }
  }

  "[(SK)/x,(KI)/x]Y should be rejected" in ∀(varGen, varGen) { (x, y) ⇒
    whenever(x != y) {
      val caught = intercept[IllegalArgumentException] {
        (KI / x) ~ (SK / x)
      }
      caught.getMessage contains "mutually distinct" shouldBe true
    }
  }

}
