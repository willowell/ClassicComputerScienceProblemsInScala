/// # Compressed Genes, featuring Parsley!
///
/// In this example, we compress a gene (meaning a string of arbitrary length including A, C, G, and T)
/// into a vector of bits, where each nucleotide corresponds to a 2-bit number.
///
/// Unlike the Java and Swift implementations, this implementation uses a `Nucleotide` enum to internally represent
/// the gene and uses Parsley to parse the string into a vector of nucleotides.
///
/// This way, we can parse the string right into a `Vector[Nucleotide]` and prevent unexpected characters.
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter1/CompressedGene.java
///
package book.chapter01.compressedGene

import core.data.gene.{Parsers as GeneParsers, *}

import parsley.*
import scodec.bits.BitVector

@main def run(): Unit = {
  val testGene = "TAGGGATTAACCGTTATATATATATAGCCATGGATCGATTATATAGGGATTAACCGTTATATATATATAGCCATGGATCGATTA";

  val parseResult = GeneParsers.entry.parse(testGene)

  parseResult match {
    case Success(gene: Gene) => {
      println(s"Result: `${gene.codons.mkString("\n")}`")

      val originalGeneAsNucleotideVec = gene.codons.flatMap { _.toNucleotideVector }

      val compressedGene = originalGeneAsNucleotideVec.map { _.toBitVector }

      val decompressedGene = compressedGene.map { Nucleotide.fromBitVector(_) }.flatten
      
      val shortGene = originalGeneAsNucleotideVec.map { _.toLetterStyled }.mkString

      val shortDecompressedGene = decompressedGene.map { _.toLetterStyled }.mkString

      println(s"Original gene:     `$testGene`")
      println(s"Parsed gene:       `$shortGene`")

      // Uncomment this to print out the vector of bitvecs representing the compressed gene.
      // println(s"Compressed gene: `$compressedGene`")

      println(s"Decompressed gene: `$shortDecompressedGene`")

      println(
        "Did the compression maintain the same data? "
        ++ (if originalGeneAsNucleotideVec == decompressedGene then "yes" else "no")
      )
    }
    case Failure(err) => {
      println("There was an error parsing the test gene.")
      println(err)
    }
  }
}
