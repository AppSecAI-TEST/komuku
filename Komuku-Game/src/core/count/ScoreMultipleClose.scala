package core.count

import core.GameMap
import entity.Point
import enumeration.Color

import scala.util.control.Breaks.{break, breakable}

object ScoreMultipleClose extends ScoreBase {
  override def getScore(point: Point): Int = {
    var value = 0
    val color = GameMap.getColor(point)
    if (color == Color.NULL) {
      return value
    }
    for (i <- 0 until 8) {
      //scala居然没有的标准的continue和break
      breakable {
        for (k <- 1 to 2) {
          val tail = GameMap.getRelatePoint(point, i, k + 1)
          if (!GameMap.reachable(tail)) {
            break()
          }
          if (GameMap.getColor(tail) == Color.NULL) {
            if (GameMap.checkColors(Color.getOtherColor(color), point, i, 1, k)) {
              if (color == ScoreData.currentColor)
                value += ScoreData.scoreClose(k)
              else
                value -= ScoreData.scoreClose(k)
            }
            break()
          }
        }
      }
    }
    value
  }
}
