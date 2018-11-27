package lc

import org.scalatest.{Matchers, WordSpec}

class OccurrenceTest extends WordSpec with Matchers {

  "Lambda Occurrence trait" when {

    "given some Terms" should {

      val (x, y, z) = (Var('x'), Var('y'), Var('z'))

      val I     = λ(x) { x }
      val K     = λ(x)(λ(y) { x })
      val SBody = x(z) ^ y(z)
      val S     = λ(x)(λ(y)(λ(z) { SBody }))
      val Kx    = K(x)
      val Kxy   = Kx(y)
      val K_xy  = K ^ x(y)

      "define the 'Occurs in' partial order relation" in {
        x ⊆ I     shouldBe true
        x ⊆ K     shouldBe true
        y ⊆ K     shouldBe true
        SBody ⊆ S shouldBe true

        x ⊆ Kxy   shouldBe true
        z ⊆ Kxy   shouldBe false
        Kx ⊆ Kxy  shouldBe true
        Kx ⊆ K_xy shouldBe false

        val subTerm   = (S ^ K(x)) ^ I(K)
        val superTerm = ((S ^ (S(I) ^ ((S ^ K(x)) ^ I(K)))) ^ K(x)) ^ (S ^ K(I))
        subTerm ⊆ superTerm shouldBe true
      }

      "define the 'Has Occurrence of' partial order relation" in {
        Kxy ⊇ x   shouldBe true
        Kxy ⊇ z   shouldBe false
        Kxy ⊇ Kx  shouldBe true
        K_xy ⊇ Kx shouldBe false

        val subTerm   = (S ^ K(x)) ^ I(K)
        val superTerm = ((S ^ (S(I) ^ ((S ^ K(x)) ^ I(K)))) ^ K(x)) ^ (S ^ K(I))
        superTerm ⊇ subTerm shouldBe true
      }

    }

  }

}
