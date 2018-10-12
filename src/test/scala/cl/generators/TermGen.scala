package cl.generators

import cl._
import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.ScalacheckShapeless.derivedArbitrary

class TermGen {

  val basicCombinatorNames = Set('I', 'K', 'S')
  val validCombinatorNames = ('A' to 'Z').toSet.diff(basicCombinatorNames).toSeq

  def varGen: Gen[Var] = Gen.alphaLowerChar.map(Var)
  def basicCombinatorGen: Gen[BasicCombinator] = arbitrary[BasicCombinator]
  def atomicConstantGen: Gen[AtomicConstant] = arbitrary[AtomicConstant]
  def atomGen: Gen[Atom] = arbitrary[Atom]

  def termGen: Gen[Term] = arbitrary[Term]

  def combinatorNameGen: Gen[Char] = Gen.oneOf(validCombinatorNames)
  def combinatorGen: Gen[Combinator] = combinatorNameGen.map(Combinator)

}
