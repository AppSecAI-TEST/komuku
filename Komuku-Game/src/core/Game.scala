package core

import java.util.Random

import entity.Point
import enumeration.Color

import scala.collection.mutable.ArrayBuffer

object Game {

  val debug = true

  val searchDeep = 4

  var resultList: ArrayBuffer[Point] = new ArrayBuffer[Point]()

  var resultPoint: Point = _

  var count = 0

  def init(map: Array[Array[Color.Value]]): Unit = {
    GameMap.map = map
  }

  def search(color: Color.Value): Point = {
    resultPoint = null
    resultList.clear()
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
    val points: List[Point] = Rank.getExpandPoints
    points.foreach(point => {
      GameMap.setColor(point, color)
      val value = getMinScore(level - 1, Color.getOtherColor(color), max)
      if (value > parentMin) {
        GameMap.setColor(point, Color.NULL)
        return value
      }
      if (level == searchDeep) {
        if (value >= max) {
          recordResult(point, value, max)
        }
        printInfo(point, points, value)
      }
      if (value >= max) {
        max = value
      }
      GameMap.setColor(point, Color.NULL)
    })
    max
  }

  def getMinScore(level: Int, color: Color.Value, parentMin: Int): Int = {
    var min = Int.MaxValue
    val points: List[Point] = Rank.getExpandPoints
    points.foreach(point => {
      GameMap.setColor(point, color)
      val value = getMaxScore(level - 1, Color.getOtherColor(color), min)
      if (value < parentMin) {
        GameMap.setColor(point, Color.NULL)
        return value
      }
      if (value < min) {
        min = value
      }
      GameMap.setColor(point, Color.NULL)
    })
    min
  }

  def recordResult(point: Point, value: Int, max: Int): Unit = {
    if (value > max) {
      resultList.clear()
    }
    resultList.append(point)
    resultPoint = resultList(new Random().nextInt(resultList.size))
  }

  def printInfo(point: Point, points: List[Point], value: Int): Unit = {
    if (debug) {
      println(point.x + " " + point.y + ": " + value + " count: " + count)
    } else {
      val index = points.indexOf(point)
      if(index == 0){
        println("开始计算...")
        for (i <- points.indices)
          print("=")
        println()
      }
      print(">")
    }
  }
}
