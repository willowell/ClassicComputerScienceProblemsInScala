package core.control.combinators

extension [A](a: A) {
  /**
    * Pipe a value into a function `f`.
    *
    * @param B
    * @param f
    * @return
    */
  def |>[B](f: A => B): B = f(a)
}
