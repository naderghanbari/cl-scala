package cl.compiler

import cl.compiler.ast.{TermRef, Var}
import org.scalatest.{EitherValues, Matchers, WordSpec}

class CLCompilerTest extends WordSpec with Matchers with EitherValues {

  val I = TermRef('I')
  val K = TermRef('K')
  val S = TermRef('S')

  "CLCompiler" should {

    "compile valid CL Terms represented in full format" in {
      CLCompiler("(((SK)I)x)").right.get shouldEqual (S $ K $ I $ Var('x'))
      CLCompiler("(((SK)K)((KS)(Sx)))").right.get shouldEqual (S $ K $ K $ ((K $ S) $ (S $ Var('x'))))
    }

  }

}
