package book.chapterEx.oopSubTypes.newInterpreter

abstract class Expr {
  def eval: Double
  def show: String                                                // NEW
}

class Num(value: Double) extends Expr {
  override def eval: Double = value
  override def show: String = value.toString                      // NEW
}

class Add(left: Expr, right: Expr) extends Expr {
  override def eval: Double = left.eval + right.eval
  override def show: String = s"(${left.show}) + (${right.show})" // NEW
}

class Sub(left: Expr, right: Expr) extends Expr {
  override def eval: Double = left.eval - right.eval
  override def show: String = s"(${left.show}) - (${right.show})" // NEW
}

class Mul(left: Expr, right: Expr) extends Expr {
  override def eval: Double = left.eval * right.eval
  override def show: String = s"(${left.show}) * (${right.show})" // NEW
}

class Div(left: Expr, right: Expr) extends Expr {
  override def eval: Double = left.eval / right.eval
  override def show: String = s"(${left.show}) / (${right.show})" // NEW
}

class Grp(inner: Expr) extends Expr {
  override def eval: Double = inner.eval
  override def show: String = s"(${inner.show})"                  // NEW
}

@main def run(): Unit = {
  val expression: Expr = Add(
    Grp(Add(Num(2.0), Num(2.0))),
    Num(6.0)
  )
  
  val stringRepr = expression.show

  val doubleVal = expression.eval
  
  println(stringRepr)
  println(s"Result: $doubleVal")
}
