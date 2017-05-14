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
      if (GameMap.reachable(fresh)) {
        if (GameMap.getColor(fresh) == ScoreData.currentColor)
          value += 2
        if (GameMap.getColor(fresh) == Color.getOtherColor(ScoreData.currentColor))
          value += 2
        if (GameMap.getColor(fresh) == Color.NULL)
          value += 1
      }
    }
    if (GameMap.getColor(point) != ScoreData.currentColor) {
      value = -value
    }
    value
  }
}
