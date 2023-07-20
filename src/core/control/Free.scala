package core.control.free

import core.control.monad.Monad
import core.control.natTrans.~>

trait Free[M[_], A] {
  import Free.*

  def flatMap[B](f: A => Free[M, B]): Free[M, B] = FlatMap(this, f)

  def map[B](f: A => B): Free[M, B] = flatMap(a => pure(f(a)))

  def foldMap[G[_]: Monad](natTrans: M ~> G): G[A] = this match {
    case Pure(a: A)                                    => Monad[G].pure(a)
    case Suspend(ma: M[A])                             => natTrans.apply(ma)
    case FlatMap(fa: Free[M, A], f: (A => Free[M, A])) =>
      Monad[G]
        .flatMap
          (fa.foldMap(natTrans))
          (a => f(a).foldMap(natTrans))
  }
}

object Free {
  def pure  [M[_], A] (a: A):     Free[M, A] = Pure(a)
  def liftF [M[_], A] (ma: M[A]): Free[M, A] = Suspend(ma)

  case class Pure    [M[_], A]    (a: A)                               extends Free[M, A]
  case class FlatMap [M[_], A, B] (fa: Free[M, A], f: A => Free[M, B]) extends Free[M, B]
  case class Suspend [M[_], A]    (ma: M[A])                           extends Free[M, A]
}
