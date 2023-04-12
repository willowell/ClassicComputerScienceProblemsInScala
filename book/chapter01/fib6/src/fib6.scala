/// # Fibonacci using LazyList, FP Style
///
/// This fibonacci program uses a lazy, infinite list of fibonacci numbers, which are generated functionally.
///
/// Whereas fib5 uses an iterative approach as in fib4, this example uses LazyList's API to generate the list in a more functional style.
/// As a matter of fact, I based this off of the example in https://www.scala-lang.org/api/3.2.2/scala/collection/immutable/LazyList.html !
///
package book.chapter01.fib4

import core.data.time._

import scala.collection.immutable.LazyList

@main def run(): Unit =
  val limit = 40

  lazy val fibs: LazyList[Int] =
    0 #:: 1 #:: fibs.zip(fibs.tail).map { (prev, curr) => prev + curr }

  val xs = fibs.take(limit)

  println("Results of fib:")

  for (x, i) <- xs.toList.zipWithIndex do
    println(s"fib(${i + 1}): $x")
  