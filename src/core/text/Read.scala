package core.text.read

trait Read[A] {
  def read(input: String): Option[A]
  def unsafeRead(input: String): A
}

given readBoolean: Read[Boolean] with {
  def read(input: String): Option[Boolean] = input match {
    case "True"  | "true"  | "t" | "Yes" | "yes" | "y" => Some(true)
    case "False" | "false" | "f" | "No"  | "no"  | "n" => Some(false)
    case _                                             => None
  }

  def unsafeRead(input: String): Boolean = input match {
    case "True"  | "true"  | "t" | "Yes" | "yes" | "y" => true
    case "False" | "false" | "f" | "No"  | "no"  | "n" => false
    case _                                             => throw new RuntimeException("Read failure")
  }
}

given readInt: Read[Int] with {
  def read(input: String): Option[Int] = input.toIntOption
  def unsafeRead(input: String): Int = input.toInt
}

given readDouble: Read[Double] with {
  def read(input: String): Option[Double] = input.toDoubleOption
  def unsafeRead(input: String): Double = input.toDouble
}
