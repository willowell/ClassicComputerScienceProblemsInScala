/// # Priority Queues
///
/// Just a small exploration of priority queues in Scala.
/// Since the A* algorithm uses it, I'm just making sure I know how to use it first!
///
package book.chapter02.pq

import scala.collection.mutable.PriorityQueue

case class Bug(
  title: String,
  desc: String,
  severity: Int
)

@main def run(): Unit = {
  val q1 = PriorityQueue(7, 2, 9, 0, 5, 3, 4)

  val reverseIntOrdering = summon[Ordering[Int]].reverse

  val q2 = PriorityQueue(7, 2, 9, 0, 5, 3, 4) { reverseIntOrdering }

  println(q1.clone.dequeueAll) // => ArraySeq(9, 7, 5, 4, 3, 2, 0)

  println(q2.clone.dequeueAll) // => ArraySeq(0, 2, 3, 4, 5, 7, 9)

  val bugs = List(
    Bug(
      title = "Computer crashed!",
      desc = "My computer crashed when I spilled coffee on it",
      severity = 10
    ),
    Bug(
      title = "Spider on my monitor",
      desc = "Please help me debug my monitor",
      severity = 5
    ),
    Bug(
      title = "No more coffee",
      desc = "The coffee machine is empty",
      severity = 1
    )
  )

  val q3 = PriorityQueue.from(bugs) {
    Ordering.by { (bug: Bug) => bug.severity }
  }

  val q4 = PriorityQueue.from(bugs) {
    Ordering.by({ (bug: Bug) => bug.severity }).reverse
  }

  // Alternative version using shorthand syntax.
  val q5 = PriorityQueue.from(bugs) {
    Ordering.by({ (_: Bug).severity }).reverse
  }

  println(q3.clone.dequeueAll.mkString("\n")) // => Bug with severity 10 appears first

  println()

  println(q4.clone.dequeueAll.mkString("\n")) // => Bug with severity 1 appears first

  val q6 = PriorityQueue(0.5, 1.2, 5.6, 3.2, 99.0, 45.0)

  val q7 = PriorityQueue(0.5, 1.2, 5.6, 3.2, 99.0, 45.0) { summon[Ordering[Double]].reverse }

  println(q6.clone.dequeueAll) // => ArraySeq(99.0, 45.0, 5.6, 3.2, 1.2, 0.5)

  println(q7.clone.dequeueAll) // => ArraySeq(0.5, 1.2, 3.2, 5.6, 45.0, 99.0)
}
