package book.chapterEx.oopSubTypes.initial

abstract class Expr {
  def eval: Double
}

class Num(value: Double) extends Expr {
  override def eval: Double = value
}

class Add(left: Expr, right: Expr) extends Expr {
  override def eval: Double = left.eval + right.eval
}

class Sub(left: Expr, right: Expr) extends Expr {
  override def eval: Double = left.eval - right.eval
}

class Mul(left: Expr, right: Expr) extends Expr {
  override def eval: Double = left.eval * right.eval
}

class Div(left: Expr, right: Expr) extends Expr {
  override def eval: Double = left.eval / right.eval
}

class Grp(inner: Expr) extends Expr {
  override def eval: Double = inner.eval
}

@main def run(): Unit = {
  val expression: Expr = Add(
    Grp(Add(Num(2.0), Num(2.0))),
    Num(6.0)
  )

  val doubleVal = expression.eval
  
  println(s"Result: $doubleVal")
}
