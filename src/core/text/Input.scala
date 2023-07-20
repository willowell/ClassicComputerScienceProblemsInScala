package core.text.input

import core.text.read.{given, *}

trait Input[A: Read] {
  def input[A: Read](msg: String)(using a: Read[A]): Option[A] = {
    val inp = promptLine(msg)
    
    a.read(inp)
  }

  def unsafeInput[A: Read](msg: String)(using a: Read[A]): A = {
    val inp = promptLine(msg)

    a.unsafeRead(inp)
  }
}

// magic!
given inputFromReadA[A: Read]: Input[A] with {}

/** Prompt the user to enter a line of text on STDIN.
  * 
  * @param msg Message to print to STDOUT.
  * @return Entered string
  */
def promptLine(msg: String): String = {
  Console.println(msg)
  Console.flush()

  val line: String | Null = scala.io.StdIn.readLine()
  
  if line != null then line else ""
}

def prompt[A: Read](msg: String, validator: A => Boolean)(using a: Input[A]): A = {
  import a.*

  val res = input(msg)

  res match {
    case Some(x) if validator(x) => x
    case _                          => {
      println("Invalid input. Please try again.")
      prompt(msg, validator)
    }
  }
}

def unsafePrompt[A: Read](msg: String, validator: A => Boolean)(using a: Input[A]): A = {
  import a.*

  val res = unsafeInput(msg)

  if (validator(res)) {
    res
  } else {
    println("Invalid input. Please try again.")
    unsafePrompt(msg, validator)
  }
}


def yesOrNo(msg: String): Boolean =
  prompt(msg ++ " (y/n): ", (x) => true)
