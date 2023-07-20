/// # The Australian Map Colouring Constraint Problem
///
/// This example is a bit rough around the edges...
/// Since the Java implementation makes use of generic abstract classes and type elision, we have to address both of these
/// issues along the way. Rather than replace the stateful generic abstract class with a struct and trait combo, I have opted to
/// instead go with just a trait, whose `variables()` function mimics the `variables` property on the corresponding Java
/// abstract class.
///
/// Methods in the Java implementation that throw exceptions return `Either`s in this implementation.
///
/// Aside from this type tetris, this example is not terribly complicated.
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter3/MapColoringConstraint.java
///
package book.chapter03.auMapColouring

import core.data.csp.{given, *}
import core.text.fansi.*
import core.text.prettyprinting.*
import core.text.prettyprinting.Ops.*

import scala.collection.mutable.{ArrayBuffer, HashMap}


enum Colour {
  case Red
  case Green
  case Blue
}

given DisplayStyled[Colour] with {
  extension (a: Colour) def displayStyled: fansi.Str = a match {
    case Colour.Red   => r("R")
    case Colour.Green => g("G")
    case Colour.Blue  => b("B")
  }
}

case class MapColouringConstraint(
  place1: String,
  place2: String,
) extends Constraint[String, Colour] {
  override val variables: Vector[String] = Vector(place1, place2)

  override def isSatisfied(assignment: HashMap[String, Colour]): Boolean = {
    import scala.util.boundary, boundary.break

    if (!assignment.contains(place1) || !assignment.contains(place2)) {
      return true
    }

    val optPlace1 = assignment.get(place1)
    val optPlace2 = assignment.get(place2)

    val isSame = for {
      p1 <- optPlace1
      p2 <- optPlace2
    } yield !(p1 == p2)

    println(s"$optPlace1 AND $optPlace2 ARE THE SAME? $isSame")

    val blockResult = boundary {
      isSame match {
        case Some(b) => break(b)
        case None => ()
      }
    }

    blockResult match {
      case b: Boolean => b
      case () => false
    }
  }
}

@main def run(): Unit = {
  val Western_Australia: String            = "Western Australia"
  val Northern_Territory: String           = "Northern Territory"
  val South_Australia: String              = "South Australia"
  val Queensland: String                   = "Queensland"
  val New_South_Wales: String              = "New South Wales"
  val Victoria: String                     = "Victoria"
  val Tasmania: String                     = "Tasmania"
  val Australian_Capital_Territory: String = "Australian Capital Territory"
  val Jervis_Bay_Territory: String         = "Jervis Bay Territory"

  val variables = ArrayBuffer(
    Western_Australia,
    Northern_Territory,
    South_Australia,
    Queensland,
    New_South_Wales,
    Victoria,
    Tasmania,
    // These two can act as good sanity checks.
    // ACT is inside NSW, so any other colour will do.
    // Likewise, JBT is on the coastline surrounded by NSW, so any other colour will do.
    Australian_Capital_Territory, // enclave inside New South Wales
    Jervis_Bay_Territory,         // territory on the coast of New South Wales
  )

  var domains = HashMap[String, ArrayBuffer[Colour]]()

  for (variable <- variables) {
    domains.addOne(variable -> ArrayBuffer(Colour.Red, Colour.Green, Colour.Blue))
  }

  var csp = CSP[String, Colour, MapColouringConstraint](variables, domains)

  csp.foreach { cspRight => 
    val constraints = List(
      Western_Australia            -> Northern_Territory,
      Western_Australia            -> South_Australia,
      South_Australia              -> Northern_Territory,
      Queensland                   -> Northern_Territory,
      Queensland                   -> South_Australia,
      Queensland                   -> New_South_Wales,
      New_South_Wales              -> South_Australia,
      Victoria                     -> South_Australia,
      Victoria                     -> New_South_Wales,
      Victoria                     -> Tasmania,
      Australian_Capital_Territory -> New_South_Wales,
      Jervis_Bay_Territory         -> New_South_Wales,
    ).map { (p1, p2) => MapColouringConstraint(p1, p2) }

    for (constraint <- constraints) {
      val _ = cspRight.addConstraint(constraint)
    }

    println(display(cspRight))

    println("\n\n")

    println("Performing a backtracking search...")

    val searchResult = cspRight.backtrackingSearch

    searchResult match {
      case Some(solution) => {
        println("Found solution:")

        for ((k, v) <- solution) {
          println(s"$k => ${v.displayStyled}")
        }
      }
      case None => {
        println("No solution found :-(")
      }
    }
    
  }
}
