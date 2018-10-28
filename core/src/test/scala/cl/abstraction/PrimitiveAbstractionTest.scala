package cl.abstraction

import cl._
import cl.generators.CLGen.{termGen, varGen}
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll => ∀, _}
import org.scalatest.{Matchers, WordSpec}

class PrimitiveAbstractionTest extends WordSpec with Matchers {

  import Reduction.reduceToWeakNormalForm

  "Example 2.19    - p: [x].xy ≡ SI(Ky)               ∀ x, y" in ∀(varGen, varGen) { (x, y) =>
    whenever(x != y) {
      val left  = PrimitiveAbstraction(x, x $ y)
      val right = S $ I $ (K $ y)
      reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
    }
  }

  "Theorem 2.21.A  - p: [x].M exists                  ∀ M, x" in ∀(varGen, termGen) { (x, M) =>
    PrimitiveAbstraction(x, M) should not be null
  }

  "Theorem 2.21.B  - p: ([x].M)N ▹w [N/x]M            ∀ M, N, x" in ∀(varGen, termGen, termGen) { (x, M, N) =>
    val left  = PrimitiveAbstraction(x, M) $ N
    val right = (N / x)(M)
    reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
  }

  "Theorem 2.21.B  - p: ([x].M)N ▹w [N/x]M            ∀ M, N, x ∈ FV(M)" in ∀(termGen, termGen) { (M, N) =>
    whenever(M.FV.nonEmpty) {
      val x     = M.FV.head
      val left  = PrimitiveAbstraction(x, M) $ N
      val right = (N / x)(M)
      reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
    }
  }

  "Theorem 2.21.C  - p: x ∉ FV([x].M)                 ∀ M, x" in ∀(varGen, termGen) { (x, M) =>
    val abstracted = PrimitiveAbstraction(x, M)
    abstracted.FV should not contain x
  }

  "Theorem 2.21.C  - p: x ∉ FV([x].M)                 ∀ M, x ∈ FV(M)" in ∀(termGen) { M =>
    whenever(M.FV.nonEmpty) {
      val x          = M.FV.head
      val abstracted = PrimitiveAbstraction(x, M)
      abstracted.FV should not contain x
    }
  }

  "Exercise 2.22.a - p: [x].u(vx) ≡ S(Ku)(S(Kv)I)     ∀ u, v, x" in ∀(varGen, varGen, varGen) { (u, v, x) =>
    whenever(u != v && u != x && x != v) {
      val left  = PrimitiveAbstraction(x, u $ (v $ x))
      val right = S $ (K $ u) $ (S $ (K $ v) $ I)
      left shouldEqual right
    }
  }

  "Exercise 2.22.b - p: [x].x(Sy) ≡ SI(S(KS)(Ky))     ∀ x, y" in ∀(varGen, varGen) { (x, y) =>
    whenever(x != y) {
      val left  = PrimitiveAbstraction(x, x $ (S $ y))
      val right = S $ I $ (S $ (K $ S) $ (K $ y))
      left shouldEqual right
    }
  }

  "Exercise 2.22.c - p: [x].uxxv ≡ S(S(S(Ku)I)I)(Kv)  ∀ u, v, x" in ∀(varGen, varGen, varGen) { (u, v, x) =>
    whenever(u != v && u != x && x != v) {
      val left  = PrimitiveAbstraction(x, u $ x $ x $ v)
      val right = S $ (S $ (S $ (K $ u) $ I) $ I) $ (K $ v)
      left shouldEqual right
    }
  }

}
