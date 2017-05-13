package core.count

import core.GameMap
import entity.Point
import enumeration.Color

object ScoreOne extends ScoreBase {
  override def getScore(point: Point): Int = {
    var value = 0
    if (GameMap.getColor(point) == Color.NULL) {
      return value
    }
    for (i <- 0 until 8) {
      val fresh = GameMap.getRelatePoint(point, i, 1)
      if (!GameMap.reachable(fresh))
        return value
      if (GameMap.getColor(fresh) != Color.NULL)
        return value
    }
    if (GameMap.getColor(point) == ScoreData.currentColor) {
      value += 1
    } else {
      value -= 1
    }
    value
  }
}
