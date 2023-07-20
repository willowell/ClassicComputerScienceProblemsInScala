/// # Fibonacci using LazyList, Iterative Style
///
/// This fibonacci program uses a lazy, infinite list of fibonacci numbers, which are generated iteratively.
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter1/Fib5.java
///
package book.chapter01.fib5

import core.data.time.*

import scala.collection.immutable.LazyList

@main def run(): Unit = {
  val limit = 40

  var last = 0
  var next = 1

  lazy val fibs: LazyList[Int] = LazyList.iterate(0) { _ =>
    val oldLast = last
    last = next
    next = oldLast + next
    last
  }

  val xs = time { fibs.take(limit) }

  println("Results of fib:")

  for ((x, i) <- xs.toList.zipWithIndex) {
    println(s"fib(${i + 1}): $x")
  }
}
