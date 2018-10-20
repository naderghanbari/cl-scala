package cl.parser.lexer

import cl.generators.CLGen.termGen
import org.scalatest.{ EitherValues, Matchers, WordSpec }
import org.scalatest.prop.GeneratorDrivenPropertyChecks.{ forAll => ∀, _ }

class CLLexerTest extends WordSpec with Matchers with EitherValues {

  "CLLexer" should {

    "parse valid CL Terms represented in short format" in {
      CLLexer("SKIx").right.get shouldEqual List(S, K, I, VAR('x'))
      CLLexer("S(KI)uv").right.get shouldEqual List(S, `(`, K, I, `)`, VAR('u'), VAR('v'))
    }

    "parse random CL Terms represented in full format" in
      ∀(termGen) { M =>
        val result = CLLexer(M.full)
        result should be('right)
        result.right.get.size shouldEqual M.full.length
      }

    "parse random CL Terms represented in short format" in
      ∀(termGen) { M =>
        val result = CLLexer(M.short)
        result should be('right)
        result.right.get.size should be >= M.short.length
      }

    "refuse to parse an invalid CL Term" in {
      val result = CLLexer("x.Xy")
      result should be('left)
    }

  }

}
