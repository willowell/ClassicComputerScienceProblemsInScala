package core.control.io

import core.control.monad.Monad

case class IO[A](
  unsafeRunSync: () => A
)

object IO {
  def create[A](a: => A): IO[A] = IO(() => a)
}

given ioMonad: Monad[IO] with {
  override def pure[A](a: A): IO[A] = IO(() => a)
  override def flatMap[A, B](ma: IO[A])(f: A => IO[B]): IO[B] =
    IO(() => f(ma.unsafeRunSync()).unsafeRunSync())
}
