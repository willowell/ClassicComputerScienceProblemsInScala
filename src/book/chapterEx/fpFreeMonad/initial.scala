package book.chapterEx.fpFreeMonad.initial

import core.control.free.Free
import core.control.io.{IO, ioMonad}
import core.control.natTrans.{~>}

sealed trait Algebra[A]
case class Add [A] (left: A, right: A) extends Algebra[A]
case class Sub [A] (left: A, right: A) extends Algebra[A]
case class Mul [A] (left: A, right: A) extends Algebra[A]
case class Div [A] (left: A, right: A) extends Algebra[A]
case class Grp [A] (inner: A)          extends Algebra[A]
case class Num [A] (value: A)          extends Algebra[A]

type Expr[A] = Free[Algebra, A]

def add[A](left: Expr[A], right: Expr[A]) = Free.liftF(Add(left, right))

def sub[A](left: Expr[A], right: Expr[A]) = Free.liftF(Sub(left, right))

def mul[A](left: Expr[A], right: Expr[A]) = Free.liftF(Mul(left, right))

def div[A](left: Expr[A], right: Expr[A]) = Free.liftF(Div(left, right))

def grp[A](inner: A): Expr[A] = Free.liftF(Grp(inner))

def num[A](value: A): Expr[A] = Free.liftF(Num(value))

def program0: Expr[Double] = for {
  res <- num(2.0)
} yield res

def program1: Expr[Double] = for {
  inner <- grp(num(2.0))
  res <- inner
} yield res

@main def run(): Unit = {
  
}
