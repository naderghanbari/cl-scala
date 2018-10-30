package cl

import cl.generators.CLGen.varGen
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll ⇒ ∀, _}
import org.scalatest.{Matchers, WordSpec}

class WeakEqualityTest extends WordSpec with Matchers {

  "Exercise 2.30: BWBIx =w SIIx" in ∀(varGen) { x ⇒
    val W     = S(S) $ K(I)
    val B     = S $ K(S) $ K
    val left  = B(W)(B)(I)(x)
    val right = S(I)(I)(x)
    (left weakEquals right) shouldBe true
  }

  "D ≡ [x,y,z].zxy and D1 ≡ [u].u([x, y]x)" should {

    import cl.abstraction.{|, Abstraction}
    import Abstraction.Implicits.eta

    val (x1, x2, z) = (Var('x'), Var('y'), Var('z'))
    val D           = |(x1, x2, z) | (z $ x1 $ x2)
    val D1          = S(I) $ K(K)
    val D2          = S(I) $ (K $ K(I))

    "Exercise 2.34.1 - η: D1(Dxy) ▹w x and D1(Dxy) ▹w x   ∀ x, y" in ∀(varGen, varGen) { (x, y) ⇒
      (D1 $ D(x)(y) weakEquals x) shouldBe true
      (D2 $ D(x)(y) weakEquals y) shouldBe true
    }
  }

}
