package core.text.input

import core.text.read.{given, *}

/** Prompt the user to enter a line of text on STDIN.
  * 
  *
  * @param msg Message to print to STDOUT.
  * @return Entered string
  */
def promptLine(msg: String): String =
  Console.println(msg)
  Console.flush()

  val line: String | Null = io.StdIn.readLine()
  
  if line != null then line else ""


def input[A: Read](msg: String)(using a: Read[A]): Option[A] =
  val inp = promptLine(msg)
  
  a.read(inp)


def inputExn[A: Read](msg: String)(using a: Read[A]): A =
  val inp = promptLine(msg)

  a.readExn(inp)


def prompt[A: Read](msg: String, validator: A => Boolean): A =
  val res = input(msg)

  res match
    case Some(x) if validator(x) => x
    case _ =>
      println("Invalid input. Please try again.")
      prompt(msg, validator)
      

def promptExn[A: Read](msg: String, validator: A => Boolean): A =
  val res = inputExn(msg)

  if validator(res) then
    res
  else
    println("Invalid input. Please try again.")
    promptExn(msg, validator)


def yesOrNo(msg: String): Boolean =
  prompt(msg ++ " (y/n): ", (x) => true)
