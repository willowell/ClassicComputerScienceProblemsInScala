/// # Calculate pi
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter1/PiCalculator.java
///
package book.chapter01.calculatePi

import core.data.time.*

import scala.annotation.tailrec
import scala.math.*

def calculatePi(numTerms: Int): Double = {
  val numerator: Double = 4.0

  var denominator: Double = 1.0
  var operation: Double = 1.0
  var pi: Double = 0.0

  for (_ <- 0 to numTerms) {
    pi = pi + operation * (numerator / denominator)
    denominator = denominator + 2.0
    operation = operation * -1.0
  }

  pi
}

/*
  Since `calculatePi` is a pretty simple loop,
  we can rework it to be recursive:
*/

def calculatePiRec(numTerms: Int): Double = {
  val numerator: Double = 4.0

  @tailrec
  def go(n: Int, pi: Double, denominator: Double, operation: Double): Double = {
    if (n == 0) {
      pi
    } else {
      go(
        n - 1,
        pi + operation * (numerator / denominator),
        denominator + 2.0,
        operation * -1.0
      )
    }
  }

  go(
    n = numTerms + 1,
    pi = 0.0,          // Starting pi value
    denominator = 1.0, // Starting denominator value
    operation = 1.0,   // Starting operation value; gets flipped between -1.0 and +1.0.
  )
}

@main def run(): Unit = {
  println("Let's make some pi!")

  val powersOfTen = Vector.iterate(1, 10) { _ * 10 }

  val xs = time { powersOfTen.map { calculatePi } }

  val result = xs.zip(powersOfTen)

  for (((x, y), i) <- result.toList.zipWithIndex) {
    println(f"${y}%-10d iterations, result: ${x}%-15.12f, difference: ${x / Pi}%-15.12f")
  }

  println("Your pi is ready! 😋")
}
