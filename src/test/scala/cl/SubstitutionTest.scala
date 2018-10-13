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

  "[(SK)/x,(KI)/y](yxx) ≡ KI(SK)(SK)" in {
    val (x, y) = (Var('x'), Var('y'))
    val SK = S $ K
    val KI = K $ I
    ((SK / x) ~ (KI / y)) apply (y $ x $ x) shouldEqual (KI $ SK $ SK)
  }

  "[(SK)/x,(KI)/y, (Kx)/z](Kz(yx)x) ≡ K(Kx)(SK(KI))(KI)" in {
    val (x, y, z) = (Var('x'), Var('y'), Var('z'))
    val SK = S $ K
    val KI = K $ I
    val Kx = K $ x
    ((KI / x) ~ (SK / y) ~ (Kx / z)) apply (K $ z $ (y $ x) $ x) shouldEqual (K $ Kx $ (SK $ KI) $ KI)
  }

  "[(SK)/x,(KI)/x]Y should be rejected" in {
    val (x, y) = (Var('x'), Var('y'))
    val SK = S $ K
    val KI = K $ I
    val caught = intercept[IllegalArgumentException] {
      (KI / x) ~ (SK / x)
    }
    caught.getMessage contains "mutually distinct" shouldBe true
  }

}
