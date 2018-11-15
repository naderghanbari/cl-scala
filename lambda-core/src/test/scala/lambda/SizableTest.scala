package lambda

import LambdaGen.termGen
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll ⇒ ∀}
import org.scalatest.{Matchers, WordSpec}

class SizableTest extends WordSpec with Matchers {

  "Lambda Term length (lgh) is a non-negative function" in ∀(termGen) { M ⇒
    val length = M.length
    length should be > 0
  }

  "Lambda Term length (lgh) is increasing and commutative wrt Application (^)" in ∀(termGen, termGen) { (U, V) ⇒
    U.length       should be < ^(U, V).length
    U.length       should be < ^(V, U).length
    ^(U, V).length shouldEqual ^(V, U).length
  }

}
