package cl.lang

import cl.lang.ast.{Abstraction ⇒ `[]`, Defn ⇒ `=:`, Ref, Var}
import org.scalatest.{EitherValues, Matchers, WordSpec}

class CLCompilerTest extends WordSpec with Matchers with EitherValues {

  "CLCompiler" should {

    val I         = Ref('I')
    val K         = Ref('K')
    val S         = Ref('S')
    val (x, y, z) = (Var('x'), Var('y'), Var('z'))

    "compile valid CL Terms" in {
      CLCompiler("SKI").right.get                 shouldEqual S(K)(I)
      CLCompiler("S(KI)").right.get               shouldEqual (S $ K(I))
      CLCompiler("S(KI)K").right.get              shouldEqual (S $ K(I) $ K)
      CLCompiler("(((SK)I)x)").right.get          shouldEqual S(K)(I)(x)
      CLCompiler("(((SK)K)((KS)(Sx)))").right.get shouldEqual (S(K)(K) $ (K(S) $ S(x)))
    }

    "compile valid CL Abstractions" in {
      CLCompiler("[x].x").right.get          shouldEqual `[]`(List(x), x)
      CLCompiler("[x,y].x").right.get        shouldEqual `[]`(List(x, y), x)
      CLCompiler("[x].(xx)").right.get       shouldEqual `[]`(List(x), x(x))
      CLCompiler("[x,y,z].xz").right.get     shouldEqual `[]`(List(x, y, z), x(z))
      CLCompiler("[x,y,z].xz(yz)").right.get shouldEqual `[]`(List(x, y, z), x(z) $ y(z))
    }

    "compile valid CL Term Definitions" in {
      CLCompiler("M := (((SK)I)x)").right.get          shouldEqual `=:`(Ref('M'), S(K)(I)(x))
      CLCompiler("N := (((SK)K)((KS)(Sx)))").right.get shouldEqual `=:`(Ref('N'), S(K)(K) $ (K(S) $ S(x)))
    }

  }

}
