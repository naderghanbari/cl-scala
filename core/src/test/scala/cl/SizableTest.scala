package cl

import cl.generators.CLGen.termGen
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{forAll => ∀, _}
import org.scalatest.{Matchers, WordSpec}

class SizableTest extends WordSpec with Matchers {

  "length (lgh) is a non-negative function" in ∀(termGen) { M =>
    val length = M.length
    length should be > 0
  }

  "length (lgh) is monotonically increasing and commutative wrt Application ($)" in ∀(termGen, termGen) { (u, v) =>
    u.length should be < (v $ u).length
    u.length should be < (v $ u).length
    (u $ v).length shouldEqual (v $ u).length
  }

}
