/// # Naive fibonacci
///
/// This fibonacci program terminates but is very slow (more than 1000 ms)
/// since it doesn't keep repeated intermediate values.
///
/// For fun, this program demonstrates two different fibonacci implementations:
/// * with `match`
/// * with `if`.
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter1/Fib2.java
///
package book.chapter01.fib2

import core.data.time._

def fib(x: Int): Int = x match
  case 0 | 1 => x
  case _     => fib(x - 2) + fib(x - 1)

def fibIfElse(x: Int): Int =
  if x < 2 then
    x
  else
    fibIfElse(x - 2) + fibIfElse(x - 1)

@main def run(): Unit =
  val range = 1 to 40

  {
    val xs = time { range map fib }

    println("Results of fib, using match:")

    for (x, i) <- xs.toList.zipWithIndex do
      println(s"fib(${i + 1}): $x")
  }

  {
    val xs = time { range map fibIfElse }

    println("Results of fib, using if statements:")

    for (x, i) <- xs.toList.zipWithIndex do
      println(s"fib(${i + 1}): $x")
  }

