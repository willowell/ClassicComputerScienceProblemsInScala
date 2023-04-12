package core.text.read

trait Read[A]:
  def read(input: String): Option[A]
  def readExn(input: String): A


given Read[Boolean] with
  def read(input: String): Option[Boolean] = input match
    case "True"  | "true"  | "t" | "Yes" | "yes" | "y" => Some(true)
    case "False" | "false" | "f" | "No"  | "no"  | "n" => Some(false)
    case _                                             => None


  def readExn(input: String): Boolean = input match
    case "True"  | "true"  | "t" | "Yes" | "yes" | "y" => true
    case "False" | "false" | "f" | "No"  | "no"  | "n" => false
    case _                                             => throw new RuntimeException("Read failure")


given Read[Int] with
  def read(input: String): Option[Int] = input.toIntOption
  def readExn(input: String): Int = input.toInt

given Read[Double] with
  def read(input: String): Option[Double] = input.toDoubleOption
  def readExn(input: String): Double = input.toDouble
