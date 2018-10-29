package cl.abstraction

import cl._
import cl.generators.CLGen.{termGen, varGen}
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll => ∀, _}
import org.scalatest.{Matchers, WordSpec}

class AbstractionDSLTest extends WordSpec with Matchers {

  import Reduction.reduceToWeakNormalForm

  "Theorem 2.21.B - η DSL: ([x].M)N ▹w [N/x]M             ∀ M, N, x" in ∀(varGen, termGen, termGen) { (x, M, N) =>
    import Abstraction.Implicits.eta
    val left  = |(x) | M $ N
    val right = (N / x)(M)
    reduceToWeakNormalForm(left) shouldEqual reduceToWeakNormalForm(right)
  }

  "Exercise 2.22.b - w DSL: [x].x(Sy) ≡ SI(K(Sy))         ∀ x, y" in ∀(varGen, varGen) { (x, y) =>
    whenever(x != y) {
      import Abstraction.Implicits.weak
      |(x) | (x $ S(y)) shouldEqual (S(I) $ (K $ S(y)))
    }
  }

  "Exercise 2.22.c - p DSL: [x].uxxv ≡ S(S(S(Ku)I)I)(Kv)  ∀ u, v, x" in ∀(varGen, varGen, varGen) { (u, v, x) =>
    whenever(u != v && u != x && x != v) {
      import Abstraction.Implicits.primitive
      |(x) | (u $ x $ x $ v) shouldEqual (S $ (S $ (S $ K(u) $ I) $ I) $ K(v))
    }
  }

  "Exercise 2.25.a - η DSL: [x,y].x ≡ K                   ∀ x, y" in ∀(varGen, varGen) { (x, y) =>
    whenever(x != y) {
      import Abstraction.Implicits.eta
      |(x, y) | x shouldEqual K
    }
  }

  "Exercise 2.25.b --η DSL: [x,y,z].xz(yz) ≡ S            ∀ x, y, z" in ∀(varGen, varGen, varGen) { (x, y, z) =>
    whenever(x != y && x != z && z != y) {
      import Abstraction.Implicits.eta
      |(x, y, z) | (x(z) $ y(z)) shouldEqual S
    }
  }

}
