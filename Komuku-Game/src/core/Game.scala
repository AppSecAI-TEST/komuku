package core

import entity.Point
import enumeration.Color

import scala.collection.mutable.ArrayBuffer

object Game {

  val searchDeep = 4

  var resultPoint: Point = _

  var count = 0

  def init(map: Array[Array[Color.Value]]): Unit = {
    GameMap.map = map
  }

  def search(color: Color.Value): Point = {
    resultPoint = null
    count = 0
    getMaxScore(searchDeep, color, Int.MaxValue)
    resultPoint
  }

  def getMaxScore(level: Int, color: Color.Value, parentMin: Int): Int = {
    if (level == 0) {
      count += 1
      return Score.getMapScore(color)
    }
    var max = Int.MinValue
    val points: List[Point] = getExpandPoints
    points.foreach(point => {
      GameMap.setColor(point, color)
      val value = getMinScore(level - 1, Color.getOtherColor(color), max)
//      if (value > parentMin) {
//        return value
//      }
      if (value > max) {
        max = value
      }
      if (level == searchDeep) {
        println(point.x + " " + point.y + ": " + value + " count: " + count)
      }
      GameMap.setColor(point, Color.NULL)
    })
    max
  }

  def getMinScore(level: Int, color: Color.Value, parentMin: Int): Int = {
    var min = Int.MaxValue
    val points: List[Point] = getExpandPoints
    points.foreach(point => {
      GameMap.setColor(point, color)
      val value = getMaxScore(level - 1, Color.getOtherColor(color), min)
//      if (value < parentMin) {
//        return value
//      }
      if (value < min) {
        min = value
      }
      GameMap.setColor(point, Color.NULL)
    })
    min
  }

  def getExpandPoints: List[Point] = {
    val result = ArrayBuffer[Point]()
    for (i <- GameMap.map.indices)
      for (j <- GameMap.map.indices) {
        if (GameMap.map(i)(j) == Color.NULL)
          result.append(new Point(i, j))
      }
    result.toList
  }
}
