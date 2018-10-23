package cl

import cl.generators.CLGen.varGen
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{ forAll => ∀, _ }
import org.scalatest.{ Matchers, WordSpec }

class WeakEqualityTest extends WordSpec with Matchers {

  "Exercise 2.30: BWBIx =w SIIx" in ∀(varGen) { x =>
    val W = S $ S $ (K $ I)
    val B = S $ (K $ S) $ K
    val left = B $ W $ B $ I $ x
    val right = S $ I $ I $ x
    (left weakEquals right) shouldBe true
  }

}
