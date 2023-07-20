/// # FP Approach using a Tagless Final Algebra
///
///
///
///
package book.chapterEx.fpTaglessFinal.newExpression

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

trait ModuloAlgebra[E[_]] {                              // NEW
  def mod (left: E[Double], right: E[Double]): E[Double] // NEW
}                                                        // NEW

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

given moduloAlg: ModuloAlgebra[Expr] with {                                                                        // NEW
  override def mod(left: Expr[Double], right: Expr[Double]): Expr[Double] = Expr[Double](left.value % right.value) // NEW
}                                                                                                                  // NEW

def program[E[_]](using
  alg: Algebra[E],
  modAlg: ModuloAlgebra[E], // NEW
): E[Double] = {
  import alg.*, modAlg.*    // NEW

  mod(
    num(4),
    mul(
      num(2),
      grp(
        add(
          num(5),
          num(3)
        )
      )
    )
  )
}

@main def run(): Unit = {
  println(program[Expr].value)
}
