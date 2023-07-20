package core.control.natTrans

trait ~>[F[_], G[_]] {
  def apply[A](fa: F[A]): G[A]
}
