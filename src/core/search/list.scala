package core.search.list

import cats.kernel.Order
import cats.kernel.Comparison
import cats.kernel.Comparison.{EqualTo as EQ, GreaterThan as GT, LessThan as LT}

import core.math.catsorderext.*

def linearContains[A](xs: Seq[A], key: A): Boolean = {
  var i = 0

  while (i < xs.length) {
    if (xs(i) == key) {
      return true
    }

    i = i + 1
  }

  false
}

def isSorted[A](xs: Seq[A])(using ordering: Ordering[A]): Boolean = {
  val sorted = xs.sorted(using ordering)

  sorted == xs
}

def binaryContains[A: Order](xs: Seq[A], key: A)(using ordering: Ordering[A]): Either[String, Boolean] = {
  if (!isSorted(xs)(using ordering)) {
    return Left("container must be sorted first")
  }

  var low = 0
  var high = xs.length - 1

  while (low <= high) {
    val middle = (low + high) / 2

    val x = xs(middle)

    x <=> key match {
      case GT => {
        high = middle - 1
      }
      case LT => {
        low = middle + 1
      }
      case EQ => {
        return Right(true)
      }
    }
  }

  return Right(false)
}
