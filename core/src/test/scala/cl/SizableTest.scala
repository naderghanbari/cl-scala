package cl

import cl.generators.CLGen.termGen
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll ⇒ ∀, _}
import org.scalatest.{Matchers, WordSpec}

class SizableTest extends WordSpec with Matchers {

  "length (lgh) is a non-negative function" in ∀(termGen) { M ⇒
    val length = M.length
    length should be > 0
  }

  "length (lgh) is monotonically increasing and commutative wrt Application ($)" in ∀(termGen, termGen) { (U, V) ⇒
    U.length    should be < V(U).length
    U.length    should be < V(U).length
    U(V).length shouldEqual V(U).length
  }

}
