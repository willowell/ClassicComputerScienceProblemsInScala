/// # OOP Approach using the Visitor Pattern
///
/// This approach solves the expression problem from an OOP perspective by encapsulating the functionality inside Visitor classes.
/// In other words, instead of having an `eval` method on each `Expr`, we can instead create an `Interpreter` Visitor that traverses
/// the AST, evaluating each expression.
///
/// As we add new kinds of expressions, we can adjust the `Visitor`s in turn.
///
/// In essence, as you can see from the `match` expressions below, each `Visitor` internally approximates ad-hoc polymorphism.
///
package book.chapterEx.oopVisitor.initial

trait Visitor[R] {
  def visit(v: Visitable): R
}

trait Visitable {
  def accept[R](visitor: Visitor[R]): R = visitor.visit(this)
}

type Expr = Visitable

case class Add (left: Expr, right: Expr) extends Visitable
case class Sub (left: Expr, right: Expr) extends Visitable
case class Mul (left: Expr, right: Expr) extends Visitable
case class Div (left: Expr, right: Expr) extends Visitable
case class Grp (inner: Expr)             extends Visitable
case class Num (value: Double)           extends Visitable

class Interpreter extends Visitor[Double] {
  def eval(expr: Expr): Double = expr.accept(this)

  override def visit(v: Visitable): Double = v match {
    case Add(left: Expr, right: Expr) => visit(left) + visit(right)
    case Sub(left: Expr, right: Expr) => visit(left) - visit(right)
    case Mul(left: Expr, right: Expr) => visit(left) * visit(right)
    case Div(left: Expr, right: Expr) => visit(left) / visit(right)
    case Grp(inner: Expr)             => visit(inner)
    case Num(value: Double)           => value
  }
}

@main def run(): Unit = {
  val expression = Add(
    Grp(Add(Num(2.0), Num(2.0))),
    Num(6.0)
  )

  val doubleVal = Interpreter().eval(expression)
}
