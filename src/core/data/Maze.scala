package core.data.maze

import scala.collection.mutable.ArrayBuffer

enum Cell extends scala.math.Ordered[Cell] derives CanEqual {
  /**
    * This cell is empty.
    */
  case Empty

  /**
    * This cell is blocked - i.e., it represents an obstruction.
    */
  case Blocked

  /**
    * This cell represents the starting position.
    */
  case Start

  /**
    * This cell represents the goal position.
    */
  case Goal

  /**
    * This cell indicates a visited position.
    */
  case Path

  def compare(that: Cell): Int = this.ordinal - that.ordinal 

  def toCharacter: String = this match {
    case Cell.Empty   => " "
    case Cell.Blocked => "◼︎"
    case Cell.Start   => "▶︎"
    case Cell.Goal    => "★"
    case Cell.Path    => "●"
  }

  def toStyledCharacter: fansi.Str = this match {
    case Cell.Empty   => " "
    case Cell.Blocked => fansi.Color.Red("◼︎")
    case Cell.Start   => fansi.Color.Green("▶︎")
    case Cell.Goal    => fansi.Color.Yellow("★")
    case Cell.Path    => fansi.Color.LightBlue("●")
  }
}

case class MazeLocation(
  row: Int,
  column: Int
) {
  /**
    * Manhattan distance from this location in the maze to `dest`.
    *
    * @param dest
    * @return
    */
  def manhattanDistanceTo(dest: MazeLocation): Int = {
    import scala.math.abs

    val xDistance = abs(dest.column - this.column)
    val yDistance = abs(dest.row - this.row)

    xDistance + yDistance
  }
}

/**
  * 
  *
  * @param rows
  * @param columns
  * @param start
  * @param goal
  * @param grid
  */
case class Maze(
  rows: Int,
  columns: Int,
  start: MazeLocation,
  goal: MazeLocation,
  var grid: ArrayBuffer[ArrayBuffer[Cell]]
) {
  def testGoal(loc: MazeLocation): Boolean = goal == loc

  def isLocationValid(loc: MazeLocation): Boolean = {
       loc.row    >= 0      // left boundary
    && loc.row    < rows    // right boundary
    && loc.column >= 0      // top boundary
    && loc.column < columns // bottom boundary
  }

  def display(): Unit = {
    for (row <- this.grid) {
      for (col <- row) {
        val leftBracket = fansi.Color.DarkGray("[")
        val rightBracket = fansi.Color.DarkGray("]")
        val cellContents = col.toStyledCharacter

        print(leftBracket ++ cellContents ++ rightBracket)
      }
      println()
    }
  }

  /**
    * 
    *
    * @param fromLoc
    * @return
    */
  def getSuccessors(fromLoc: MazeLocation): Vector[MazeLocation] = {
    import scala.math.abs

    var successors: ArrayBuffer[MazeLocation] = ArrayBuffer.empty

    for (dx <- -1 to 1) {
      for (dy <- -1 to 1) {
        if (abs(dx + dy) == 1) {
          val newPosition = MazeLocation(
            row = fromLoc.row + dx,
            column = fromLoc.column + dy
          )

          if (isLocationValid(newPosition)) {
            val mazeValue = grid(newPosition.row)(newPosition.column)

            if (mazeValue != Cell.Blocked) {
              successors.append(newPosition)
            }
          }
        }
      }
    }

    successors.toVector
  }

  /**
    * 
    */
  def markStartAndGoal(): Unit = {
    grid(start.row)(start.column) = Cell.Start
    grid(goal.row)(goal.column) = Cell.Goal
  }

  /**
    * 
    */
  def markPath(path: Vector[MazeLocation]): Unit = {
    path.foreach { loc =>
      grid(loc.row)(loc.column) = Cell.Path
    }

    markStartAndGoal()
  }

  /**
    * 
    */
  def clearPath(): Unit = {
    for ((row, r) <- grid.zipWithIndex) {
      for ((col, c) <- row.zipWithIndex) {
        if (col == Cell.Path) {
          grid(r)(c) = Cell.Empty
        }
      }
    }
  }

  def distanceToGoal(loc: MazeLocation): Int = goal.manhattanDistanceTo(loc)
}

object Maze {
  /** foobar
    * foo
    *
    * @param rows
    * @param columns
    * @return
    */
  def apply(rows: Int, columns: Int): Maze = {
    val grid = ArrayBuffer.fill(rows, columns) { Cell.Empty }

    Maze(
      rows,
      columns,
      start = MazeLocation(0, 0),
      goal = MazeLocation(0, 0),
      grid
    )
  }

  /**
    * 
    *
    * @param rows
    * @param columns
    * @param sparseness
    * @return
    */
  def apply(rows: Int, columns: Int, sparseness: Double): Maze = {
    val grid = ArrayBuffer.fill(rows, columns) {
      import scala.util.Random

      if Random.nextDouble() < sparseness then Cell.Blocked else Cell.Empty
    }

    Maze(
      rows,
      columns,
      start = MazeLocation(0, 0),
      goal = MazeLocation(0, 0),
      grid
    )
  }

  /**
    * 
    *
    * @param rows
    * @param columns
    * @param start
    * @param goal
    * @param sparseness
    * @return
    */
  def apply(
    rows: Int,
    columns: Int,
    start: MazeLocation,
    goal: MazeLocation,
    sparseness: Double
  ): Maze = {
    val grid = ArrayBuffer.fill(rows, columns) {
      import scala.util.Random

      if Random.nextDouble() < sparseness then Cell.Blocked else Cell.Empty
    }

    Maze(
      rows,
      columns,
      start,
      goal,
      grid
    )
  }
}
