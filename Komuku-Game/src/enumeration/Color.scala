package enumeration

object Color extends Enumeration {
  type Color = Value
  val BLACK, WHITE, NULL = Value

  def getOtherColor(color: Color): Color = {
    if (color == BLACK) {
      return WHITE
    }
    if (color == WHITE) {
      return BLACK
    }
    NULL
  }
}
