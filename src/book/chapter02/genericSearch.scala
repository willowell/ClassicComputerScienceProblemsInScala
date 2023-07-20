/// # Generic Search
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter2/GenericSearch.java
package book.chapter02.genericSearch

import core.search.list.*
import core.text.fansi.*
import core.text.prettyprinting.*

@main def run(): Unit = {
  val xs = Vector(1, 5, 15, 15, 15, 15, 20)
  val ys = Vector('a', 'd', 'e', 'f', 'z')
  val zs = Vector("john", "mark", "ronald", "sarah")
  val unsorted = Vector('b', 'a', 'c')

  //==================================================================================================================//
  // Linear search on sorted lists                                                                                    //
  //==================================================================================================================//
  println("=".repeat(100))
  
  println(y("Linear search on sorted lists"))

  val xsContains5 = linearContains(xs, 5)
  val ysContainsF = linearContains(ys, 'f')
  val zsContainsName = linearContains(zs, "sheila")

  println(s"Does xs contain `5`? ${xsContains5.toYesOrNo}")
  println(s"Does ys contain `'f'`? ${ysContainsF.toYesOrNo}")
  println(s"Does zs contain `\"sheila\"`? ${zsContainsName.toYesOrNo}")

  println("=".repeat(100))

  //==================================================================================================================//
  // Linear search on unsorted lists                                                                                  //
  //==================================================================================================================//
  println(y("Linear search on unsorted lists"))

  val unsortedContainsA = linearContains(unsorted, 'a')

  println(s"Does the unsorted list of characters contain `'a'`? ${unsortedContainsA.toYesOrNo}")

  println("=".repeat(100))

  //==================================================================================================================//
  // Binary search on sorted lists                                                                                    //
  //==================================================================================================================//
  println(y("Binary search on sorted lists"))

  val xsContains52 = binaryContains(xs, 5)
  val ysContainsF2 = binaryContains(ys, 'f')
  val zsContainsName2 = binaryContains(zs, "sheila")

  println(s"Does xs contain `5`? ${xsContains52 map { _.toYesOrNo }}")
  println(s"Does ys contain `'f'`? ${ysContainsF2 map { _.toYesOrNo }}")
  println(s"Does zs contain `\"sheila\"`? ${zsContainsName2 map { _.toYesOrNo }}")

  println("=".repeat(100))

  //==================================================================================================================//
  // Binary search on unsorted lists                                                                                  //
  //==================================================================================================================//
  println(y("Binary search on unsorted lists"))

  val unsortedContainsA2 = binaryContains(unsorted, 'a')

  println(s"Does the unsorted list of characters contain `'a'`? ${unsortedContainsA2 map { _.toYesOrNo }}")

  println("=".repeat(100))
}