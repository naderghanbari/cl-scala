package cl

import cl.Abstraction._
import cl.Reduction.reduceToWeakNormalForm
import cl.generators.CLGen.{ termGen, varGen }
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{ forAll => ∀, _ }
import org.scalatest.{ Matchers, WordSpec }

class AbstractionTest extends WordSpec with Matchers {

  "Example 2.19   ---        [x].xy ≡ SI(Ky)           ∀ x, y" in
    ∀(varGen, varGen) { (x, y) =>
      whenever(x != y) {
        val left = |(x)| (x $ y)
        val right = S $ I $ (K $ y)
        reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
      }
    }

  "Theorem 2.21.A ---        [x].M exists              ∀ M, x" in
    ∀(varGen, termGen) { (x, M) =>
      |(x)| M should not be null
    }

  "Theorem 2.21.B ---        ([x].M)N ▹w [N/x]M        ∀ M, N, x" in
    ∀(varGen, termGen, termGen) { (x, M, N) =>
      val left = |(x)| M $ N
      val right = (N / x) (M)
      reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
    }

  "Theorem 2.21.B ---        ([x].M)N ▹w [N/x]M        ∀ M, N, x ∈ FV(M)" in
    ∀(termGen, termGen) { (M, N) =>
      whenever(M.FV.nonEmpty) {
        val x = M.FV.head
        val left = |(x)| M $ N
        val right = (N / x) (M)
        reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
      }
    }

  "Theorem 2.21.C ---        x ∉ FV([x].M)             ∀ M, x" in
    ∀(varGen, termGen) { (x, M) =>
      val abstracted = |(x)| M
      abstracted.FV should not contain x
    }

  "Theorem 2.21.C ---        x ∉ FV([x].M)             ∀ M, x ∈ FV(M)" in
    ∀(termGen) { M =>
      whenever(M.FV.nonEmpty) {
        val x = M.FV.head
        val abstracted = |(x)| M
        abstracted.FV should not contain x
      }
    }

  "Exercise 2.22   ---        [x].u(vx) ≡ S(Ku)v       ∀ u, v, x" in
    ∀(varGen, varGen, varGen) { (u, v, x) =>
      whenever(u != v && u != x && x != v) {
        val left = |(x)| (u $ (v $ x))
        val right = S $ (K $ u) $ v
        left shouldEqual right
      }
    }

}
