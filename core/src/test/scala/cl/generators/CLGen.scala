package cl.generators

import cl._
import org.scalacheck.Gen

object CLGen {

  import Gen._

  def varGen: Gen[Var]                         = alphaLowerChar.map(Var)
  def basicCombinatorGen: Gen[BasicCombinator] = oneOf(I, K, S)
  def atomicConstantGen: Gen[AtomicConstant]   = basicCombinatorGen
  def atomGen: Gen[Atom] =
    oneOf(varGen, basicCombinatorGen, basicCombinatorGen, basicCombinatorGen)

  def weakRedexIGen: Gen[Term] = termGen.map(U => I $ U)
  def weakRedexKGen: Gen[Term] = termGen.flatMap(U => termGen.map(V => K $ U $ V))
  def weakRedexSGen: Gen[Term] = termGen.flatMap(U => termGen.flatMap(V => termGen.map(W => S $ U $ V $ W)))
  def weakRedexGen: Gen[Term]  = oneOf(weakRedexIGen, weakRedexKGen, weakRedexSGen)

  private def closedLeftGen =
    lzy(frequency((7, closedTermGen), (2, basicCombinatorGen)))
  private def closedRightGen =
    lzy(frequency((1, basicCombinatorGen), (2, closedTermGen), (6, basicCombinatorGen)))
  private def closedApplicationGen =
    closedLeftGen.flatMap(U => closedRightGen.map(V => U $ V))

  def closedTermGen: Gen[Term] = lzy(frequency((7, closedApplicationGen), (2, basicCombinatorGen)))

  private def leftGen                  = lzy(frequency((7, termGen), (2, atomGen)))
  private def rightGen                 = lzy(frequency((1, atomGen), (2, termGen), (6, atomGen)))
  def applicationGen: Gen[Application] = leftGen.flatMap(U => rightGen.map(V => U $ V))

  def termGen: Gen[Term] = lzy(frequency((7, applicationGen), (2, atomGen)))

  val basicCombinatorNames      = Seq('I', 'K', 'S')
  def termRefNameGen: Gen[Char] = oneOf(('A' to 'Z').diff(basicCombinatorNames))

}
