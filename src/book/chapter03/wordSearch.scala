/// # Word Search
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter3/WordSearchConstraint.java
///
package book.chapter03.wordSearch

import core.data.csp.{given, *}
import core.text.fansi.*
import core.text.prettyprinting.*
import core.text.prettyprinting.Ops.*

import scala.collection.mutable.{ArrayBuffer, HashMap, HashSet}
import scala.util.Random
import scala.collection.mutable

case class GridLocation(row: Int, col: Int)

case class WordGrid(
  rows: Int,
  columns: Int,
  var grid: ArrayBuffer[ArrayBuffer[Char]],
) {
  def randomlyFillCells(): Unit = {
    import WordGrid.{FIRST_LETTER, LAST_LETTER}

    val randomGrid = grid.map { row =>
      row.map { col =>
        Random.between(FIRST_LETTER, LAST_LETTER + 1).toChar
      }
    }

    grid = randomGrid
  }

  def markWord(word: String, locations: ArrayBuffer[GridLocation]): Unit = {
    var i = 0

    while (i < word.length) {
      val location = locations(i)

      grid(location.row)(location.col) = word(i)

      i = i + 1
    }
  }

  def generateDomain(word: String): ArrayBuffer[ArrayBuffer[GridLocation]] = {
    var domain = ArrayBuffer[ArrayBuffer[GridLocation]]()
    val wordLength = word.length

    for (row <- 0 until rows) {
      for (col <- 0 until columns) {
        if ((col + wordLength) <= columns) {
          WordGrid.generateRightLocations(row, col, wordLength).foreach { locs =>
            domain.append(locs)
          }

          if ((row + wordLength) <= rows) {
            WordGrid.generateDiagonalRightLocations(row, col, wordLength).foreach { locs =>
              domain.append(locs)
            }
          }
        }

        if ((row + wordLength) <= rows) {
          WordGrid.generateDownLocations(row, col, wordLength).foreach { locs =>
            domain.append(locs)
          }

          if ((col - wordLength) >= 0) {
            WordGrid.generateDiagonalLeftLocations(row, col, wordLength).foreach { locs =>
              domain.append(locs)
            }
          }
        }
      }
    }

    domain
  }
}

object WordGrid {
  val ALPHABET_LENGTH = 26
  val FIRST_LETTER = 'A'
  val LAST_LETTER = 'Z'

  def apply(): WordGrid = WordGrid(
    rows = 0,
    columns = 0,
    grid = ArrayBuffer()
  )

  def apply(rows: Int, columns: Int, shouldFillRandomly: Boolean = false): WordGrid = {
    val grid = ArrayBuffer.fill(rows, columns) { FIRST_LETTER }

    WordGrid(
      rows,
      columns,
      grid = if shouldFillRandomly then generateRandomWordGrid(grid) else grid,
    )
  }

  def generateRandomWordGrid(wordgrid: ArrayBuffer[ArrayBuffer[Char]]): ArrayBuffer[ArrayBuffer[Char]] = {
    import WordGrid.{FIRST_LETTER, LAST_LETTER}

    val randomGrid = wordgrid.map { row =>
      row.map { col =>
        Random.between(FIRST_LETTER, LAST_LETTER + 1).toChar
      }
    }

    randomGrid
  }

  def generateRightLocations(row: Int, column: Int, wordLength: Int): Option[ArrayBuffer[GridLocation]] = {
    var locations = ArrayBuffer[GridLocation]()

    var c = column

    while (c < column + wordLength) {
      locations.append(GridLocation(row, col = c))

      c = c + 1
    }

    if (!locations.isEmpty) then Some(locations) else None
  }

  def generateDiagonalRightLocations(row: Int, column: Int, wordLength: Int): Option[ArrayBuffer[GridLocation]] = {
    var locations = ArrayBuffer[GridLocation]()

    var c = column
    var r = row

    while (c < column + wordLength) {
      locations.append(GridLocation(row = r, col = c))

      r = r + 1
      c = c + 1
    }

    if (!locations.isEmpty) then Some(locations) else None
  }

  def generateDownLocations(row: Int, column: Int, wordLength: Int): Option[ArrayBuffer[GridLocation]] = {
    var locations = ArrayBuffer[GridLocation]()

    var r = row

    while (r < row + wordLength) {
      locations.append(GridLocation(row = r, column))

      r = r + 1
    }

    if (!locations.isEmpty) then Some(locations) else None
  }

  def generateDiagonalLeftLocations(row: Int, column: Int, wordLength: Int): Option[ArrayBuffer[GridLocation]] = {
    var locations = ArrayBuffer[GridLocation]()

    var c = column
    var r = row

    while (r < row + wordLength) {
      locations.append(GridLocation(row = r, col = c))

      r = r + 1
      c = c - 1
    }

    if (!locations.isEmpty) then Some(locations) else None
  }
}

def displayWordGrid(wg: WordGrid): Unit = {
  for (row <- wg.grid) {
    for (col <- row) {
      print(s"${dgr("[")}$col${dgr("]")}")
    }

    println()
  }
}

case class WordSearchConstraint(
  words: ArrayBuffer[String],
) extends Constraint[String, ArrayBuffer[GridLocation]] {
  override val variables: Vector[String] = words.toVector

  override def isSatisfied(assignment: mutable.HashMap[String, mutable.ArrayBuffer[GridLocation]]): Boolean = {
    val allLocations = assignment.values.flatten

    val allLocationsSet = HashSet.from(allLocations)

    allLocations.size == allLocationsSet.size
  }
}

@main def run(): Unit = {
  var wordgrid = WordGrid(10, 10, shouldFillRandomly = true)

  println("Initial word grid:")

  displayWordGrid(wordgrid)

  val words = ArrayBuffer("MATTHEW", "JOE", "MARY", "SARAH", "SALLY")

  var domains = HashMap[String, ArrayBuffer[ArrayBuffer[GridLocation]]]()

  for (word <- words) {
    domains.addOne(word -> wordgrid.generateDomain(word))
  }

  var csp = CSP[String, ArrayBuffer[GridLocation], WordSearchConstraint](words, domains)

  csp.foreach { cspRight =>
    val _ = cspRight.addConstraint(WordSearchConstraint(words))

    val possibleSolution = cspRight.backtrackingSearch

    possibleSolution match {
      case Some(solution) => {
        println("Found solution!")

        for ((word, locs) <- solution) {
          val wordLocations = if (Random.nextBoolean()) then locs.reverse else locs

          wordgrid.markWord(word, wordLocations)
        }

        displayWordGrid(wordgrid)
      }
      case None => {
        println("No solution found :-(")
      }
    }
  }
}
