package core

import entity.Point
import enumeration.Color

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object Rank {

  val debug = false

  val sequenceWeight = Array(0, 1, 5, 10, 100, 1000);

  def getExpandPoints: List[Point] = {
    val result = ArrayBuffer[Point]()
    val score = ArrayBuffer[Int]()

    val signal: Array[Array[Boolean]] = Array.ofDim(GameMap.map.length, GameMap.map.length)
    val range = 3

    for (i <- GameMap.map.indices)
      for (j <- GameMap.map.indices)
        if (GameMap.map(i)(j) != Color.NULL) {
          findRange(new Point(i, j), range, signal)
        }

    for (i <- GameMap.map.indices)
      for (j <- GameMap.map.indices)
        if (GameMap.map(i)(j) == Color.NULL && signal(i)(j)) {
          val point = new Point(i, j)
          result.append(point)
          score.append(getScore(point))
        }
    sort(0, score.size - 1, result, score)

    if (debug) {
      printResult(result)
    }
    result.toList
  }

  def findRange(point: Point, step: Int, signal: Array[Array[Boolean]]): Unit = {
    if (step == 0) {
      return
    }
    if (signal(point.x)(point.y)) {
      return
    }
    signal(point.x)(point.y) = true
    for (i <- 0 until 8) {
      val fresh = GameMap.getRelatePoint(point, i, 1)
      if (GameMap.reachable(fresh))
        if (GameMap.getColor(fresh) == Color.NULL) {
          findRange(fresh, step - 1, signal)
        }
    }
  }

  def getScore(point: Point): Int = {
    var value = 0
    for (i <- 0 until 8) {
      val fresh = GameMap.getRelatePoint(point, i, 1)
      if (GameMap.reachable(fresh)) {
        val color = GameMap.getColor(fresh)
        if (color != Color.NULL) {
          var length = GameMap.getMaxSequence(color, fresh, i)
          if (length > 5) {
            length = 5
          }
          value += sequenceWeight(length)
        }
      }
    }
    value
  }

  def sort(left: Int, right: Int, points: ArrayBuffer[Point], scores: ArrayBuffer[Int]): Unit = {
    var x = left
    var y = right
    val mid = scores((x + y) / 2)
    while (x < y) {
      while (scores(x) > mid) x += 1
      while (scores(y) < mid) y -= 1
      if (x <= y) {
        val temp = scores(x)
        scores(x) = scores(y)
        scores(y) = temp
        val point = points(x)
        points(x) = points(y)
        points(y) = point
        x += 1
        y -= 1
      }
    }
    if (x < right) sort(x, right, points, scores)
    if (left < y) sort(left, y, points, scores)
  }

  def printResult(result: ArrayBuffer[Point]): Unit = {
    result.foreach(point => {
      println(point.x + " " + point.y)
    })
    print(result.size)
  }
}
