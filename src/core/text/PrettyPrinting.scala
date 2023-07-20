package core.text.prettyprinting

trait Display[A] {
  def display(a: A): String
}

object Display {
  def apply[A: Display]: Display[A] = summon[Display[A]]
}

object Ops {
  def display[A: Display](a: A): String = Display[A].display(a)

  implicit class DisplayOps[A: Display](a: A) {
    def display: String = Display[A].display(a)
  }
}

trait DisplayStyled[A] {
  extension (a: A) def displayStyled: fansi.Str
}

extension (b: Boolean) {
  def toYesOrNo: String = if b then "yes" else "no"
}
