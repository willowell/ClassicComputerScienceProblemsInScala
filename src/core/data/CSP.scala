package core.data.csp

import core.text.prettyprinting.*

import scala.collection.mutable.{ArrayBuffer, HashMap}

trait Constraint[V, D] {
  val variables: Vector[V]

  def isSatisfied(assignment: HashMap[V, D]): Boolean
}

case class CSP[
  V,
  D,
  C <: Constraint[V, D]
](
  var variables: ArrayBuffer[V],
  var domains: HashMap[V, ArrayBuffer[D]],
  var constraints: HashMap[V, ArrayBuffer[C]]
) {
  def addConstraint(constraint: C): Either[String, Unit] = {
    import scala.util.boundary, boundary.break

    val vars = constraint.variables

    val blockResult = boundary {
      for (variable <- constraint.variables) {
        if (!variables.contains(variable)) {
          break(Left("Variable in constraint is not in CSP"))
        }

        val associatedConstraints = constraints.get(variable)

        associatedConstraints.foreach { ac =>
          ac.addOne(constraint)
        }
      }
    }

    blockResult match {
      case Left(str) => Left(str)
      case () => Right(())
    }
  }

  def isConsistent(variable: V, assignment: HashMap[V, D]): Boolean = {
    import scala.util.boundary, boundary.break

    val associatedConstraints = constraints.get(variable)

    val blockResult = boundary {
      associatedConstraints.foreach { assocContraints =>
        for (constraint <- assocContraints) {
          if (!constraint.isSatisfied(assignment)) {
            break(false)
          }
        }
      }
    }

    blockResult match {
      case b: Boolean => b
      case () => true
    }
  }

  def backtrackingSearchWithAssignment(assignment: HashMap[V, D]): Option[HashMap[V, D]] = {
    import scala.util.boundary, boundary.break

    //println("SEARCHING WITH ASSIGNMENT: ")
    //println(assignment.mkString)

    // Assignment is complete if every variable is assigned (base case)
    if (assignment.size == variables.length) {
      //println("Every variable is assigned!")
      return Some(assignment)
    }

    val unassigned = variables.find { v => !assignment.contains(v) }

    //println("UNASSIGNED:")
    //println(unassigned)

    val blockResult = boundary {
      unassigned.foreach { unassignedV => 
        for (associatedDomains <- domains.get(unassignedV)) {
          for (value <- associatedDomains) {
            var localAssignment = assignment.clone

            localAssignment.addOne(unassignedV -> value)

            //println("LOCAL ASSIGNMENT:")
            //println(localAssignment)

            if (isConsistent(unassignedV, localAssignment)) {
             //println("LOCAL ASSIGNMENT IS CONSISTENT")

              // Return a search result ONLY on `Some`.
              // Returning `result` itself is completely valid per the types,
              // but this can cause invalid states to be returned as well.
              backtrackingSearchWithAssignment(localAssignment).foreach { result =>
                //println("FOUND RESULT")
                break(Some(result))
              }
            }
          }
        }
      }
    }

    blockResult match {
      case Some(res) => Some(res)
      case () => None
    }
  }

  def backtrackingSearch: Option[HashMap[V, D]] = backtrackingSearchWithAssignment(HashMap())
}

given displayCSP[V, D, C <: Constraint[V, D]]: Display[CSP[V, D, C]] with {
  override def display(a: CSP[V, D, C]): String = {
    val formattedVariables = a.variables.mkString("\n")
    val formattedDomains = a.domains.mkString("\n")
    val formattedConstraints = a.constraints.mkString("\n")

    "Variables:\n" ++ formattedVariables ++ "\n\nDomains:\n" ++ formattedDomains ++ "\n\nConstraints:\n" ++ formattedConstraints
  }
}

object CSP {
  def apply[V, D, C <: Constraint[V, D]](variables: ArrayBuffer[V], domains: HashMap[V, ArrayBuffer[D]]): Either[String, CSP[V, D, C]] = {
    import scala.util.boundary, boundary.break

    var constraints = HashMap[V, ArrayBuffer[C]]()

    val blockResult = boundary {
      for (variable <- variables) {
        constraints.addOne(variable -> ArrayBuffer())

        if (!domains.contains(variable)) {
          break(Left("Every variable shold have a domain assigned to it."))
        }
      }
    }

    blockResult match {
      case Left(str) => Left(str)
      case () => Right(CSP(
        variables,
        domains,
        constraints,
      ))
    }
  }
}
