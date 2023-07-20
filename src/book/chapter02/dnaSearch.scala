/// # DNA Search
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter2/Gene.java
package book.chapter02.dnaSearch

import core.data.gene.{Parsers as GeneParsers, *}
import core.text.fansi.*
import core.text.prettyprinting.toYesOrNo

import parsley.{Failure, Success}
import parsley.character.*
import parsley.combinator.*

@main def run(): Unit = {
  val testGene = "ACGTGGCTCTCTAACGTACGTACGTACGGGGTTTATATATACCCTAGGACTCCCTTT"

  val numNucleotides = testGene.length
  val isOrNot = if numNucleotides % 3 == 0 then "is" else "is not"
  val timesDivisible = if numNucleotides % 3 == 0 then s" (${numNucleotides / 3} times)" else ""

  println(s"There are $numNucleotides nucleotides in the gene, which $isOrNot divisible by 3.$timesDivisible")

  val result = GeneParsers.entry.parse(testGene)

  result match {
    case Success(gene: Gene) => {
      val prettyGene = gene.codons
        .flatMap { _.toNucleotideVector }
        .map { _.toLetterStyled }
        .mkString

      println(s"Result: `$prettyGene`\n\n")

      val acgRes = (GeneParsers.codon <* eof).parse("ACG")
      val gatRes = (GeneParsers.codon <* eof).parse("GAT")

      acgRes.foreach { acg =>
        val doesGeneContainAcgLinear = gene.linearContains(acg)
        val doesGeneContainAcgBinary = gene.binaryContains(acg)

        println(s"[${y("LINEAR SEARCH")}] Does the gene contain the codon `ACG`? ${doesGeneContainAcgLinear.toYesOrNo}")
        println(s"[${b("BINARY SEARCH")}] Does the gene contain the codon `ACG`? ${doesGeneContainAcgBinary.toYesOrNo}")
      }

      gatRes.foreach { gat =>
        val doesGeneContainGatLinear = gene.linearContains(gat)
        val doesGeneContainGatBinary = gene.binaryContains(gat)

        println(s"[${y("LINEAR SEARCH")}] Does the gene contain the codon `GAT`? ${doesGeneContainGatLinear.toYesOrNo}")
        println(s"[${b("BINARY SEARCH")}] Does the gene contain the codon `GAT`? ${doesGeneContainGatBinary.toYesOrNo}")
      }

    }
    case Failure(err) => {
      println("There was an error parsing the test gene.")
      println(err.toString)
    }
  }
}
