/// # Non Local Returns
///
/// Scala does not include `break`, `continue`, or using `return` to exit a function during a loop,
/// but it is easy to implement them using
/// `scala.util.boundary` and `scala.util.boundary.break`.
///
/// There's one big catch: wherever you would use `return <value>` inside a loop,
/// you must use `break(value)` AND capture its value.
///
package book.chapter00.nlr

import scala.util.boundary, boundary.break

//====================================================================================================================//
// BREAK                                                                                                              //
//====================================================================================================================//

def xsContains(value: Int): Boolean = {
  val xs = (1 to 10).toList

  // There is a very important gotcha here: what do you think this will return?
  //
  // You might think that `boundary` will cause the whole function to return when `break` is encountered,
  // but this is not the case: the function continues anyway, and the result of `boundary` gets discarded.
  //
  // Note that this `boundary` has type `Unit | Boolean`; i.e., it optionally returns `true` as below.
  // Because Scala returns ONLY the last expression in a function, the return value of `boundary` gets discarded here.
  // In other words, this function will always return `false`!
  boundary {
    for (x <- xs) {
      if (x == value) {
        break(true)
      }
    }
  }

  false
}

def xsContains2(value: Int): Boolean = {
  val xs = (1 to 10).toList

  // In order to use the value returned by `break(value)`, you need to save the result of `boundary` to a
  // variable and then pattern match on it.
  // `boundary` has type `Unit | Boolean`, reflecting that it may do nothing (`Unit`) or return a `Boolean`.
  val res = boundary {
    for (x <- xs) {
      if (x == value) {
        break(true)
      }
    }
  }

  // If `res` is `Unit` (`()`), then the `boundary` block never encountered the `break(value)` arm.
  res match {
    case b: Boolean => b
    case () => false
  }
}

//====================================================================================================================//
// CONTINUE                                                                                                           //
//====================================================================================================================//

def xsSillyMap: List[Int] = {
  var xs = (1 to 10).toBuffer

  // To implement `continue` using `boundary` and `break`,
  // just wrap the body of the loop with `boundary` and add `break()` where you would otherwise use `continue`.
  for ((x, i) <- xs.zipWithIndex) {

    // No need to capture the result of `boundary` as it is only `Unit` in this case.
    boundary {
      if (!(x % 2 == 0)) {
        break()
      }

      xs(i) = x * 2
    }
  }

  xs.toList
}

//====================================================================================================================//
// ALTERNATIVES                                                                                                       //
//====================================================================================================================//

// Do you notice that the `break` examples resemble filters and that the `continue` examples resemble maps?
//
// If the `boundary` and `break` system feels janky, that's because Scala would like to encourage you to approach this
// in terms of immutable data - i.e., `xsSillyMap` involves mutation on `xs`, but we can more cleanly express this using a map.
//

def xsSillyMap2: List[Int] = {
  var xs = (1 to 10).toList

  xs.map { (x) => if (x % 2 == 0) then x * 2 else x }
}

// For the `xsContains` functions, we can replace the `boundary` and `break` system with `exists`:

def xsContains3(value: Int): Boolean = {
  val xs = (1 to 10).toList

  xs.exists { _ == value }
}

//====================================================================================================================//
// WHILE LOOPS                                                                                                        //
//====================================================================================================================//

// Or, you can use a while loop!
// The reason why for-loops have this non-local return issue in the first place is that internally, a for-loop
// is syntax sugar over for a collection of methods, including `.map`, `.flatMap`, `.filterWhile`, etc.
//
// Of course, using a while loop feels like a step backwards: you have to manage the iterator and moving it yourself.
//

def xsContains4(x: Int): Boolean = {
  var xs = (1 to 10).toBuffer

  var i = 0

  while (i < xs.length) {
    if (xs(i) == x) {
      return true
    }

    i = i + 1
  }

  false
}

//====================================================================================================================//
// MAIN                                                                                                               //
//====================================================================================================================//

@main def run(): Unit = {
  println(xsContains(4))
  println(xsContains2(4))
  println(xsSillyMap)
  println(xsSillyMap2)
  println(xsContains4(4))
}
