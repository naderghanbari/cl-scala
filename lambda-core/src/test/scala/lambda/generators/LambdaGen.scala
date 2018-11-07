package lambda.generators

import lambda._
import org.scalacheck.Gen

object LambdaGen {

  import Gen._

  def varGen: Gen[Var]   = alphaLowerChar.map(Var)
  def atomGen: Gen[Atom] = varGen

  private def closedLeftGen =
    lzy(frequency((7, closedTermGen), (2, varGen)))
  private def closedRightGen =
    lzy(frequency((1, varGen), (2, closedTermGen), (6, varGen)))
  private def closedApplicationGen =
    closedLeftGen.flatMap(U ⇒ closedRightGen.map(V ⇒ $(U, V)))

  def closedTermGen: Gen[Term] = lzy(frequency((7, closedApplicationGen), (2, varGen)))

  private def leftGen                  = lzy(frequency((7, termGen), (2, atomGen)))
  private def rightGen                 = lzy(frequency((1, atomGen), (2, termGen), (6, atomGen)))
  def applicationGen: Gen[Application] = leftGen.flatMap(U ⇒ rightGen.map(V ⇒ $(U, V)))

  def abstractionGen: Gen[Abstraction] = varGen.flatMap(x ⇒ termGen.map(M ⇒ λ(x) { M }))

  def termGen: Gen[Term] = lzy(frequency((7, applicationGen), (2, atomGen)))

}
