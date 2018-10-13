package cl

import cl.generators.CLGen.{ atomicConstantGen, closedTermGen, termGen, varGen }
import org.scalatest.{ Matchers, WordSpec }
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{ forAll => ∀, _ }

class ScopeTest extends WordSpec with Matchers {

  "FV(UV) = F(U) ∪ FV(V)       ∀ U,V" in ∀(termGen, termGen) { (u, v) =>
    (u $ v).FV shouldEqual (u.FV union v.FV)
  }

  "FV(A) = ∅                   ∀ Atomic Constants A" in ∀(atomicConstantGen) { c =>
    c.FV shouldBe empty
    c.isClosed shouldBe true
  }

  "FV(C) = ∅                   ∀ Closed Terms C" in ∀(closedTermGen) { U =>
    U.FV shouldBe empty
    U.isClosed shouldBe true
  }

  "FV(x) = {x}                 ∀ x" in ∀(varGen) { x =>
    x.FV shouldBe Set(x)
    x.isClosed shouldBe false
  }


}
