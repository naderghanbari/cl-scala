package cl

import cl.generators.CLGen.{ atomicConstantGen, closedTermGen, termGen }
import org.scalatest.{ Matchers, WordSpec }
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class ScopeTest extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  "Scope trait" when {

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

    "given arbitrary Terms" should {

      "respect FV rules wrt Application" in forAll(termGen, termGen) { (u: Term, v) =>
        (u $ v).FV should contain allElementsOf u.FV
        (u $ v).FV should contain allElementsOf v.FV
      }

      "return empty FV for Atomic Constants" in forAll(atomicConstantGen) { c =>
        c.FV shouldBe empty
        c.isClosed shouldBe true
      }

      "return empty FV for Closed Terms" in forAll(closedTermGen) { U =>
        U.FV shouldBe empty
        U.isClosed shouldBe true
      }
    }
  }

}
