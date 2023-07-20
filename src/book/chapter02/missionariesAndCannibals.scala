/// # Missionaries and Cannibals
///
/// Nothing really special going on with this implementation compared to the Java one.
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter2/MCState.java
package book.chapter02.missionariesAndCannibals

import scala.collection.mutable.ArrayBuffer

import core.search.graph.bfs

val MAX_NUM: Int = 3

case class MCState(
  wm: Int,
  wc: Int,
  em: Int,
  ec: Int,
  isBoatOnWestBank: Boolean
) {
  def isAllowedState: Boolean = {
    // If west bank cannibals outnumber west bank missionaries
    // AND there is at least one missionary left,
    // then that missionary is in danger.
    if (wm < wc && wm > 0) {
      return false
    }

    // Likewise for the east bank.
    if (em < ec && em > 0) {
      return false
    }

    true
  }

  def testGoal: Boolean = {
    isAllowedState
    // All of the missionaries and cannibals are now on the east bank.
    // Side note: wouldn't it be best if they were on opposite sides of the river?
    && em == MAX_NUM && ec == MAX_NUM
  }

  def display(): Unit = {
    println(s"There are $wm missionaries and $wc cannibals on the west bank.")
    println(s"There are $em missionaries and $ec cannibals on the east bank.")
    println(s"The boat is on the ${if isBoatOnWestBank then "west" else "east"} bank.")
  }
}

object MCState {
  def apply(missionaries: Int, cannibals: Int, isBoatOnWestBank: Boolean): MCState = MCState(
    wm = missionaries,
    wc = cannibals,
    em = MAX_NUM - missionaries,
    ec = MAX_NUM - cannibals,
    isBoatOnWestBank,
  )

  def getSuccessors(mcs: MCState): List[MCState] = {
    var sucs: ArrayBuffer[MCState] = ArrayBuffer.empty

    if (mcs.isBoatOnWestBank) {
      if (mcs.wm > 1) {
        sucs.addOne(MCState(mcs.wm - 2, mcs.wc, !mcs.isBoatOnWestBank));
      }
      if (mcs.wm > 0) {
        sucs.addOne(MCState(mcs.wm - 1, mcs.wc, !mcs.isBoatOnWestBank));
      }
      if (mcs.wc > 1) {
        sucs.addOne(MCState(mcs.wm, mcs.wc - 2, !mcs.isBoatOnWestBank));
      }
      if (mcs.wc > 0) {
        sucs.addOne(MCState(mcs.wm, mcs.wc - 1, !mcs.isBoatOnWestBank));
      }
      if (mcs.wc > 0 && mcs.wm > 0) {
        sucs.addOne(MCState(
          mcs.wm - 1,
          mcs.wc - 1,
          !mcs.isBoatOnWestBank,
        ));
      }
    } else {
      if (mcs.em > 1) {
        sucs.addOne(MCState(mcs.wm + 2, mcs.wc, !mcs.isBoatOnWestBank));
      }
      if (mcs.em > 0) {
        sucs.addOne(MCState(mcs.wm + 1, mcs.wc, !mcs.isBoatOnWestBank));
      }
      if (mcs.ec > 1) {
        sucs.addOne(MCState(mcs.wm, mcs.wc + 2, !mcs.isBoatOnWestBank));
      }
      if (mcs.ec > 0) {
        sucs.addOne(MCState(mcs.wm, mcs.wc + 1, !mcs.isBoatOnWestBank));
      }
      if (mcs.ec > 0 && mcs.em > 0) {
        sucs.addOne(MCState(
          mcs.wm + 1,
          mcs.wc + 1,
          !mcs.isBoatOnWestBank,
        ));
      }
    }

    // Remove invalid states. In other words, the states where the missionaries are in danger!
    val validStates = sucs.filter({ _.isAllowedState }).toList

    validStates
  }

  def displaySolution(path: Seq[MCState]): Unit = {
    if (path.length == 0) {
      return
    }

    var oldState = path.head

    for (currentState <- path.tail) {
      if (currentState.isBoatOnWestBank) {
        val movedMissionaries = oldState.em - currentState.em
        val movedCannibals = oldState.ec - currentState.ec

        println(s"$movedMissionaries missionaries and $movedCannibals cannibals moved from the east bank to the west bank.")
      } else {
        val movedMissionaries = oldState.wm - currentState.wm
        val movedCannibals = oldState.wc - currentState.wc

        println(s"$movedMissionaries missionaries and $movedCannibals cannibals moved from the east bank to the east bank.")
      }

      currentState.display()

      println("=".repeat(74))

      println()

      oldState = currentState
    }

    oldState.display()
  }
}

@main def run(): Unit = {
  println("=".repeat(74))

  println("MISSIONARIES AND CANNIBALS")

  println("=".repeat(74))

  println()

  val start = MCState(
    missionaries = MAX_NUM,
    cannibals = MAX_NUM,
    isBoatOnWestBank = true,
  )



  val solution = bfs(
    initial = start,
    goalTestFn = { mcs => mcs.testGoal },
    getSuccessorsFn = { mcs => MCState.getSuccessors(mcs) }
  )

  solution match {
    case Some(sol) => {
      val path = sol.toPath

      println("Found a solution using BFS!")

      println("=".repeat(74))

      MCState.displaySolution(path.toSeq)

      println(s"Took ${path.length} steps.")
    }
    case None => {
      println("No solution found! :-(")
    }
  }
}
