package core.math.ordo

enum Ordo {
  /** Less Than */
  case LT
  /** Greater Than */
  case GT
  /** Equal */
  case EQ
}

given CanEqual[Ordo, Ordo] = CanEqual.derived

object Ordo {
  def fromInt(x: Int): Ordo = x match {
    case x if x < 0 => LT
    case x if x > 0 => GT
    case _          => EQ
  }
  
  def fromIntComparison(x: Int, y: Int): Ordo =
    Ordo.fromInt `apply` (x compare y)
}

extension (x: Int)
  /**
    * blah
    *
    * @param y
    * @return
    */
  def <=>(y: Int): Ordo = Ordo.fromIntComparison(x, y)
