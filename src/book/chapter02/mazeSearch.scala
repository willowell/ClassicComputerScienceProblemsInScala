/// # Maze Search
///
/// This example is especially interesting because it reveals assumptions that the Java implementation makes and has
/// to account for them.
///
/// For instance, [`java.util.PriorityQueue`](https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/util/PriorityQueue.html)
/// orders its elements such that the the least value is first.
///
/// However, [`scala.collection.mutable.PriorityQueue`](https://scala-lang.org/api/3.3.0/scala/collection/mutable/PriorityQueue.html)
/// works the other way: as you can see in `pq.scala`, the default ordering for `PriorityQueue[Double]` is greatest to least.
///
/// Additionally, rather than using `null` to signal a search failure, this implementation uses `Option`.
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter2/Maze.java
///
package book.chapter02.mazeSearch

import core.data.maze.*

import core.search.graph.*

@main def run(): Unit = {
  val rows = 16
  val columns = 16
  val start = MazeLocation(0, 0)
  val goal = MazeLocation(15, 15)

  var maze = Maze(rows, columns, start, goal, sparseness = 0.3)

  //==================================================================================================================//
  // DISPLAY INITIAL MAZE                                                                                             //
  //==================================================================================================================//

  println("=".repeat(48))
  println("MAZE SEARCH")
  println("=".repeat(48))

  println()
  println("Here's how the maze looks:")

  maze.markStartAndGoal()
  maze.display()
  println("=".repeat(48))

  //==================================================================================================================//
  // ITERATIVE DFS                                                                                                    //
  //==================================================================================================================//

  println("ITERATIVE DFS")
  println()

  val dfsSolution = dfs(
    maze.start,
    { loc => maze.testGoal(loc) },
    { loc => maze.getSuccessors(loc) },
  )

  dfsSolution match {
    case Some(solution) => {
      println("Found solution with iterative DFS!")

      val path = solution.toPath

      maze.markPath(path.toVector)

      println("Solution path:")

      maze.display()

      println(s"Took ${path.length} steps.")
    }
    case None => {
      println("No solution found :-(")
      println("Perhaps the start or the goal are blocked off?")
    }
  }

  maze.clearPath()

  println("=".repeat(48))

  //==================================================================================================================//
  // BFS                                                                                                              //
  //==================================================================================================================//

  println("BFS")
  println()

  val bfsSolution = bfs(
    maze.start,
    { loc => maze.testGoal(loc) },
    { loc => maze.getSuccessors(loc) },
  )

  bfsSolution match {
    case Some(solution) => {
      println("Found solution with BFS!")

      val path = solution.toPath

      maze.markPath(path.toVector)

      println("Solution path:")

      maze.display()

      println(s"Took ${path.length} steps.")
    }
    case None => {
      println("No solution found :-(")
      println("Perhaps the start or the goal are blocked off?")
    }
  }

  maze.clearPath()

  println("=".repeat(48))

  //==================================================================================================================//
  // A-STAR                                                                                                           //
  //==================================================================================================================//

  println("A-STAR")
  println()

  val aStarSolution = astar(
    maze.start,
    { loc => maze.testGoal(loc) },
    { loc => maze.getSuccessors(loc) },
    { loc => maze.distanceToGoal(loc) },
  )

  aStarSolution match {
    case Some(solution) => {
      println("Found solution with A*!")

      val path = solution.toPath

      maze.markPath(path.toVector)

      println("Solution path:")

      maze.display()

      println(s"Took ${path.length} steps.")
    }
    case None => {
      println("No solution found :-(")
      println("Perhaps the start or the goal are blocked off?")
    }
  }

  maze.clearPath()

  println("=".repeat(48))
}
