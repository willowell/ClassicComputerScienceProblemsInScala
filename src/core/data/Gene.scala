package core.data.gene

import scala.collection.immutable.Vector

import core.text.fansi.*

import parsley.{Parsley as P}
import parsley.character.*
import parsley.combinator.*

import scodec.bits.{bin, BitVector}

///==================================================================================================================///
/// NUCLEOTIDE                                                                                                       ///
///==================================================================================================================///

enum Nucleotide extends scala.math.Ordered[Nucleotide] derives CanEqual {
  case Adenine, Cytosine, Guanine, Thymine

  def toLetter: Char = this match {
    case Adenine  => 'A'
    case Cytosine => 'C'
    case Guanine  => 'G'
    case Thymine  => 'T'
  }

  def toLetterStyled: fansi.Str = this match {
    case Adenine  => r("A")
    case Cytosine => g("C")
    case Guanine  => b("G")
    case Thymine  => y("T")
  }
  
  def toByte: Byte = this match {
    case Adenine  => 0
    case Cytosine => 1
    case Guanine  => 2
    case Thymine  => 3
  }

  def toBitVector: BitVector = this match {
    case Adenine  => Nucleotide.adenineBits
    case Cytosine => Nucleotide.cytosineBits
    case Guanine  => Nucleotide.guanineBits
    case Thymine  => Nucleotide.thymineBits
  }

  def compare(that: Nucleotide): Int = this.ordinal - that.ordinal 
}

object Nucleotide {
  val adenineBits  = bin"00"
  val cytosineBits = bin"10"
  val guanineBits  = bin"01"
  val thymineBits  = bin"11"

  def fromLetter(c: Char): Option[Nucleotide] = c match {
    case 'A' | 'a' => Some(Adenine)
    case 'C' | 'c' => Some(Cytosine)
    case 'G' | 'g' => Some(Guanine)
    case 'T' | 't' => Some(Thymine)
    case _         => None
  }
  
  def fromByte(b: Byte): Option[Nucleotide] = b match {
    case 0 => Some(Adenine)
    case 1 => Some(Cytosine)
    case 2 => Some(Guanine)
    case 3 => Some(Thymine)
    case _ => None
  }

  def fromBitVector(bv: BitVector): Option[Nucleotide] = bv match {
    case `adenineBits`  => Some(Adenine)
    case `cytosineBits` => Some(Cytosine)
    case `guanineBits`  => Some(Guanine)
    case `thymineBits`  => Some(Thymine)
    case _              => None
  }

  val p: P[Nucleotide] = choice(
    oneOf('a', 'A') #> Adenine,
    oneOf('c', 'C') #> Cytosine,
    oneOf('g', 'G') #> Guanine,
    oneOf('t', 'T') #> Thymine
  )
}

///==================================================================================================================///
/// CODON                                                                                                            ///
///==================================================================================================================///

case class Codon(
  first: Nucleotide,
  second: Nucleotide,
  third: Nucleotide
) {
  def toNucleotideVector: Vector[Nucleotide] = Vector(this.first, this.second, this.third)
  
  def toCodonString: String = s"${this.first}${this.second}${this.third}"
}

object Codon {
  def fromNucleotideVector(nv: Vector[Nucleotide]): Codon = Codon(nv(0), nv(1), nv(2))

  val p: P[Codon] = exactly(3, Nucleotide.p).map({ nl => Codon.fromNucleotideVector(nl.toVector) })
}

given codonOrdering: scala.math.Ordering[Codon] = Ordering.by { (codon: Codon) =>
  (codon.first, codon.second, codon.third)  
}

given codonOrdered(using x: Codon): scala.math.Ordered[Codon] = scala.math.Ordered.orderingToOrdered(x)(codonOrdering)

// value > is not a member of core.data.gene.Codon, but could be made available as an extension method.

///==================================================================================================================///
/// GENE                                                                                                             ///
///==================================================================================================================///

case class Gene(
  codons: Vector[Codon]
) {
  def linearContains(key: Codon): Boolean = {
    println(s"Performing linear search for presence of `$key` in gene")

    this.codons.contains(key)
  }

  def binaryContains(key: Codon): Boolean = {
    println(s"Performing binary search for presence of `$key` in gene")

    val sortedCodons = codons.sorted

    var low = 0
    var high = sortedCodons.length - 1

    while (low <= high) {
      val middle = (low + high) / 2

      val x = sortedCodons(middle)

      import scala.math.Ordered.orderingToOrdered

      x compare key match {
        case x if x > 0 => {
          high = middle - 1
        }
        case x if x < 0 => {
          low = middle + 1
        }
        case x if x == 0 => {
          return true
        }
      }
    }

    return false
  }
}

object Gene {
  def fromCodonVector(cv: Vector[Codon]): Gene = Gene(cv)

  def fromBitVector(bv: BitVector): Gene = ???

  val p: P[Gene] = manyN(1, Codon.p).map({ cl => Gene.fromCodonVector(cl.toVector) })
}

///==================================================================================================================///
/// PARSERS                                                                                                          ///
///==================================================================================================================///

object Parsers {
  val entry = (Gene.p <* eof)
  
  val nucleotide = Nucleotide.p
  val codon = Codon.p
  val gene = Gene.p
}
