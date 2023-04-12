package core.data.maze

enum Cell:
  case Empty, Blocked, Start, Goal, Path

  def toCharacter(): String = ""

