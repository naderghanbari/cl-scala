package cl

import cl.generators.CLGen.termGen
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll ⇒ ∀, _}
import org.scalatest.{Matchers, WordSpec}

class ShowTest extends WordSpec with Matchers {

  "Show class" when {

    "given some Terms and Variables" should {

      val (x, y, z) = (Var('x'), Var('y'), Var('z'))

      val Kx                   = K(x)
      val Kxy                  = K(x)(y)
      val K_xy                 = K $ x(y)
      val SKxy                 = S(K)(x)(y)
      val S_Kx_SKK             = S $ K(x) $ S(K)(K)
      val S__SI_K___Kx____S_KI = (S $ (S(I) $ K) $ K(x)) $ (S $ K(I))

      "show Terms in full format" in {
        x.full                    shouldBe "x"
        y.full                    shouldBe "y"
        z.full                    shouldBe "z"
        Kx.full                   shouldBe "(Kx)"
        Kxy.full                  shouldBe "((Kx)y)"
        K_xy.full                 shouldBe "(K(xy))"
        SKxy.full                 shouldBe "(((SK)x)y)"
        S_Kx_SKK.full             shouldBe "((S(Kx))((SK)K))"
        S__SI_K___Kx____S_KI.full shouldBe "(((S((SI)K))(Kx))(S(KI)))"
      }

      "show Term in short format" in {
        x.short                                              shouldBe "x"
        y.short                                              shouldBe "y"
        z.short                                              shouldBe "z"
        Kx.short                                             shouldBe "Kx"
        Kxy.short                                            shouldBe "Kxy"
        K_xy.short                                           shouldBe "K(xy)"
        SKxy.short                                           shouldBe "SKxy"
        S_Kx_SKK.short                                       shouldBe "S(Kx)(SKK)"
        S__SI_K___Kx____S_KI.short                           shouldBe "S(SIK)(Kx)(S(KI))"
        (S $ S(I)(K) $ K(x) $ (S $ K(I))).short              shouldBe "S(SIK)(Kx)(S(KI))"
        (S $ (S $ (I $ x(y)) $ K) $ K(x) $ (S $ K(I))).short shouldBe "S(S(I(xy))K)(Kx)(S(KI))"
      }

    }

    "given arbitrary Terms" should {

      "satisfy basic properties for the full and short representations" in ∀(termGen, termGen) { (U, V) ⇒
        U.full.length + V.full.length + 2   shouldEqual U(V).full.length
        U.short.length + V.short.length + 2 should be >= U(V).short.length
        U(V).full contains U.full           shouldBe true
        U(V).full contains V.full           shouldBe true
        U(V).short contains U.short         shouldBe true
        U(V).short contains V.short         shouldBe true
        U.short.length                      should be <= U.full.length
        V.short.length                      should be <= V.full.length
      }

    }
  }

}
