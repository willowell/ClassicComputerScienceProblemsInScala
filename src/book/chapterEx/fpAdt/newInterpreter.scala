/// # FP Approach using an Algebraic Data Type
///
/// This approach does not solve the expression problem because, while it is easy to add new functions that operate on the ADT
/// via `match`, we cannot modify the ADT without also modifying every function that uses it.
///
package book.chapterEx.fpAdt.newInterpreter

enum Expr {
  case Add   (left: Expr, right: Expr)
  case Sub   (left: Expr, right: Expr)
  case Mul   (left: Expr, right: Expr)
  case Div   (left: Expr, right: Expr)
  case Group (inner: Expr)
  case Num   (value: Double)
}

def eval(expr: Expr): Double = {
  import Expr.*
  expr match {
    case Add(left: Expr, right: Expr) => eval(left) + eval(right)
    case Sub(left: Expr, right: Expr) => eval(left) - eval(right)
    case Mul(left: Expr, right: Expr) => eval(left) * eval(right)
    case Div(left: Expr, right: Expr) => eval(left) / eval(right)
    case Group(inner: Expr)           => eval(inner)
    case Num(value: Double)           => value
  }
}

def show(expr: Expr): String = {                                           // NEW
  import Expr.*                                                            // NEW
  expr match {                                                             // NEW
    case Add(left: Expr, right: Expr) => s"${show(left)} + ${show(right)}" // NEW
    case Sub(left: Expr, right: Expr) => s"${show(left)} - ${show(right)}" // NEW
    case Mul(left: Expr, right: Expr) => s"${show(left)} * ${show(right)}" // NEW
    case Div(left: Expr, right: Expr) => s"${show(left)} / ${show(right)}" // NEW
    case Group(inner: Expr)           => show(inner)                       // NEW
    case Num(value: Double)           => value.toString                    // NEW
  }                                                                        // NEW
}                                                                          // NEW

@main def run(): Unit = {
  import Expr.*

  val expr = Add(Num(23.0), Num(42.0))

  println(expr)
  println(eval(expr))
}
