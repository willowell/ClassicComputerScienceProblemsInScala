package core.search.graph

import scala.collection.mutable.{ArrayBuffer, ArrayDeque, HashMap, HashSet, PriorityQueue}

import scala.math.Ordered

case class Node[S](
  state: S,
  parent: Option[Node[S]],
  cost: Double,
  heuristic: Double,
) extends Ordered[Node[S]] {
  def toNodePath: ArrayDeque[Node[S]] = {
    var path = ArrayDeque[Node[S]]()

    path.prepend(this)

    var currentNode = this

    // Work backwards from end to front
    while (currentNode.hasParent) {
      currentNode.parent.foreach { parentNode =>
        currentNode = parentNode

        path.prepend(parentNode)
      }
    }

    path
  }

  def toPath: ArrayDeque[S] = {
    val nodePath = toNodePath

    nodePath.map { _.state }
  }

  def getTotalCostOfPath: Double = {
    val nodePath = toNodePath

    nodePath.foldLeft(0.0) { (acc, node) => acc + node.cost }
  }

  def hasParent: Boolean = parent match {
    case Some(_) => true
    case None    => false
  }

  override def compare(that: Node[S]): Int = {
    val lhsCost = this.cost + this.heuristic
    val rhsCost = that.cost + that.heuristic

    (lhsCost - rhsCost).toInt
  }
}

object Node {
  def apply[S](state: S, parent: Option[Node[S]]): Node[S] = new Node[S](
    state,
    parent,
    0.0,
    0.0,
  )

  def apply[S](state: S, parent: Option[Node[S]], cost: Double, heuristic: Double): Node[S] = new Node[S](
    state,
    parent,
    cost,
    heuristic,
  )
}

type GoalTestFn[S] = S => Boolean

type GetSuccessorsFn[S] = S => Seq[S]

/** ## Depth First Search
  *
  * Use `goalTestFn` to search for a value in a given structure,
  * given an `initial` state and `getSuccessorsFn` that describes how to reach a value's neighbours.
  *
  * Returns `Some` if the goal is found, and `None` otherwise.
  * 
  * You can traverse the returned `Node`'s `parent`s to get the path the algorithm took.
  *
  * @param initial
  * @param goalTestFn
  * @param getSuccessorsFn
  * @return
  */
def dfs[S](
  initial: S,
  goalTestFn: GoalTestFn[S],
  getSuccessorsFn: GetSuccessorsFn[S],
): Option[Node[S]] = {
  // Frontier is where we've yet to go.
  var frontier = ArrayBuffer[Node[S]]()

  frontier.append(Node(initial, None))

  // Explored is where we've been.
  var explored = HashSet[S]()

  val _ = explored.add(initial)

  // Keep going while there is more to explore.
  while (!frontier.isEmpty) {
    val currentNode = frontier.remove(frontier.length - 1)

    val currentState = currentNode.state

    // If the current node matches the goal, we're done!
    if (goalTestFn(currentState)) {
      return Some(currentNode)
    }

    // Otherwise, get the current node's neighbours and keep exploring.
    for (succ <- getSuccessorsFn(currentState)) {
      val notYetExplored = explored.add(succ)

      if (notYetExplored) {
        frontier.append(
          Node(succ, Some(currentNode))
        )
      }

      // Do nothing if node already explored
    }
  }

  None
}

/** ## Recursive Depth First Search
  *
  * Use `goalTestFn` to search for a value in a given structure,
  * given an `initial` state and `getSuccessorsFn` that describes how to reach a value's neighbours.
  *
  * Returns `Some` if the goal is found, and `None` otherwise.
  * 
  * You can traverse the returned `Node`'s `parent`s to get the path the algorithm took.
  *
  * @param initial
  * @param goalTestFn
  * @param getSuccessorsFn
  * @return
  */
def dfsRec[S](
  initial: S,
  goalTestFn: GoalTestFn[S],
  getSuccessorsFn: GetSuccessorsFn[S],
): Option[ArrayDeque[S]] = {
  var path = ArrayDeque(initial)

  if dfsStep(path, getSuccessorsFn, goalTestFn) then
    Some(path)
  else
    None
}

def dfsStep[S](
  path: ArrayDeque[S],
  getSuccessorsFn: GetSuccessorsFn[S],
  goalTestFn: GoalTestFn[S],
): Boolean = {
  import scala.util.boundary, boundary.break

  if (goalTestFn(path.removeLast())) {
    true
  } else {
    val successorsIt = getSuccessorsFn(path.removeLast())

    boundary {
      for (n <- successorsIt) {
        if (!path.contains(n)) {
          path.append(n)

          if (dfsStep(path, getSuccessorsFn, goalTestFn)) {
            break(true)
          }

          val _ = path.removeLastOption()
        }
      }
    }
  }

  false
}

/** ## Breadth First Search
  *
  * Use `goal_test_fn` to search for a value in a given structure,
  * given an `initial` state and `get_successors_fn` that describes how to reach a value's neighbours.
  *
  * Returns `Some` if the goal is found, and `None` otherwise.
  * 
  * You can traverse the returned `Node`'s `parent`s to get the path the algorithm took.
  *
  * @param initial
  * @param goalTestFn
  * @param getSuccessorsFn
  * @return
  */
def bfs[S](
  initial: S,
  goalTestFn: GoalTestFn[S],
  getSuccessorsFn: GetSuccessorsFn[S],
): Option[Node[S]] = {
  // Frontier is where we've yet to go.
  var frontier = ArrayDeque[Node[S]]()

  frontier.append(Node(initial, None))

  // Explored is where we've been.
  var explored = HashSet[S]()

  val _ = explored.add(initial)

  // Keep going while there is more to explore.
  while (!frontier.isEmpty) {
    val currentNode = frontier.removeHead()

    val currentState = currentNode.state

    // If the current node matches the goal, we're done!
    if (goalTestFn(currentState)) {
      return Some(currentNode)
    }

    // Otherwise, get the current node's neighbours and keep exploring.
    for (succ <- getSuccessorsFn(currentState)) {
      val notYetExplored = explored.add(succ)

      if (notYetExplored) {
        frontier.append(
          Node(succ, Some(currentNode))
        )
      }

      // Do nothing if node already explored
    }
  }

  None
}

type HeuristicFn[S] = S => Double

/** ## A-Star Search
  *
  * Use `goalTestFn` to search for a value in a given structure,
  * given an `initial` state and movement cost and `getSuccessorsFn` that describes how to reach a value's neighbours,
  * and a `heuristicFn` that describes the estimated cost of moving from one node to the goal node.
  *
  * Returns `Some` if the goal is found, and `None` otherwise.
  * 
  * You can traverse the returned `Node`'s `parent`s to get the path the algorithm took.
  *
  * @param initial
  * @param goalTestFn
  * @param getSuccessorsFn
  * @param heuristicFn
  * @return
  */
def astar[S](
  initial: S,
  goalTestFn: GoalTestFn[S],
  getSuccessorsFn: GetSuccessorsFn[S],
  heuristicFn: HeuristicFn[S]
): Option[Node[S]] = {
  // Note that this uses the reverse ordering to create a min-heap,
  // so that it sorts nodes by cost in ascending order, as described by `Node`'s `compare`.
  //
  // The default is descending order, so the algorithm may appear to choose the *worst* possible path,
  // which is obviously not what we want in a proper A* implementation!
  var frontier: PriorityQueue[Node[S]] = PriorityQueue() {
    summon[Ordering[Node[S]]].reverse
  }

  frontier.addOne(Node(
    state = initial,
    parent = None,
    cost = 0.0,
    heuristic = heuristicFn(initial),
  ))

  var explored = HashMap[S, Double]()

  val _ = explored.addOne(initial -> 0.0)

  while (!frontier.isEmpty) {
    val currentNode = frontier.dequeue()

    val currentState = currentNode.state

    // If the current node matches the goal, we're done!
    if (goalTestFn(currentState)) {
      return Some(currentNode)
    }

    for (succ <- getSuccessorsFn(currentState)) {
      // Since we are using a regular grid with no cost to move from one tile to another,
      // an added cost of 1 is fine.
      //
      // If there are obstructions between nodes, a `costFn` would make more sense here.
      // For instance, if there was a patch of rough terrain between two nodes, that move
      // would be more expensive than if the path was clear.
      //
      // Or, for another example, if we were making a map where our nodes represent places on that map,
      // we would want to consider traffic and delays on the route here, which can make an otherwise optimal
      // route more expensive.
      val newCost = currentNode.cost + 1.0

      val oldCost = explored.getOrElse(succ, { 0.0 })

      // If we have not explored this location yet,
      // OR if we have AND it has a lower cost, push the node
      if (!explored.contains(succ) || (oldCost != 0.0 && oldCost > newCost)) {
        val _ = explored.addOne(succ -> newCost)

        frontier.enqueue(Node(
          state = succ,
          parent = Some(currentNode),
          cost = newCost,
          heuristic = heuristicFn(succ),
        ))
      }
    }
  }

  None
}
