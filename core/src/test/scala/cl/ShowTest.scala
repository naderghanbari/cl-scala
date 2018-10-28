package cl

import cl.generators.CLGen.termGen
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll => ∀, _}
import org.scalatest.{Matchers, WordSpec}

class ShowTest extends WordSpec with Matchers {

  "Show class" when {

    "given some Terms and Variables" should {

      val (x, y, z) = (Var('x'), Var('y'), Var('z'))

      val Kx                   = K $ x
      val Kxy                  = Kx $ y
      val K_xy                 = K $ (x $ y)
      val SKxy                 = S $ K $ x $ y
      val S_Kx_SKK             = S $ (K $ x) $ (S $ K $ K)
      val S__SI_K___Kx____S_KI = (S $ ((S $ I) $ K) $ (K $ x)) $ (S $ (K $ I))

      "show Terms in full format" in {
        x.full shouldBe "x"
        y.full shouldBe "y"
        z.full shouldBe "z"
        Kx.full shouldBe "(Kx)"
        Kxy.full shouldBe "((Kx)y)"
        K_xy.full shouldBe "(K(xy))"
        SKxy.full shouldBe "(((SK)x)y)"
        S_Kx_SKK.full shouldBe "((S(Kx))((SK)K))"
        S__SI_K___Kx____S_KI.full shouldBe "(((S((SI)K))(Kx))(S(KI)))"
      }

      "show Term in short format" in {
        x.short shouldBe "x"
        y.short shouldBe "y"
        z.short shouldBe "z"
        Kx.short shouldBe "Kx"
        Kxy.short shouldBe "Kxy"
        K_xy.short shouldBe "K(xy)"
        SKxy.short shouldBe "SKxy"
        S_Kx_SKK.short shouldBe "S(Kx)(SKK)"
        S__SI_K___Kx____S_KI.short shouldBe "S(SIK)(Kx)(S(KI))"
        (S $ (S $ I $ K) $ (K $ x) $ (S $ (K $ I))).short shouldBe "S(SIK)(Kx)(S(KI))"
        (S $ (S $ (I $ (x $ y)) $ K) $ (K $ x) $ (S $ (K $ I))).short shouldBe "S(S(I(xy))K)(Kx)(S(KI))"
      }

    }

    "given arbitrary Terms" should {

      "satisfy basic properties for the full and short representations" in ∀(termGen, termGen) { (u, v) =>
        u.full.length + v.full.length + 2 shouldEqual (u $ v).full.length
        u.short.length + v.short.length + 2 should be >= (u $ v).short.length
        (u $ v).full contains u.full shouldBe true
        (u $ v).full contains v.full shouldBe true
        (u $ v).short contains u.short shouldBe true
        (u $ v).short contains v.short shouldBe true
        u.short.length should be <= u.full.length
        v.short.length should be <= v.full.length
      }

    }
  }

}
