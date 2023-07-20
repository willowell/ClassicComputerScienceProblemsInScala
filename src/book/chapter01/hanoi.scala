package book.chapter01.hanoi

import scala.collection.mutable.ArrayDeque

case class Hanoi3(
  numDiscs: Int,
  towerA: ArrayDeque[Int],
  towerB: ArrayDeque[Int],
  towerC: ArrayDeque[Int],
) {
  def display(): Unit = {
    println("============Towers of Hanoi===========")
    println("======================================")
    println("Higher numbers represent smaller discs")

    println("===============Tower A================")

    if (!towerA.isEmpty) {
      towerA.zipWithIndex.foreach { (disc, i) => {
        val spacer = " ".repeat(numDiscs - i - 1)
        val discWidth = " ".repeat(numDiscs - disc)
        println(s"$spacer[$discWidth$disc$discWidth]$spacer")
      } }
    } else {
      for (_ <- 1 to numDiscs) {
        val spacer = " ".repeat(numDiscs - 1)

        println(s"$spacer[ ]$spacer")
      }
    }

    println("===============Tower B================")

    if (!towerB.isEmpty) {
      towerB.zipWithIndex.foreach { (disc, i) => {
        val spacer = " ".repeat(numDiscs - i - 1)
        val discWidth = " ".repeat(numDiscs - disc)
        println(s"$spacer[$discWidth$disc$discWidth]$spacer")
      } }
    } else {
      for (_ <- 1 to numDiscs) {
        val spacer = " ".repeat(numDiscs - 1)

        println(s"$spacer[ ]$spacer")
      }
    }

    println("===============Tower C================")

    if (!towerC.isEmpty) {
      towerC.zipWithIndex.foreach { (disc, i) => {
        val spacer = " ".repeat(numDiscs - i - 1)
        val discWidth = " ".repeat(numDiscs - disc)
        println(s"$spacer[$discWidth$disc$discWidth]$spacer")
      } }
    } else {
      for (_ <- 1 to numDiscs) {
        val spacer = " ".repeat(numDiscs - 1)

        println(s"$spacer[ ]$spacer")
      }
    }

    println("======================================")
    println("======================================\n\n")
  }

  def solve(): Unit = {
    Hanoi3.moveDiscs(begin = towerA, end = towerC, temp = towerB, n = numDiscs)
  }
}

object Hanoi3 {
  def apply(numDiscs: Int): Hanoi3 = Hanoi3(
    numDiscs,
    towerA = ArrayDeque.range[Int](numDiscs, 0, -1),
    towerB = new ArrayDeque(numDiscs),
    towerC = new ArrayDeque(numDiscs)
  )

  def moveDiscs(begin: ArrayDeque[Int], end: ArrayDeque[Int], temp: ArrayDeque[Int], n: Int): Unit = {
    if (n == 1) {
      val beginFront = begin.removeHeadOption()

      beginFront.foreach { front => end.prepend(front) }
    } else {
      moveDiscs(begin, temp, end, n - 1)
      moveDiscs(begin, end, temp, 1)
      moveDiscs(temp, end, begin, n - 1)
    }
  }
}

@main def run(): Unit = {
  var hanoi3 = Hanoi3(numDiscs = 5)

  hanoi3.display()

  hanoi3.solve()

  hanoi3.display()
}
