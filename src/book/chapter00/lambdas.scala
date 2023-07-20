/// # How to Write Lambdas in Scala 3
///
///
package book.chapter00.lambdas

import core.control.combinators.*

@main def run(): Unit = {
  val xs = (1 to 10).toVector

  //==================================================================================================================//
  // Passing Functions as Arguments                                                                                   //
  //==================================================================================================================//

  def f(x: Int) = x * 2

  val _ = xs.map(f)

  //==================================================================================================================//
  // Arrow Lambdas                                                                                                    //
  //==================================================================================================================//

  val _ = xs.map((x: Int) => x * 2)

  val _ = xs.flatMap((x: Int) => 
    Vector.fill(x)(x)
  )

  val _ = xs.flatMap((x: Int) => 
    Vector.fill(x){ x }
  )

  val _ = xs.map({ (x: Int) => x * 2 })

  val _ = xs.map(x => x * 2)

  // With a colon, the syntax rules are a little weird...
  // This one is OK:
  val _ = xs.map:
    x => x * 2
  
  // This one is also OK
  val _ = xs.map: x =>
    x * 2
  
  // But this one is NOT OK
  //val _ = xs.map: x => x * 2

  //==================================================================================================================//
  // Short-Form Lambas                                                                                                //
  //==================================================================================================================//

  /*
    You can express unary functions even more tersly by instead just passing the function's body, with `_` where the arg goes.

    This mirrors a very common Haskell pattern that relies on partial application, where you can omit one of a binary function's args,
    and it will be automatically supplied via partial application.
  */

  // In Haskell, this is `map (*2) xs`
  val _ = xs.map(_ * 2)

  // You can also give a type annotation!
  val _ = xs.map((_: Int) * 2)

  // Brackets also work here.
  val _ = xs.map({_ * 2})

  val _ = xs.map { _ * 2 }


  val _ = xs.flatMap { x =>
    Vector.fill(x){ x }
  }

  val _ = xs.flatMap: x =>
    Vector.fill(x){ x }

  //==================================================================================================================//
  // Chaining Methods and Lambdas                                                                                     //
  //==================================================================================================================//

  // In some cases, the `.` can be omitted

  val _ = xs map { _ * 2 }

  // And you can even chain methods together without the `.`!

  val _ = xs.map{ _ * 2 }.map{ _ + 2 }

  val _ = xs
    .map { _ * 2 }
    .map { _ + 2 }

  val _ = xs map { _ * 2 } map { _ + 2 }

  val _ = xs map (_ * 2) map (_ + 2)

  // This is a compiler error, unfortunately!
  // Says `map` cannot be found - Seems like the compiler does not see that we would like to apply `map` here
  // It thinks `val _ = xs` is all we meant!
  /*
  val _ = xs
    map { _ * 2 }
    map { _ + 2 }
  */

  // Adding parentheses fixes the error but is inelegant.
  val _ = (xs
    map { _ * 2 }
    map { _ + 2 }
  )

  // Instead, it's probably best to just use the `.`-version here.
  val _ = xs
    .map { _ * 2 }
    .map { _ + 2 }

  // Scala's type inference currently has a hard time with expressions like this,
  // so you may need to add a type annotation like below.
  val _ = xs map ({ (_: Int) + 2 } compose { _ * 2 })

  val _ = xs
    |> { _.map { _ * 2 } }
    |> { _.map { _ + 2 } }
  
  //==================================================================================================================//
  // What do I prefer?                                                                                                //
  //==================================================================================================================//

  /*
    For short, unary functions or method calls, I prefer short-form lambdas with brackets,
    split over multiple lines with prepended `.`, like so:
  */
  val _ = xs
    .map { _ * 2 }
    .map { _ + 2 }
  
  /*
    For short, inline lambdas, I prefer using the bracket form above if they are the LAST argument.
    Otherwise, I surround them with parantheses, like so:
  */
  val _ = xs.map({ _ * 2 }).toString

  /*
    For call-by-name arguments, I prefer using the bracket form, like so:
  */
  val _ = List.fill(3) { 0 }

  val _ = List.fill(3) { 0 } .toArray

  val _ = List.fill(10) {
    import scala.util.Random

    Random.nextInt()
  }
}
