package cl.abstraction

import cl._
import cl.generators.CLGen.{termGen, varGen}
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll => ∀, _}
import org.scalatest.{Matchers, WordSpec}

class EtaAbstractionTest extends WordSpec with Matchers {

  import Reduction.reduceToWeakNormalForm

  "Example 2.19    - η: [x].xy ≡ SI(Ky)        ∀ x, y" in ∀(varGen, varGen) { (x, y) =>
    whenever(x != y) {
      val left  = EtaAbstraction(x, x(y))
      val right = S(I) $ K(y)
      reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
    }
  }

  "Theorem 2.21.A  - η: [x].M exists           ∀ M, x" in ∀(varGen, termGen) { (x, M) =>
    EtaAbstraction(x, M) should not be null
  }

  "Theorem 2.21.B  - η: ([x].M)N ▹w [N/x]M     ∀ M, N, x" in ∀(varGen, termGen, termGen) { (x, M, N) =>
    val left  = EtaAbstraction(x, M) $ N
    val right = (N / x)(M)
    reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
  }

  "Theorem 2.21.B  - η: ([x].M)N ▹w [N/x]M     ∀ M, N, x ∈ FV(M)" in ∀(termGen, termGen) { (M, N) =>
    whenever(M.FV.nonEmpty) {
      val x     = M.FV.head
      val left  = EtaAbstraction(x, M) $ N
      val right = (N / x)(M)
      reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
    }
  }

  "Theorem 2.21.C  - η: x ∉ FV([x].M)          ∀ M, x" in ∀(varGen, termGen) { (x, M) =>
    val abstracted = EtaAbstraction(x, M)
    abstracted.FV should not contain x
  }

  "Theorem 2.21.C  - η: x ∉ FV([x].M)          ∀ M, x ∈ FV(M)" in ∀(termGen) { M =>
    whenever(M.FV.nonEmpty) {
      val x          = M.FV.head
      val abstracted = EtaAbstraction(x, M)
      abstracted.FV should not contain x
    }
  }

  "Exercise 2.22.a - η: [x].u(vx) ≡ S(Ku)v     ∀ u, v, x" in ∀(varGen, varGen, varGen) { (u, v, x) =>
    whenever(u != v && u != x && x != v) {
      EtaAbstraction(x, u $ v(x)) shouldEqual (S $ K(u) $ v)
    }
  }

  "Exercise 2.22.b - η: [x].x(Sy) ≡ SI(K(Sy))  ∀ x, y" in ∀(varGen, varGen) { (x, y) =>
    whenever(x != y) {
      EtaAbstraction(x, x $ S(y)) shouldEqual (S(I) $ (K $ S(y)))
    }
  }

  "Exercise 2.22.c - η: [x].uxxv ≡ S(SuI)(Kv)  ∀ u, v, x" in ∀(varGen, varGen, varGen) { (u, v, x) =>
    whenever(u != v && u != x && x != v) {
      EtaAbstraction(x, u(x)(x)(v)) shouldEqual (S $ S(u)(I) $ K(v))
    }
  }

  "Exercise 2.25.a - η: [x,y].x ≡ K            ∀ x, y" in ∀(varGen, varGen) { (x, y) =>
    whenever(x != y) {
      EtaAbstraction(x, EtaAbstraction(y, x)) shouldEqual K
    }
  }

  "Exercise 2.25.b - η: [x,y,z].xz(yz) ≡ S     ∀ x, y, z" in ∀(varGen, varGen, varGen) { (x, y, z) =>
    whenever(x != y && x != z && z != y) {
      val left = EtaAbstraction(x, EtaAbstraction(y, EtaAbstraction(z, x(z) $ y(z))))
      left shouldEqual S
    }
  }

}
