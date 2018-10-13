package cl.generators

import cl._
import org.scalacheck.Gen
import org.scalacheck.Gen._

object CLGen {

  def varGen: Gen[Var] = alphaLowerChar.map(Var)
  def basicCombinatorGen: Gen[BasicCombinator] = oneOf(I, K, S)
  def atomicConstantGen: Gen[AtomicConstant] = basicCombinatorGen
  def atomGen: Gen[Atom] = oneOf(varGen, basicCombinatorGen, basicCombinatorGen, basicCombinatorGen)

  def applicationGen: Gen[$] = for {
    u <- lzy(frequency((7, termGen), (2, atomGen)))
    v <- lzy(frequency((1, atomGen), (2, termGen), (6, atomGen)))
  } yield u $ v
  def termGen: Gen[Term] = lzy(frequency((7, applicationGen), (2, atomGen)))

  private def closedApplicationGen: Gen[$] = for {
    u <- lzy(frequency((7, closedTermGen), (2, basicCombinatorGen)))
    v <- lzy(frequency((1, basicCombinatorGen), (2, closedTermGen), (6, basicCombinatorGen)))
  } yield u $ v
  def closedTermGen: Gen[Term] = lzy(frequency((7, closedApplicationGen), (2, basicCombinatorGen)))

  val basicCombinatorNames = Set('I', 'K', 'S')
  val validCombinatorNames = ('A' to 'Z').toSet.diff(basicCombinatorNames).toSeq
  def combinatorNameGen: Gen[Char] = oneOf(validCombinatorNames)
  def combinatorGen: Gen[Combinator] = combinatorNameGen.map(Combinator)

}
