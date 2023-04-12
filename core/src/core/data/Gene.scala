package core.data.gene

import scala.collection.immutable.Vector

enum Nucleotide:
  case
    Adenine,
    Cytosine,
    Guanine,
    Thymine

  def toLetter: Char =
    import Nucleotide._
    this match
      case Adenine  => 'A'
      case Cytosine => 'C'
      case Guanine  => 'G'
      case Thymine  => 'T'

given CanEqual[Nucleotide, Nucleotide] = CanEqual.derived

case class Codon(
  first: Nucleotide,
  second: Nucleotide,
  third: Nucleotide
)


