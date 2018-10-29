package cl.compiler

import cl.compiler.ast.{Defn, Ref, Var}
import org.scalatest.{EitherValues, Matchers, WordSpec}

class CLCompilerTest extends WordSpec with Matchers with EitherValues {

  val I = Ref('I')
  val K = Ref('K')
  val S = Ref('S')

  "CLCompiler" should {

    "compile valid CL Terms represented in full format" in {
      CLCompiler("(((SK)I)x)").right.get          shouldEqual S(K)(I)(Var('x'))
      CLCompiler("(((SK)K)((KS)(Sx)))").right.get shouldEqual (S(K)(K) $ (K(S) $ S(Var('x'))))
    }

    "compile valid CL Term Definitions represented in full format" in {
      CLCompiler("M := (((SK)I)x)").right.get          shouldEqual Defn(Ref('M'), S(K)(I)(Var('x')))
      CLCompiler("N := (((SK)K)((KS)(Sx)))").right.get shouldEqual Defn(Ref('N'), S(K)(K) $ (K(S) $ S(Var('x'))))
    }

  }

}
