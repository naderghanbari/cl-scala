package lc

import LambdaGen.termGen
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll ⇒ ∀}
import org.scalatest.{Matchers, WordSpec}

class ShowTest extends WordSpec with Matchers {

  "Show class" when {

    "given some Terms and Variables" should {

      val (x, y, z) = (Var('x'), Var('y'), Var('z'))

      val I         = λ(x) { x }
      val IFull     = "(λx.x)"
      val IShort    = "λx.x"
      val IDeBruijn = "[x]x"

      val K         = λ(x)(λ(y) { x })
      val KFull     = "(λx.(λy.x))"
      val KShort    = "λxy.x"
      val KDeBruijn = "[x][y]x"

      val SBody     = x(z) ^ y(z)
      val S         = λ(x)(λ(y)(λ(z) { SBody }))
      val SFull     = "(λx.(λy.(λz.((xz)(yz)))))"
      val SShort    = "λxyz.xz(yz)"
      val SDeBruijn = "[x][y][z]((z)y)(z)x"

      val Kx                   = K(x)
      val Kxy                  = Kx(y)
      val K_xy                 = K ^ x(y)
      val SKxy                 = S(K)(x)(y)
      val S_Kx_SKK             = S ^ K(x) ^ S(K)(K)
      val S__SI_K___Kx____S_KI = (S ^ (S(I) ^ K) ^ K(x)) ^ (S ^ K(I))

      "show Terms in full format" in {
        x.full                    shouldBe "x"
        y.full                    shouldBe "y"
        z.full                    shouldBe "z"
        K.full                    shouldBe KFull
        S.full                    shouldBe SFull
        I.full                    shouldBe IFull
        Kx.full                   shouldBe s"(${KFull}x)"
        Kxy.full                  shouldBe s"((${KFull}x)y)"
        K_xy.full                 shouldBe s"($KFull(xy))"
        SKxy.full                 shouldBe s"((($SFull$KFull)x)y)"
        S_Kx_SKK.full             shouldBe s"(($SFull(${KFull}x))(($SFull$KFull)$KFull))"
        S__SI_K___Kx____S_KI.full shouldBe s"((($SFull(($SFull$IFull)$KFull))(${KFull}x))($SFull($KFull$IFull)))"
      }

      "show Term in short format" in {
        x.short        shouldBe "x"
        y.short        shouldBe "y"
        z.short        shouldBe "z"
        K.short        shouldBe KShort
        S.short        shouldBe SShort
        I.short        shouldBe IShort
        Kx.short       shouldBe s"($KShort)x"
        Kxy.short      shouldBe s"($KShort)xy"
        K_xy.short     shouldBe s"($KShort)(xy)"
        SKxy.short     shouldBe s"($SShort)($KShort)xy"
        S_Kx_SKK.short shouldBe s"($SShort)(($KShort)x)(($SShort)($KShort)($KShort))"
      }

      "show Terms in De Bruijn format" in {
        x.deBruijn    shouldBe "x"
        y.deBruijn    shouldBe "y"
        z.deBruijn    shouldBe "z"
        K.deBruijn    shouldBe KDeBruijn
        S.deBruijn    shouldBe SDeBruijn
        I.deBruijn    shouldBe IDeBruijn
        Kx.deBruijn   shouldBe s"(x)$KDeBruijn"
        Kxy.deBruijn  shouldBe s"(y)(x)$KDeBruijn"
        K_xy.deBruijn shouldBe s"((y)x)$KDeBruijn"
        SKxy.deBruijn shouldBe s"(y)(x)($KDeBruijn)$SDeBruijn"
      }

    }

    "given arbitrary Terms" should {

      "satisfy basic properties for the full, short, and De Bruijn representations" in ∀(termGen, termGen) { (U, V) ⇒
        U.full.length + V.full.length + 2   shouldEqual U(V).full.length
        U.short.length + V.short.length + 2 should be >= U(V).short.length
        U(V).full contains U.full           shouldBe true
        U(V).full contains V.full           shouldBe true
        U(V).short contains U.short         shouldBe true
        U(V).short contains V.short         shouldBe true
        U.short.length                      should be <= U.full.length
        V.short.length                      should be <= V.full.length
        U.deBruijn.count(_ == '(')          shouldEqual U.deBruijn.count(_ == ')')
        U.full.count(_ == '(')              shouldEqual U.full.count(_ == ')')
        U.short.count(_ == '(')             should be <= U.deBruijn.count(_ == '(')
        U.deBruijn.count(_ == '(')          should be <= U.full.count(_ == '(')
      }

    }

  }

}
