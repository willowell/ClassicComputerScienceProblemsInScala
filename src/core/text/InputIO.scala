package core.text.inputio

import core.text.read.*

import cats.*
import cats.effect.*
import cats.syntax.all.*

def promptLine(msg: String): IO[String] = {
  IO.println(msg)
  >> IO.blocking { scala.Console.flush() }
  >> IO.readLine
}

def input[A: Read](msg: String)(using a: Read[A]): IO[Option[A]] = {
  promptLine(msg).map { a.read(_) }
}

def prompt[A: Read](msg: String)(validator: A => Boolean): IO[A] = {
  input(msg) >>= {
    case Some(value: A) if validator(value) => IO.pure(value) 
    case Some(_) | None                     => {
      IO.println("Invalid input") *> prompt(msg)(validator) 
    }
  }
}

def yesOrNo(msg: String)(using Read[Boolean]): IO[Boolean] = {
  prompt(msg ++ " (y/n): ") { _ => true }
}
