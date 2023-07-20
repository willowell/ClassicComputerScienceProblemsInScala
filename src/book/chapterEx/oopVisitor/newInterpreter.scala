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
package book.chapterEx.oopVisitor.newInterpreter

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
    case Grp(inner: Expr) =>             visit(inner)
    case Num(value: Double) =>           value
  }
}

class ASTPrinter extends Visitor[String] {                                // NEW
  def print(expr: Expr): String = expr.accept[String](this)               // NEW
                                                                          // NEW
  override def visit(v: Visitable): String = v match {                    // NEW
    case Add(left: Expr, right: Expr) => parenthesize("ADD", left, right) // NEW
    case Sub(left: Expr, right: Expr) => parenthesize("SUB", left, right) // NEW
    case Mul(left: Expr, right: Expr) => parenthesize("MUL", left, right) // NEW
    case Div(left: Expr, right: Expr) => parenthesize("DIV", left, right) // NEW
    case Grp(inner: Expr)             => parenthesize("GROUP", inner)     // NEW
    case Num(value: Double)           => value.toString                   // NEW
  }                                                                       // NEW
                                                                          // NEW
  private def parenthesize(name: String, exprs: Expr*): String = {        // NEW
    var builder: StringBuilder = new StringBuilder()                      // NEW
                                                                          // NEW
    builder.append("(").append(name)                                      // NEW
                                                                          // NEW
    for (expr: Expr <- exprs) {                                           // NEW
      builder.append(" ")                                                 // NEW
                                                                          // NEW
      builder.append(expr.accept[String](this))                           // NEW
    }                                                                     // NEW
                                                                          // NEW
    builder.append(")")                                                   // NEW
                                                                          // NEW
    builder.toString                                                      // NEW
  }                                                                       // NEW
}                                                                         // NEW

@main def run(): Unit = {
  val expression = Add(
    Grp(Add(Num(2.0), Num(2.0))),
    Num(6.0)
  )
  
  val stringRepr = ASTPrinter().print(expression)

  val doubleVal = Interpreter().eval(expression)
  
  println(stringRepr)
}
