package cl

import org.scalatest.{ Matchers, WordSpec }

class OccurrenceTest extends WordSpec with Matchers {

  "Occurrence trait" when {

    "given some Terms" should {

      val (x, y, z) = (Var('x'), Var('y'), Var('z'))
      val Kx = K $ x
      val Kxy = Kx $ y
      val K_xy = K $ (x $ y)

      "define the 'occurs in' partial order relation" in {
        x ⊆ Kxy shouldBe true
        z ⊆ Kxy shouldBe false
        Kx ⊆ Kxy shouldBe true
        Kx ⊆ K_xy shouldBe false
        val subTerm = (S $ (K $ x)) $ (I $ K)
        val superTerm = ((S $ ((S $ I) $ ((S $ (K $ x)) $ (I $ K)))) $ (K $ x)) $ (S $ (K $ I))
        subTerm ⊆ superTerm shouldBe true
      }

      "return their FV set and define the isClosed attribute" in {
        x.FV shouldBe Set(x)
        y.FV shouldBe Set(y)
        Kxy.FV shouldBe Set(x, y)
        K_xy.FV shouldBe Set(x, y)
        K_xy.isClosed shouldBe false
      }

    }

  }

}
