/// # Send More Money
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter3/SendMoreMoneyConstraint.java
///
package book.chapter03.sendMoreMoney

import core.data.csp.{given, *}
import core.text.fansi.*
import core.text.prettyprinting.*
import core.text.prettyprinting.Ops.*

import scala.collection.mutable.{ArrayBuffer, HashMap, HashSet}
import scala.collection.mutable

case class SendMoreMoneyConstraint(
  letters: ArrayBuffer[Char],
) extends Constraint[Char, Int] {
  override val variables: Vector[Char] = letters.toVector

  override def isSatisfied(assignment: mutable.HashMap[Char, Int]): Boolean = {
    import scala.util.boundary, boundary.break

    // If there are duplicate values, then it's not a solution.
    val assignmentAsSet = HashSet.from(assignment.values)

    if (assignmentAsSet.size < assignment.size) {
      return false
    }

    // If all variables have been assigned, check if it adds correctly.
    val blockResult = boundary {
      if (assignment.size == letters.length) {
        val addsCorrectly = for {
          s <- assignment.get('S')
          e <- assignment.get('E')
          n <- assignment.get('N')
          d <- assignment.get('D')
          m <- assignment.get('M')
          o <- assignment.get('O')
          r <- assignment.get('R')
          y <- assignment.get('Y')

          send = s * 1000 + e * 100 + n * 10 + d
          more = m * 1000 + o * 100 + r * 10 + e
          money = m * 10000 + o * 1000 + n * 100 + e * 10 + y

        } yield (send + more == money)

        addsCorrectly.foreach { b =>
          break(b)
        }
      }
    }

    blockResult match {
      case b: Boolean => b
      case () => {
        // No conflicts
        true
      }
    }
  }
}

@main def run(): Unit = {
  val letters = ArrayBuffer('S', 'E', 'N', 'D', 'M', 'O', 'R', 'Y')

  var possibleDigits = HashMap[Char, ArrayBuffer[Int]]()

  for (letter <- letters) {
    possibleDigits.addOne(letter -> ArrayBuffer(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))
  }

  possibleDigits.update('M', ArrayBuffer(1))

  val csp = CSP[Char, Int, SendMoreMoneyConstraint](letters, possibleDigits)

  csp.foreach { cspRight =>
    val _ = cspRight.addConstraint(SendMoreMoneyConstraint(letters))

    println(display(cspRight))

    println("\n\n")

    println("Performing a backtracking search...")

    val searchResult = cspRight.backtrackingSearch

    searchResult match {
      case Some(solution) => {
        println("Found solution:")

        for ((k, v) <- solution) {
          println(s"$k => $v")
        }
      }
      case None => {
        println("No solution found :-(")
      }
    }
  }
}
