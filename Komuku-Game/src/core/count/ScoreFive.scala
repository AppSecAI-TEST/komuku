package core.count

import core.GameMap
import entity.Point

object ScoreFive extends ScoreBase {
  override def getScore(point: Point): Int = {
    var value = 0
    val length = 5
    for (i <- 0 until 4) {
      if (GameMap.checkColors(ScoreData.currentColor, point, i, 0, length - 1)) {
        value += ScoreData.scoreOpen(length)
      } else if (GameMap.checkColors(ScoreData.otherColor, point, i, 0, length - 1)) {
        value -= ScoreData.scoreOpen(length)
      }
    }
    value
  }
}
