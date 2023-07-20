/// # Eight Queens
///
/// While implementing this example, I discovered a silly bug with my implementation of the CSP class:
/// I underestimated the importance of this null check on the `result` of the backtracking search: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter3/CSP.java#L76-L78
///
/// Since my `backtracking_search()` returns an `Option`, I just returned `result` directly since it fit the type.
/// However, I was getting some weird results with this example - the backtracking search never went backwards!
/// By the time it got to the 6th queen, it could not come up with a solution.
///
/// Checking the Swift implementation, I realized this error because it uses an `if let` on the result: https://github.com/davecom/ClassicComputerScienceProblemsInSwift/blob/master/Classic%20Computer%20Science%20Problems%20in%20Swift.playground/Pages/Chapter%203.xcplaygroundpage/Contents.swift#L89-L91.
///
/// Somehow, this bug did not surface during the Australian map colouring problem!
///
/// Along the way, I went down the rabbit hole learning about Rust's BTreeMap, thinking maybe the unordered nature of HashMap was causing it.
/// Nope!
///
/// Other than that silly bug, this example is pretty straightforward.
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter3/QueensConstraint.java
///
package book.chapter03.eightQueens

import core.data.csp.{given, *}
import core.text.fansi.*
import core.text.prettyprinting.*
import core.text.prettyprinting.Ops.*

import scala.collection.mutable.{ArrayBuffer, HashMap}

type Board = HashMap[Int, Int]

case class QueensConstraint(
  columns: ArrayBuffer[Int],
) extends Constraint[Int, Int] {
  override val variables: Vector[Int] = columns.toVector

  override def isSatisfied(assignment: HashMap[Int, Int]): Boolean = {
    import scala.math.abs
    import scala.util.boundary, boundary.break

    val blockRes = boundary {
      for ((q1c, q1r) <- assignment) {
        for (q2c <- (q1c + 1) to (variables.length)) {
          assignment.get(q2c).foreach { q2r => 
            if (q1r == q2r) {
              break(false)
            }

            if (abs(q1r - q2r) == abs(q1c - q2c)) {
              break(false)
            }
          }
        }
      }
    }

    blockRes match {
      case b: Boolean => b
      case () => true
    }
  }
}

def displayBoard(board: Board): Unit = {
  for (row <- 1 to 8) {
    for (col <- 1 to 8) {
      val qr = board.get(col)

      val queenChar = qr match {
        case Some(r) => {
          if r == row then y("Q") else " "
        }
        case None => {
          " "
        }
      }

      print(s"${dgr("[")}$queenChar${dgr("]")}")
    }
    println()
  }
}

@main def run(): Unit = {
  val columns = ArrayBuffer(1, 2, 3, 4, 5, 6, 7, 8)

  var rows = HashMap[Int, ArrayBuffer[Int]]()

  for (column <- columns) {
    rows.addOne(column -> columns.clone)
  }

  val possibleCsp = CSP[Int, Int, QueensConstraint](columns, rows)

  possibleCsp.foreach { csp =>
    val _ = csp.addConstraint(QueensConstraint(columns))

    val possibleSolution = csp.backtrackingSearch

    possibleSolution match {
      case Some(solution) => {
        println("Found solution:")
        displayBoard(solution)
      }
      case None => {
        println("No solution found :-(")
      }
    }
  }
}
