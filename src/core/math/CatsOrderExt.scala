package core.math.catsorderext

import cats.Order
import cats.kernel.Comparison

extension[A: Order](lhs: A) {
  /** # Spaceship Operator!
    * Compare this and `rhs`, returning a `cats.kernel.Comparison`.
    * 
    * This is akin to Rust's `cmp` or Haskell's `compare`.
    *
    * @param rhs value to compare against
    * @return
    * 
    * @see `cats.Order` - wraps `Order[A].comparison`
    */
  def <=>(rhs: A): Comparison = Order[A].comparison(lhs, rhs)
}
