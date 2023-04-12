package core.math.ordering

enum Ordering:
  case
    /** Less Than */ LT,
    /** Greater Than */ GT,
    /** Equal */ EQ

given CanEqual[Ordering, Ordering] = CanEqual.derived

object Ordering:
  def fromInt(x: Int): Ordering = x match
    case x if x < 0 => LT
    case x if x > 0 => GT
    case _          => EQ
  
  def fromIntComparison(x: Int, y: Int): Ordering =
    Ordering.fromInt apply (x compare y)

extension (x: Int)
  /**
    * blah
    *
    * @param y
    * @return
    */
  def <=>(y: Int): Ordering = Ordering.fromIntComparison(x, y)
