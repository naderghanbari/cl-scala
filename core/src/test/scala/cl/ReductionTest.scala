package cl

import cl.Reduction.{contractLeftMost, reduceToWeakNormalForm}
import cl.generators.CLGen.{termGen, varGen, weakRedexGen}
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll => ∀, _}
import org.scalatest.{Matchers, WordSpec}

class ReductionTest extends WordSpec with Matchers {

  "Weak Redexes are known and respected" in ∀(weakRedexGen) { R =>
    R.isWeakRedex shouldEqual true
    R.isWeakNormalForm shouldEqual false
  }

  "Non-Weak Redexes are known and frowned upon" in ∀(termGen, termGen, termGen) { (U, V, W) =>
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

  "Bxyz is NOT in Weak Normal Form" in ∀(varGen, varGen, varGen) { (x, y, z) =>
    val B    = S $ (K $ S) $ K
    val Bxyz = B $ x $ y $ z
    Bxyz.isWeakNormalForm shouldEqual false
  }

  "SKKX ▹1w KX(KX) " in ∀(termGen) { X =>
    val SKKX  = S $ K $ K $ X
    val KX$KX = K $ X $ (K $ X)
    contractLeftMost(SKKX) shouldEqual Some(KX$KX)
    contractLeftMost(KX$KX) shouldEqual Some(X)
  }

  "B ≡ S(KS)K ⇒ BXYZ ▹w X(YZ)        with Church–Rosser theorem implicitly used" in ∀(termGen, termGen, termGen) {
    (X, Y, Z) =>
      val B    = S $ (K $ S) $ K
      val BXYZ = B $ X $ Y $ Z
      val X$YZ = X $ (Y $ Z)
      reduceToWeakNormalForm(BXYZ) shouldEqual reduceToWeakNormalForm(X$YZ)
  }

  "C ≡ S(BBS)(KK) ⇒ CXYZ ▹w XZY      with Church–Rosser theorem implicitly used" in ∀(termGen, termGen, termGen) {
    (X, Y, Z) =>
      val B    = S $ (K $ S) $ K
      val C    = S $ (B $ B $ S) $ (K $ K)
      val CXYZ = C $ X $ Y $ Z
      val XZY  = X $ Z $ Y
      reduceToWeakNormalForm(CXYZ) shouldEqual reduceToWeakNormalForm(XZY)
  }

  "W ≡ SS(KI) ⇒ WXY ▹w XYY           with Church–Rosser theorem implicitly used" in ∀(termGen, termGen) { (X, Y) =>
    val W   = S $ S $ (K $ I)
    val WXY = W $ X $ Y
    val XYY = X $ Y $ Y
    reduceToWeakNormalForm(WXY) shouldEqual reduceToWeakNormalForm(XYY)
  }

  "B′ ≡ S(K(SB))K ⇒ B′XY Z ▹w Y(XZ)       with Church–Rosser theorem implicitly used" in ∀(termGen, termGen, termGen) {
    (X, Y, Z) =>
      val B     = S $ (K $ S) $ K
      val Bp    = S $ (K $ (S $ B)) $ K
      val BpXYZ = Bp $ X $ Y $ Z
      val Y$XZ  = Y $ (X $ Z)
      reduceToWeakNormalForm(BpXYZ) shouldEqual reduceToWeakNormalForm(Y$XZ)
  }

  "Substitution lemma for ▹w" should {

    "X ▹w Y =⇒ FV(X) ⊇ FV(Y)" in ∀(termGen) { X =>
      val Y = reduceToWeakNormalForm(X)
      X.FV should contain allElementsOf Y.FV
    }

    "X ▹w Y =⇒ [X/v]Z ▹w [Y/v]Z        with Church–Rosser theorem implicitly used" in ∀(termGen, termGen, varGen) {
      (X, Z, v) =>
        val Y = reduceToWeakNormalForm(X)
        reduceToWeakNormalForm((X / v).apply(Z)) shouldEqual reduceToWeakNormalForm((Y / v).apply(Z))
    }

  }

}
