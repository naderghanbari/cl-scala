package cl

import org.scalatest.{ Matchers, WordSpec }

class ContainerTest extends WordSpec with Matchers {

  "Container trait" when {

    "given some CL terms" should {

      val x = Var('x')
      val y = Var('y')
      val z = Var('z')
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

    }
  }

}
