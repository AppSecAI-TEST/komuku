package core.count

import core.GameMap
import entity.Point
import enumeration.Color

import scala.util.control.Breaks._

object ScoreMultiple extends ScoreBase {

  override def getScore(point: Point): Int = {
    var value = 0
    if (GameMap.getColor(point) != Color.NULL) {
      return value
    }
    for (i <- 0 until 4) {
      breakable {
        for (k <- 2 to 4) {
          val tail = GameMap.getRelatePoint(point, i, k + 1)
          if (!GameMap.reachable(tail)) {
            break()
          }
          if (GameMap.getColor(tail) == Color.NULL) {
            if (GameMap.checkColors(ScoreData.currentColor, point, i, 1, k)) {
              value += ScoreData.scoreOpen(k)
            } else if (GameMap.checkColors(ScoreData.otherColor, point, i, 1, k)) {
              value -= ScoreData.scoreOpen(k)
            }
            break()
          }
        }
      }
    }
    value
  }
}
