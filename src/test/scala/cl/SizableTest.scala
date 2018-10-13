package cl

import cl.generators.CLGen.termGen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{ Matchers, WordSpec }

class SizableTest extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  ".length is a non-negative function" in
    forAll(termGen) { u =>
      u.length should be > 0
    }

  ".length (lgh) is monotonically increasing and commutative wrt application ($)" in
    forAll(termGen, termGen) { (u, v) =>
      u.length should be < (v $ u).length
      u.length should be < (v $ u).length
      (u $ v).length shouldEqual (v $ u).length
    }

}
