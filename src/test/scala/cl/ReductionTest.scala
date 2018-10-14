package cl

import org.scalatest.{ Matchers, WordSpec }

class ReductionTest extends WordSpec with Matchers {

  import cl.Reduction.{ contractLeftMost, reduceToWeakNormalForm }

  "SKKX ▹1w KX(KX) " in {
    val x = Var('x')
    val SKKx = S $ K $ K $ x
    val Kx_Kx = K $ x $ (K $ x)
    contractLeftMost(SKKx) shouldEqual Some(Kx_Kx)
    contractLeftMost(Kx_Kx) shouldEqual Some(x)
  }

  "B ≡ S(KS)K ⇒ BXYZ ▹w X(YZ)" in {
    val (x, y, z) = (Var('x'), Var('y'), Var('z'))
    val B = S $ (K $ S) $ K
    val Bxyz = B $ x $ y $ z
    val x_yz = x $ (y $ z)
    reduceToWeakNormalForm(Bxyz) shouldEqual x_yz
  }

}
