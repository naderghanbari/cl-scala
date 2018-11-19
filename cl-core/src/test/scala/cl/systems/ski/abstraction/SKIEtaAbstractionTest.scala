package cl.systems.ski.abstraction

import cl.CLGen.{ termGen, varGen }
import cl._
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{ forAll => ∀, _ }
import org.scalatest.{ Matchers, WordSpec }

class SKIEtaAbstractionTest extends WordSpec with Matchers {

  import Reduction.reduceToWeakNormalForm
  import cl.systems.CLSystem.Implicits.SKI
  import cl.systems.ski.SKI.{ I, K, S }

  "Example 2.19    - η: [x].xy ≡ SI(Ky)        ∀ x, y" in ∀(varGen, varGen) { (x, y) ⇒
    whenever(x != y) {
      val left  = Eta(x, x(y))
      val right = S(I) ^ K(y)
      reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
    }
  }

  "Theorem 2.21.A  - η: [x].M exists           ∀ M, x" in ∀(varGen, termGen) { (x, M) ⇒
    Eta(x, M) should not be null
  }

  "Theorem 2.21.B  - η: ([x].M)N ▹w [N/x]M     ∀ M, N, x" in ∀(varGen, termGen, termGen) { (x, M, N) ⇒
    val left  = Eta(x, M) ^ N
    val right = (N / x)(M)
    reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
  }

  "Theorem 2.21.B  - η: ([x].M)N ▹w [N/x]M     ∀ M, N, x ∈ FV(M)" in ∀(termGen, termGen) { (M, N) ⇒
    whenever(M.FV.nonEmpty) {
      val x     = M.FV.head
      val left  = Eta(x, M) ^ N
      val right = (N / x)(M)
      reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
    }
  }

  "Theorem 2.21.C  - η: x ∉ FV([x].M)          ∀ M, x" in ∀(varGen, termGen) { (x, M) ⇒
    val abstracted = Eta(x, M)
    abstracted.FV should not contain x
  }

  "Theorem 2.21.C  - η: x ∉ FV([x].M)          ∀ M, x ∈ FV(M)" in ∀(termGen) { M ⇒
    whenever(M.FV.nonEmpty) {
      val x          = M.FV.head
      val abstracted = Eta(x, M)
      abstracted.FV should not contain x
    }
  }

  "Exercise 2.22.a - η: [x].u(vx) ≡ S(Ku)v     ∀ u, v, x" in ∀(varGen, varGen, varGen) { (u, v, x) ⇒
    whenever(u != v && u != x && x != v) {
      Eta(x, u ^ v(x)) shouldEqual (S ^ K(u) ^ v)
    }
  }

  "Exercise 2.22.b - η: [x].x(Sy) ≡ SI(K(Sy))  ∀ x, y" in ∀(varGen, varGen) { (x, y) ⇒
    whenever(x != y) {
      Eta(x, x ^ S(y)) shouldEqual (S(I) ^ (K ^ S(y)))
    }
  }

  "Exercise 2.22.c - η: [x].uxxv ≡ S(SuI)(Kv)  ∀ u, v, x" in ∀(varGen, varGen, varGen) { (u, v, x) ⇒
    whenever(u != v && u != x && x != v) {
      Eta(x, u(x)(x)(v)) shouldEqual (S ^ S(u)(I) ^ K(v))
    }
  }

  "Exercise 2.25.a - η: [x,y].x ≡ K            ∀ x, y" in ∀(varGen, varGen) { (x, y) ⇒
    whenever(x != y) {
      Eta(x, Eta(y, x)) shouldEqual K
    }
  }

  "Exercise 2.25.b - η: [x,y,z].xz(yz) ≡ S     ∀ x, y, z" in ∀(varGen, varGen, varGen) { (x, y, z) ⇒
    whenever(x != y && x != z && z != y) {
      val left = Eta(x, Eta(y, Eta(z, x(z) ^ y(z))))
      left shouldEqual S
    }
  }

}
