/// # Function Fixity and Application in Scala 3
///
package book.chapter00.fixity

extension (a: Int)
  def add(b: Int) = a + b

extension (a: Int)
  infix def add2(b: Int) = a + b

@main def run(): Unit = {
  val xs = (1 to 10).toList

  val a = xs.map { _ * 2 }

  val b = xs map { _ * 2 }

  val b2 = xs map { _ * 2 } map { _ + 2 } 

  val c = xs `map` { _ * 2 }



  val d = 1.add(2)

  val e = 1 `add` 2

  // Compiler error!
  /*
    Alphanumeric method add is not declared infix; it should not be used as infix operator.
    Instead, use method syntax .add(...) or backticked identifier `add`.
    The latter can be rewritten automatically under -rewrite -deprecation.
  */
  //val f = 1 add 2



  val g = 1.add2(2)

  val h = 1 `add2` 2
}
