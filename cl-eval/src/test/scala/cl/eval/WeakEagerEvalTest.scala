package cl.eval

import cl.lang.CLCompiler
import cl.eval.Eval.Out
import org.scalatest.{EitherValues, Matchers, OptionValues, WordSpec}

class WeakEagerEvalTest extends WordSpec with Matchers with EitherValues with OptionValues {

  val (x, y, z) = (cl.Var('x'), cl.Var('y'), cl.Var('z'))

  "x(NM) ~~> Unbound Ref N!" in {
    import cl.abstraction.Abstraction.Implicits.eta
    val ast = CLCompiler("x(NM)")
    ast should be('right)
    val result = Eval.weakEagerEval(ast.right.get)(Env.pure, eta)
    result            should be('left)
    result.left.value shouldEqual UnboundRefError("N")
  }

  "B:=x; B:=Sy ~~> Ref B is already bound!)" in {
    import cl.abstraction.Abstraction.Implicits.eta
    val first  = CLCompiler("B := x")
    val second = CLCompiler("B := y")
    first  should be('right)
    second should be('right)
    val Out(_, newEnv) = Eval.weakEagerEval(first.right.get)(Env.pure, eta).right.get
    val result         = Eval.weakEagerEval(second.right.get)(newEnv, eta)

    result            should be('left)
    result.left.value shouldEqual RefRebindError("B")
  }

  "B:=S(KS)K; Bxyz ~~> x(yz)" in {
    import cl.abstraction.Abstraction.Implicits.eta
    val first  = CLCompiler("B:=S(KS)K")
    val second = CLCompiler("Bxyz")
    first  should be('right)
    second should be('right)
    val Out(_, newEnv) = Eval.weakEagerEval(first.right.get)(Env.pure, eta).right.get
    val Out(result, _) = Eval.weakEagerEval(second.right.get)(newEnv, eta).right.get

    result       should be('defined)
    result.value shouldEqual (x $ y(z))
  }

}
