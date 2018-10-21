package cl.compiler

import cl._
import org.scalatest.{EitherValues, Matchers, WordSpec}

class CLCompilerTest extends WordSpec with Matchers with EitherValues {

  "CLCompiler" should {

    "compile valid CL Terms represented in full format" in {
      CLCompiler("(((SK)I)x)").right.get shouldEqual (S $ K $ I $ Var('x'))
      CLCompiler("(((SK)K)((KS)(Sx)))").right.get shouldEqual (S $ K $ K $ ((K $ S) $ (S $ Var('x'))))
    }

  }

}
