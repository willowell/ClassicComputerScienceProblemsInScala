/// # FP Approach using a Tagless Final Algebra
///
///
///
///
package book.chapterEx.fpTaglessFinal.newInterpreter

//
// ALGEBRA
//

trait Algebra[E[_]] {
  def num (num: Double): E[Double]

  def add (left: E[Double], right: E[Double]): E[Double]
  def sub (left: E[Double], right: E[Double]): E[Double]
  def mul (left: E[Double], right: E[Double]): E[Double]
  def div (left: E[Double], right: E[Double]): E[Double]

  def grp (inner: E[Double]): E[Double]
}

case class Expr[A](value: A)

//
// INTERPRETERS
//

given exprAlg: Algebra[Expr] with {
  override def num (num: Double): Expr[Double] = Expr(num)

  override def add (left: Expr[Double], right: Expr[Double]): Expr[Double] = Expr(left.value + right.value)
  override def sub (left: Expr[Double], right: Expr[Double]): Expr[Double] = Expr(left.value - right.value)
  override def mul (left: Expr[Double], right: Expr[Double]): Expr[Double] = Expr(left.value * right.value)
  override def div (left: Expr[Double], right: Expr[Double]): Expr[Double] = Expr(left.value / right.value)

  override def grp (inner: Expr[Double]): Expr[Double] = Expr(inner.value)
}

def program[E[_]](using alg: Algebra[E]): E[Double] = {
  import alg.*

  add(num(4), mul(num(2), grp(add(num(5), num(3)))))
}

@main def run(): Unit = {
  println(program[Expr].value)
}
