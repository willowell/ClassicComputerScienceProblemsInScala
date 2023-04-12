/// # Fibonacci with simple memoization
///
/// This version expands on `fib2.scala` by using a `HashMap` to memoize intermediate values.
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter1/Fib3.java
///
package book.chapter01.fib3

import core.data.time._

import scala.collection.mutable.HashMap

def fibMemo(x: Int)(using cache: HashMap[Int, Int]): Int =
  val possibleCacheEntry = cache.get(x)

  possibleCacheEntry match
    case Some(entry) => entry
    case None =>
      val newValue = x match
        case 0 | 1 => x
        case n     => fibMemo(n - 1) + fibMemo(n - 2)
      
      val _ = cache.put(x, newValue)

      newValue

@main def run(): Unit =
  val range = 1 to 40

  given memoMap: HashMap[Int, Int] = HashMap[Int, Int](0 -> 0, 1 -> 1)

  val xs = time { range map fibMemo }

  println("Results of fib, using memoization with a hash map:")

  for (x, i) <- xs.toList.zipWithIndex do
    println(s"fib(${i + 1}): $x")
  