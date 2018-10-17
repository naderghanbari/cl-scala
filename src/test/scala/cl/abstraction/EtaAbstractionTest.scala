package cl.abstraction

import cl._
import cl.Reduction.reduceToWeakNormalForm
import cl.generators.CLGen.{ termGen, varGen }
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{ forAll => ∀, _ }
import org.scalatest.{ Matchers, WordSpec }

class EtaAbstractionTest extends WordSpec with Matchers {

  import cl.abstraction.Implicits.EtaAbstraction

  "Example 2.19    --- η      [x].xy ≡ SI(Ky)           ∀ x, y" in
    ∀(varGen, varGen) { (x, y) =>
      whenever(x != y) {
        val left = EtaAbstraction(x, x $ y)
        val right = S $ I $ (K $ y)
        reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
      }
    }

  "Theorem 2.21.A  --- η      [x].M exists              ∀ M, x" in
    ∀(varGen, termGen) { (x, M) =>
      EtaAbstraction(x, M) should not be null
    }

  "Theorem 2.21.B  --- η      ([x].M)N ▹w [N/x]M        ∀ M, N, x" in
    ∀(varGen, termGen, termGen) { (x, M, N) =>
      val left = EtaAbstraction(x, M) $ N
      val right = (N / x) (M)
      reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
    }

  "Theorem 2.21.B  --- η      ([x].M)N ▹w [N/x]M        ∀ M, N, x ∈ FV(M)" in
    ∀(termGen, termGen) { (M, N) =>
      whenever(M.FV.nonEmpty) {
        val x = M.FV.head
        val left = EtaAbstraction(x, M) $ N
        val right = (N / x) (M)
        reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
      }
    }

  "Theorem 2.21.C  --- η      x ∉ FV([x].M)             ∀ M, x" in
    ∀(varGen, termGen) { (x, M) =>
      val abstracted = EtaAbstraction(x, M)
      abstracted.FV should not contain x
    }

  "Theorem 2.21.C  --- η      x ∉ FV([x].M)             ∀ M, x ∈ FV(M)" in
    ∀(termGen) { M =>
      whenever(M.FV.nonEmpty) {
        val x = M.FV.head
        val abstracted = EtaAbstraction(x, M)
        abstracted.FV should not contain x
      }
    }

  "Exercise 2.22.a --- η      [x].u(vx) ≡ S(Ku)v        ∀ u, v, x" in
    ∀(varGen, varGen, varGen) { (u, v, x) =>
      whenever(u != v && u != x && x != v) {
        val left = EtaAbstraction(x, u $ (v $ x))
        val right = S $ (K $ u) $ v
        left shouldEqual right
      }
    }

  "Exercise 2.22.b --- η      [x].x(Sy) ≡ SI(K(Sy))     ∀ x, y" in
    ∀(varGen, varGen) { (x, y) =>
      whenever(x != y) {
        val left = EtaAbstraction(x, x $ (S $ y))
        val right = S $ I $ (K $ (S $ y))
        left shouldEqual right
      }
    }

  "Exercise 2.22.c --- η      [x].uxxv ≡ S(SuI)(Kv)     ∀ u, v, x" in
    ∀(varGen, varGen, varGen) { (u, v, x) =>
      whenever(u != v && u != x && x != v) {
        val left = EtaAbstraction(x, u $ x $ x $ v)
        val right = S $ (S $ u $ I) $ (K $ v)
        left shouldEqual right
      }
    }

}
