package cl

import cl.generators.CLGen.{ atomicConstantGen, termGen }
import org.scalatest.{ Matchers, WordSpec }
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class ScopeTest extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  "Scope trait" when {

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

      "return free variables: FV method" in {
        x.FV shouldBe Set(x)
        y.FV shouldBe Set(y)
        Kxy.FV shouldBe Set(x, y)
        K_xy.FV shouldBe Set(x, y)
      }

      "respect FV rules wrt application" in
        forAll(termGen, termGen) { (u, v) =>
          (u $ v).FV should contain allElementsOf u.FV
          (u $ v).FV should contain allElementsOf v.FV
        }

      "return empty FV for atomic constants" in
        forAll(atomicConstantGen) { c =>
          c.FV shouldBe empty
        }

    }
  }

}
