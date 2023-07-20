/// # Fibonacci with simple memoization
///
/// This version expands on `fib2.scala` by using a `HashMap` to memoize intermediate values.
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter1/Fib3.java
///
/// ## What's `given` and `using`?
///
/// Scala enables passing arguments implicitly to methods and functions.
/// Scala will look for a `given` value with the required type and use it for the implicit parameter.
/// The trick here is that there can be ONLY ONE `given` for that type - sort-of like how you can only
/// have one instance of a type class for a given type in Haskell.
///
/// So, in the example below, `using cache: HashMap[Int, Int]` says that this function uses an implicit parameter.
/// `given memoMap: HashMap[Int, Int] = ...` creates the implicit argument, and then Scala passes it in.
///
/// In this example, this means I can use `fibMemo` as if it were a unary function with `map`, while still memoizing the values.
///
/// Of course, we can always pass an explicit argument where an implicit is expected.
///
/// While this is a contrived example, implicits are very powerful, and together with type classes, allow us to implement
/// Haskell-style ad-hoc polymorphism, AKA parametric polymorphism.
///
/// You'll see this come into play in `chapterEx`, which covers the Expression Problem!
///
package book.chapter01.fib3

import core.data.time.*

import scala.collection.mutable.HashMap

def fibMemo(x: Int)(using cache: HashMap[Int, Int]): Int = {
  val possibleCacheEntry = cache.get(x)

  possibleCacheEntry match {
    case Some(entry) => entry
    case None => {
      val newValue = x match {
        case 0 | 1 => x
        case n     => fibMemo(n - 1) + fibMemo(n - 2)
      }
      
      val _ = cache.put(x, newValue)

      newValue
    }
  }
}

@main def run(): Unit = {
  val range = 0 until 40

  given memoMap: HashMap[Int, Int] = HashMap[Int, Int](0 -> 0, 1 -> 1)

  val xs = time { range.map(fibMemo) }

  // alternatively, passing the implicit explicitly:
  // val xs = time { range.map(fibMemo(_)(using memoMap)) }

  println("Results of fib, using memoization with a hash map:")

  for ((x, i) <- xs.toList.zipWithIndex) {
    println(s"fib(${i + 1}): $x")
  }
}
  