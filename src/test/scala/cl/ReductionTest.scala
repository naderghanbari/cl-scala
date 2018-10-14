package cl

import cl.Reduction.{ contractLeftMost, reduceToWeakNormalForm }
import cl.generators.CLGen.{ termGen, varGen, weakRedexGen }
import org.scalatest.{ Matchers, WordSpec }
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{ forAll => ∀, _ }

class ReductionTest extends WordSpec with Matchers {

  "Weak Redexes are known and respected" in
    ∀(weakRedexGen) { R =>
      R.isWeakRedex shouldEqual true
    }

  "Non-Weak Redexes are known and frowned upon" in
    ∀(termGen, termGen, termGen) { (U, V, W) =>
      I.isWeakRedex shouldEqual false
      (K $ U).isWeakRedex shouldEqual false
      (S $ U).isWeakRedex shouldEqual false
      (S $ U $ V).isWeakRedex shouldEqual false
      (K $ U $ V $ W).isWeakRedex shouldEqual false
    }

  "B is in Weak Normal Form" in {
    val B = S $ (K $ S) $ K
    B.isWeakNormalForm shouldEqual true
  }

  "Bxyz is NOT in Weak Normal Form" in
    ∀(varGen, varGen, varGen) { (x, y, z) =>
      val B = S $ (K $ S) $ K
      val Bxyz = B $ x $ y $ z
      Bxyz.isWeakNormalForm shouldEqual false
    }

  "SKKX ▹1w KX(KX) " in
    ∀(varGen) { x =>
      val SKKx = S $ K $ K $ x
      val Kx_Kx = K $ x $ (K $ x)
      contractLeftMost(SKKx) shouldEqual Some(Kx_Kx)
      contractLeftMost(Kx_Kx) shouldEqual Some(x)
    }

  "B ≡ S(KS)K ⇒ BXYZ ▹w X(YZ)" in
    ∀(varGen, varGen, varGen) { (x, y, z) =>
      val B = S $ (K $ S) $ K
      val Bxyz = B $ x $ y $ z
      val x_yz = x $ (y $ z)
      reduceToWeakNormalForm(Bxyz) shouldEqual x_yz
    }

  "C ≡ S(BBS)(KK) ⇒ CXYZ ▹w XZY" in
    ∀(varGen, varGen, varGen) { (x, y, z) =>
      val B = S $ (K $ S) $ K
      val C = S $ (B $ B $ S) $ (K $ K)
      val Cxyz = C $ x $ y $ z
      val xzy = x $ z $ y
      reduceToWeakNormalForm(Cxyz) shouldEqual xzy
    }

  "Substitution lemma for ▹w" should {

    "X ▹w Y =⇒ FV(X) ⊇ FV(Y)" in
      ∀(termGen) { X =>
        val Y = reduceToWeakNormalForm(X)
        X.FV should contain allElementsOf Y.FV
      }

    "X ▹w Y =⇒ [X/v]Z ▹w [Y/v]Z  (with Church–Rosser theorem implicitly used)" in
      ∀(termGen, termGen, varGen) { (X, Z, v) =>
        val Y = reduceToWeakNormalForm(X)
        reduceToWeakNormalForm((X / v).apply(Z)) shouldEqual reduceToWeakNormalForm((Y / v).apply(Z))
      }

  }


}
