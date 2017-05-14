package core

import core.count._
import entity.Point
import enumeration.Color

object Score {

  def getMapScore(color: Color.Value): Int = {
    ScoreData.init(color)
    var value = 0
    for (i <- GameMap.map.indices)
      for (j <- GameMap.map.indices) {
        val point = new Point(i, j)
        value += ScoreOne.getScore(point)
        value += ScoreMultiple.getScore(point)
        value += ScoreFive.getScore(point)
        value += ScoreMultipleClose.getScore(point)
      }
    value
  }

}
