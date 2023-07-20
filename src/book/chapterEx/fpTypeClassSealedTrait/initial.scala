/// # FP Approach using a Type Class
///
/// This approach solves the expression problem from an FP perspective
///
///
package book.chapterEx.fpTypeClassSealedTrait.initial

sealed trait Expr
case class Add [A <: Expr, B <: Expr] (left: A, right: B) extends Expr
case class Sub [A <: Expr, B <: Expr] (left: A, right: B) extends Expr
case class Mul [A <: Expr, B <: Expr] (left: A, right: B) extends Expr
case class Div [A <: Expr, B <: Expr] (left: A, right: B) extends Expr
case class Grp [A <: Expr]            (inner: A)          extends Expr
case class Num                        (value: Double)     extends Expr

//====================================================================================================================//
// SHOW TYPE CLASS                                                                                                    //
//====================================================================================================================//

trait Show[T] {
  def show(t: T): String
}

object Show {
  def apply[A: Show]: Show[A] = summon[Show[A]]
}

//====================================================================================================================//
// SHOW INSTANCES                                                                                                     //
//====================================================================================================================//

given showAdd[A <: Expr: Show, B <: Expr: Show]: Show[Add[A, B]] with {
  override def show(t: Add[A, B]): String =
    "(" + Show[A].show(t.left) + ") + (" + Show[B].show(t.right) + ")"
}

given showSub[A <: Expr: Show, B <: Expr: Show]: Show[Sub[A, B]] with {
  override def show(t: Sub[A, B]): String =
    "(" + Show[A].show(t.left) + ") - (" + Show[B].show(t.right) + ")"
}


given showMul[A <: Expr: Show, B <: Expr: Show]: Show[Mul[A, B]] with {
  override def show(t: Mul[A, B]): String =
    "(" + Show[A].show(t.left) + ") * (" + Show[B].show(t.right) + ")"
}

given showDiv[A <: Expr: Show, B <: Expr: Show]: Show[Div[A, B]] with {
  override def show(t: Div[A, B]): String =
    "(" + Show[A].show(t.left) + ") / (" + Show[B].show(t.right) + ")"
}

given showGrp[A <: Expr: Show]: Show[Grp[A]] with {
  override def show(t: Grp[A]): String = "(" + Show[A].show(t.inner) + ")"
}

given showNum: Show[Num] with {
  override def show(t: Num): String = t.value.toString
}

//====================================================================================================================//
// EVAL TYPE CLASS                                                                                                    //
//====================================================================================================================//

trait Eval[T] {
  def eval(t: T): Double
}

object Eval {
  def apply[A: Eval]: Eval[A] = summon[Eval[A]]
}

//====================================================================================================================//
// EVAL INSTANCES                                                                                                     //
//====================================================================================================================//

given evalAdd[A <: Expr: Eval, B <: Expr: Eval]: Eval[Add[A, B]] with {
  override def eval(t: Add[A, B]): Double = Eval[A].eval(t.left) + Eval[B].eval(t.right)
}

given evalSub[A <: Expr: Eval, B <: Expr: Eval]: Eval[Sub[A, B]] with {
  override def eval(t: Sub[A, B]): Double = Eval[A].eval(t.left) - Eval[B].eval(t.right)
}

given evalMul[A <: Expr: Eval, B <: Expr: Eval]: Eval[Mul[A, B]] with {
  override def eval(t: Mul[A, B]): Double = Eval[A].eval(t.left) * Eval[B].eval(t.right)
}

given evalDiv[A <: Expr: Eval, B <: Expr: Eval]: Eval[Div[A, B]] with {
  override def eval(t: Div[A, B]): Double = Eval[A].eval(t.left) / Eval[B].eval(t.right)
}

given evalGrp[A <: Expr: Eval]: Eval[Grp[A]] with {
  override def eval(t: Grp[A]): Double = Eval[A].eval(t.inner)
}

given evalNum: Eval[Num] with {
  override def eval(t: Num): Double = t.value
}

//====================================================================================================================//
// INTERPRETERS                                                                                                       //
//====================================================================================================================//

object Interpreters {
  def eval[T <: Expr: Eval](t: T): Double = Eval[T].eval(t)

  def show[T <: Expr: Show](t: T): String = Show[T].show(t)

  implicit class EvalOps[T: Eval](t: T) {
    def eval: Double = Eval[T].eval(t)
  }

  implicit class ShowOps[T: Show](t: T) {
    def show: String = Show[T].show(t)
  }
}

//====================================================================================================================//
// MAIN FUNCTION                                                                                                      //
//====================================================================================================================//

@main def run(): Unit = {
  import Interpreters.*
  
  val example                       = Add(Mul(Num(3), Num(2)), Num(7))
  val example2                         = Add(Mul(Num(3), Num(2)), Grp(Num(7)))
  
  println(eval(example))
  println(eval(example2))

  println(example.eval)
  println(example2.eval)
  println(example2.show)
}
