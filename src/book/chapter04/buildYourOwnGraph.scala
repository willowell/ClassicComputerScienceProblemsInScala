/// # Build Your Own Graph
///
/// Before working through Kopec's examples for this chapter, this package explores implementing a graph in Scala.
///
package book.chapter04.buildYourOwnGraph

import scala.collection.mutable.ArrayBuffer

import core.text.prettyprinting.{Display, Ops}
import Ops.*

trait Edge {
  def u: Int
  def v: Int
  def reversed: Edge
}

case class UEdge(
  u: Int,
  v: Int
) extends Edge {
  override def reversed: UEdge = UEdge(v, u)
}

given Display[UEdge] with {
  override def display(e: UEdge) = s"{${e.u}} -> {${e.v}}"
}

case class WEdge(
  u: Int,
  v: Int,
  weight: Double
) extends Edge with scala.math.Ordered[WEdge] {

  override def reversed: WEdge = WEdge(v, u, weight)

  override def compare(that: WEdge): Int = {
    this.weight compare that.weight
  }
}

given Display[WEdge] with {
  override def display(e: WEdge) = s"{${e.u}} --(${e.weight})-> {${e.v}}"
}

trait Graph[V, E <: Edge] {
  var vertices: ArrayBuffer[V]
  var edges: ArrayBuffer[ArrayBuffer[E]]

  def getVertexCount: Int = vertices.length

  def getEdgesCount: Int = edges.map( _.length ).sum

  def addVertex(v: V): Int = {
    vertices.addOne(v)
    
    edges.addOne(ArrayBuffer.empty)

    getVertexCount - 1
  }

  def getVertexAt(index: Int): V = {
    vertices.apply(index)
  }

  def getIndexOf(v: V) = {
    vertices.indexOf(v)
  }

  def getNeighboursOf(index: Int): List[V] = {
    edges(index)
      .map { e => getVertexAt(e.v) }
      .toList
  }
}

given displayGraph[V, E <: Edge & Display[E]]: Display[Graph[V, E]] with {
  override def display(g: Graph[V, E]): String = {
    g.edges.map { edge =>
      edge.map { node =>
        node.display
      }.mkString
    }.mkString
  }
}

class UGraph[V](
  var vertices: ArrayBuffer[V],
  var edges: ArrayBuffer[ArrayBuffer[UEdge]]
) extends Graph[V, UEdge] {
  def addEdge(e: UEdge): Unit = {
    //println(edges)
    val _ = edges(e.u).addOne(e)
    val _ = edges(e.v).addOne(e.reversed)
  }

  def addEdge(from: V, to: V): Unit = {
    val i = getIndexOf(from)
    val j = getIndexOf(to)

    //println(s"$i -> $j")

    val e = UEdge(i, j)

    addEdge(e)
  }

  def addEdges(edges: Seq[(V, V)]): Unit = {
    for ((from, to) <- edges) {
      addEdge(from, to)
    }
  }
}

object UGraph {
  def apply[V]: UGraph[V] = UGraph.empty

  def apply[V](vertices: Seq[V]): UGraph[V] = new UGraph[V](
    vertices = ArrayBuffer.from(vertices),
    edges = ArrayBuffer.fill(vertices.length) { ArrayBuffer.empty }
  )

  def empty[V]: UGraph[V] = new UGraph[V](
    vertices = ArrayBuffer.empty,
    edges = ArrayBuffer.empty
  )
}

given displayUGraph[V]: Display[UGraph[V]] with {
  override def display(g: UGraph[V]): String = {
    g.edges.map { edge =>
      edge.map { node =>
        node.display
      }.mkString("", "\n", "")
    }.mkString("", "\n", "")
  }
}

class WGraph[V](
  var vertices: ArrayBuffer[V],
  var edges: ArrayBuffer[ArrayBuffer[WEdge]]
) extends Graph[V, WEdge] {

}

@main def smallGraphs(): Unit = {
  var ug0 = UGraph(Seq("A", "B"))

  ug0.addEdges(Seq("A" -> "B"))

  println(display(ug0))
}

@main def run(): Unit = {
  val Atlanta = "Atlanta"
  val Boston = "Boston"
  val Chicago = "Chicago"
  val Dallas = "Dallas"
  val Detroit = "Detroit"
  val Houston = "Houston"
  val Los_Angeles = "Los Angeles"
  val Miami = "Miami"
  val Montreal = "Montréal"
  val New_York = "New York"
  val Philadelphia = "Philadelphia"
  val Phoenix = "Pheonix"
  val Riverside = "Riverside"
  val San_Francisco = "San Francisco"
  val Seattle = "Seattle"
  val Toronto = "Toronto"
  val Washington_D_C = "Washington, D.C."
  val Vancouver = "Vancouver"

  val vertices = Seq(
    Atlanta,       Boston,       Chicago,       Dallas,
    Detroit,       Houston,      Los_Angeles,   Miami,
    New_York,      Philadelphia, Phoenix,       Riverside,
    San_Francisco, Seattle,      Washington_D_C
  )

  val edges = Seq(
    Seattle       -> Chicago,
    Seattle       -> San_Francisco,
    San_Francisco -> Riverside,
    San_Francisco -> Los_Angeles,
    Los_Angeles   -> Riverside,
    Los_Angeles   -> Phoenix,
    Riverside     -> Phoenix,
    Riverside     -> Chicago,
    Phoenix       -> Dallas,
    Phoenix       -> Houston,
    Dallas        -> Chicago,
    Dallas        -> Atlanta,
    Dallas        -> Houston,
    Houston       -> Atlanta,
    Houston       -> Miami,
    Atlanta       -> Chicago,
    Atlanta       -> Washington_D_C,
    Atlanta       -> Miami,
    Atlanta       -> New_York,
    Miami         -> Washington_D_C,
    Chicago       -> Detroit,
    Detroit       -> Boston,
    Detroit       -> Washington_D_C,
    Detroit       -> New_York,
    Boston        -> New_York,
    New_York      -> Philadelphia,
    New_York      -> Washington_D_C,
    Boston        -> Washington_D_C,
    Philadelphia  -> Washington_D_C,
  )

  var ug = UGraph(vertices)

  ug.addEdges(edges)

  println(display(ug))
}
