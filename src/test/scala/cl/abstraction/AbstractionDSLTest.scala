package cl.abstraction

import cl.Reduction.reduceToWeakNormalForm
import cl._
import cl.generators.CLGen.{ termGen, varGen }
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{ forAll => ∀, _ }
import org.scalatest.{ Matchers, WordSpec }

class AbstractionDSLTest extends WordSpec with Matchers {

  "Theorem 2.21.B  --- η Bracket Symbolic DSL           ([x].M)N ▹w [N/x]M        ∀ M, N, x" in
    ∀(varGen, termGen, termGen) { (x, M, N) =>
      import Abstraction.Implicits.etaAbstraction
      val left = |(x)| M $ N
      val right = (N / x) (M)
      reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
    }

  "Exercise 2.22.b --- w Bracket DSL                [x].x(Sy) ≡ SI(S(KS)(Ky))     ∀ x, y" in
    ∀(varGen, varGen) { (x, y) =>
      whenever(x != y) {
        import Abstraction.Implicits.weakAbstraction
        val left = Bracket(x).apply(x $ (S $ y))
        val right = S $ I $ (K $ (S $ y))
        left shouldEqual right
      }
    }

  "Exercise 2.22.c --- p Bracket Symbolic DSL     [x].uxxv ≡ S(S(S(Ku)I)I)(Kv)    ∀ u, v, x" in
    ∀(varGen, varGen, varGen) { (u, v, x) =>
      whenever(u != v && u != x && x != v) {
        import Abstraction.Implicits.primitiveAbstraction
        val left = |(x)| (u $ x $ x $ v)
        val right = S $ (S $ (S $ (K $ u) $ I) $ I) $ (K $ v)
        left shouldEqual right
      }
    }


}
