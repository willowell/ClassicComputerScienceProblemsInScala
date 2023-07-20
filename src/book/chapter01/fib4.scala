/// # Iterative fibonacci
///
/// This fibonacci program uses an iterative approach rather than recursion.
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter1/Fib4.java
///
package book.chapter01.fib4

import core.data.time.*

def fib(x: Int): Int = {
  var last = 0
  var next = 1

  var i = 0

  while (i < x) {
    val oldLast = last
    last = next
    next = oldLast + next

    i = i + 1
  }

  last
}


@main def run(): Unit = {
  val range = 0 until 40

  val xs = time { range.map(fib) }

  println("Results of fib:")

  for ((x, i) <- xs.toList.zipWithIndex) {
    println(s"fib(${i + 1}): $x")
  }
}
  