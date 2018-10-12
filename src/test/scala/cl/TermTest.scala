package cl

import org.scalatest.{ Matchers, WordSpec }

class TermTest extends WordSpec with Matchers {

  "Term" when {

    "given some variables and basic combinators" should {

      val x = Var('x')
      val y = Var('y')
      val z = Var('z')

      "be definable" in {

        val Kx = K $ x
        val Kxy = Kx $ y
        val K_xy = K $ (x $ y)
        val SKxy = S $ K $ x $ y
        val S_Kx__SK_K = (S $ (K $ x)) $ ((S $ K) $ K)
        val S__SI_K___Kx____S_KI = ((S $ ((S $ I) $ K)) $ (K $ x)) $ (S $ (K $ I))

        x ⊆ Kxy shouldBe true
        z ⊆ Kxy shouldBe false
        Kx ⊆ Kxy shouldBe true
        Kx ⊆ K_xy shouldBe false

        x.length shouldBe 1
        Kx.length shouldBe 2
        K_xy.length shouldBe 3
      }

    }
  }

}
