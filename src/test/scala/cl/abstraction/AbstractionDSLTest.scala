package cl.abstraction

import cl.Reduction.reduceToWeakNormalForm
import cl._
import cl.generators.CLGen.{ termGen, varGen }
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{ forAll => ∀, _ }
import org.scalatest.{ Matchers, WordSpec }

class AbstractionDSLTest extends WordSpec with Matchers {

  import cl.abstraction.Abstraction._

  "Exercise 2.22.c --- p Bracket DSL       [x].uxxv ≡ S(S(S(Ku)I)I)(Kv)    ∀ u, v, x" in
    ∀(varGen, varGen, varGen) { (u, v, x) =>
      whenever(u != v && u != x && x != v) {
        import cl.abstraction.Implicits.PrimitiveAbstraction
        val left = |(x)| (u $ x $ x $ v)
        val right = S $ (S $ (S $ (K $ u) $ I) $ I) $ (K $ v)
        left shouldEqual right
      }
    }

  "Theorem 2.21.B  --- η Bracket DSL             ([x].M)N ▹w [N/x]M        ∀ M, N, x" in
    ∀(varGen, termGen, termGen) { (x, M, N) =>
      import cl.abstraction.Implicits.EtaAbstraction
      val left = |(x)| M $ N
      val right = (N / x) (M)
      reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
    }


}
