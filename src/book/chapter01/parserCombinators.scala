package book.chapter01.parserCombinators

import core.text.inputio.*

import cats.*
import cats.effect.*
import cats.syntax.all.*

import parsley.{Parsley as P}
import parsley.character.*
import parsley.combinator.*
import parsley.Success
import parsley.Failure

enum RGB {
  case R, G, B
}

val pRGB: P[RGB] = choice(
  char('r') #> RGB.R,
  char('g') #> RGB.G,
  char('b') #> RGB.B,
)

val silly = string("hello") *> whitespace *> string("world") *> char('!')

object Main extends IOApp.Simple {
  override final val run: IO[Unit] = for {
    _ <- List("rgb", "foo").traverse { s =>
      pRGB.parse(s) match {
        case Success(x) => (
          IO.println(s"Parsed `$s` as $x")
        )

        case Failure(err) => (
          IO.println(s"Error: `$s` gave error $err")
        )
      }
    }
    
  } yield ()
}
