/// # OOP Approach using Extension Methods
///
///
///
///
package book.chapterEx.oopExtensions.initial

trait Expr

trait Eval {
  def eval: Double
}

case class Num (value: Double)           extends Expr
case class Add (left: Expr, right: Expr) extends Expr
case class Sub (left: Expr, right: Expr) extends Expr
case class Mul (left: Expr, right: Expr) extends Expr
case class Div (left: Expr, right: Expr) extends Expr
case class Grp (inner: Expr)             extends Expr

// extension (n: Num) def eval: Double = n.value
// extension (a: Add) def eval: Double = a.left.eval + a.right.eval
// extension (s: Sub) def eval: Double = s.left.eval - s.right.eval
// extension (m: Mul) def eval: Double = m.left.eval * m.right.eval
// extension (d: Div) def eval: Double = d.left.eval / d.right.eval
// extension (g: Grp) def eval: Double = g.inner.eval

def eval(expr: Expr & Eval): Double = expr.eval

@main def run(): Unit = {
  val expression: Expr = Add(
    Grp(Add(Num(2.0), Num(2.0))),
    Num(6.0)
  )

  // val doubleVal = eval(expression)

  // println(s"Result: $doubleVal")
}
