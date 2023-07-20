package core.control.monad

trait Monad[M[_]] {
  def pure[A](a: A): M[A]
  def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
}

object Monad {
  def apply[M[_]](using monad: Monad[M]): Monad[M] = monad
}
