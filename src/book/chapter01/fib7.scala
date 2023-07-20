/// # Fibonacci using FS2 Stream
///
/// This fibonacci program uses a lazy, infinite list of fibonacci numbers, which are generated functionally.
///
/// While fib6 uses Scala's built-in LazyList, this version uses FS2's Stream.
///
/// Please note that this is a contrived example - Stream shines dealing with incremental I/O.
///
package book.chapter01.fib7

import core.data.time.*

import scala.collection.immutable.LazyList

import fs2.{Pure, Stream}

@main def run(): Unit = {
  val limit = 40

  lazy val fibs: Stream[Pure, Int] = Stream(0) ++ fibs.scan(1) { (prev, curr) => prev + curr }

  val xs = time {

    fibs
      .take(limit)
      /*
        Please note we can use `.toList` here since this is a pure stream.
        If this was an effectful stream, we would have to compile it first.
      */
      .toList
  }

  println("Results of fib:")

  for ((x, i) <- xs.toList.zipWithIndex) {
    println(s"fib(${i + 1}): $x")
  }
}
