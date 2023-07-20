/// # Bit Vectors
///
package book.chapter01.bitVectors

import scodec.bits.*

@main def run(): Unit = {
  val a = bin"01"

  val b = BitVector.bits(Array(false, true))

  val c = bin"0" ++ bin"1"


  val d = for {
    encd <- ByteVector.encodeUtf8("Hello, world!")
    decd <- encd.decodeUtf8
  } yield decd

  val e = d.map({ _ == "Hello, world!" }).getOrElse(false)

  val ADENINE  = bin"00"
  val CYTOSINE = bin"10"
  val GUANINE  = bin"01"
  val THYMINE  = bin"11"

  // Note the backticks surrounding the values!
  // Without these, Scala will interpret these as placeholder values.
  // With backticks, Scala will match on the value of these `val`s.
  c match {
    case `ADENINE`  => { println("Got Adenine") }
    case `CYTOSINE` => { println("Got Cytosine") }
    case `GUANINE`  => { println("Got Guanine") }
    case `THYMINE`  => { println("Got Thymine") }
    case _          => { println("Got something else") }
  }
}
