import java.io.{File, PrintWriter}

import core.{Game, GameMap}
import enumeration.Color

import scala.io.Source

object Main {

  def main(args: Array[String]): Unit = {
    val map = readMap()
    Game.init(map)
    val point = Game.search(Color.WHITE)
    GameMap.setColor(point, Color.WHITE)
    printMap()

    println()
    Game.resultList.foreach(point => {
      print("(" + point.x + "," + point.y + ") ")
    })
    println()
    println(point.x + " " + point.y)
  }

  def readMap(): Array[Array[Color.Value]] = {
    val iterator = Source.fromFile("Komuku-Game/src/input.txt").getLines()
    val boardSize = iterator.next().toInt
    val map = Array.ofDim[Color.Value](boardSize, boardSize)
    for (i <- 0 until boardSize) {
      val line = iterator.next()
      for (j <- 0 until boardSize) {
        line.charAt(j) match {
          case '□' => map(i)(j) = Color.NULL
          case '×' => map(i)(j) = Color.BLACK
          case '●' => map(i)(j) = Color.WHITE
        }
      }
    }
    map
  }

  def printMap(): Unit = {
    val writer = new PrintWriter(new File("Komuku-Game/src/input.txt"))
    var content = GameMap.map.size + "\n"
    for (i <- GameMap.map.indices) {
      for (j <- GameMap.map.indices) {
        GameMap.map(i)(j) match {
          case Color.NULL => content += '□'
          case Color.BLACK => content += '×'
          case Color.WHITE => content += '●'
        }
      }
      content += "\n"
    }
    content += "●×"
    writer.write(content)
    writer.close()
  }
}
