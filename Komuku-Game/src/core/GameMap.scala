package core

import entity.Point
import enumeration.Color

object GameMap {

  val directX = Array(0, 1, 1, 1, 0, -1, -1, -1)
  val directY = Array(1, 1, 0, -1, -1, 1, 0, -1)

  var map: Array[Array[Color.Value]] = _

  def reachable(point: Point): Boolean = {
    if (point.x < 0 || point.x >= map.length)
      return false
    if (point.y < 0 || point.y >= map.length)
      return false
    true
  }

  def checkColors(color: Color.Value, point: Point, direct: Int, start: Int, end: Int): Boolean = {
    for (i <- start to end) {
      val fresh = getRelatePoint(point, direct, i)
      if (!GameMap.reachable(fresh)) {
        return false
      }
      if (getColor(fresh) != color) {
        return false
      }
    }
    true
  }


  def getMaxSequence(color: Color.Value, point: Point, direct: Int): Int = {
    var value = 0
    for (i <- 0 to 3) {
      val fresh = getRelatePoint(point, direct, i)
      if (!reachable(fresh)) {
        return value
      }
      if (getColor(fresh) == color) {
        value += 1
      }
      else {
        return value
      }
    }
    value
  }

  def getColor(point: Point): Color.Value = map(point.x)(point.y)

  def setColor(point: Point, color: Color.Value): Unit = map(point.x)(point.y) = color

  def getRelatePoint(point: Point, direct: Int, distance: Int): Point = new Point(point.x + directX(direct) * distance, point.y + directY(direct) * distance)

}
