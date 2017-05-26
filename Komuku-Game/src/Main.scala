import java.io.{File, PrintWriter}

import core.{Config, Game, GameMap}
import enumeration.Color

import scala.io.Source

object Main {

  val game = new Game

  val map = readMap()

  def main(args: Array[String]): Unit = {
    println("正在初始化数据...")
    game.init(map)
    println("开始计算...")
    val point = game.search(Color.WHITE)
    map(point.getX)(point.getY) = Color.WHITE
    printMap()

    println()
    println(point.getX + " " + point.getY)
  }

  def readMap(): Array[Array[Color]] = {
    val iterator = Source.fromFile("Komuku-Game/src/input.txt").getLines()
    val boardSize = iterator.next().toInt
    val map = Array.ofDim[Color](boardSize, boardSize)
    for (i <- 0 until boardSize) {
      val line = iterator.next()
      for (j <- 0 until boardSize) {
        line.charAt(j) match {
          case '.' => map(i)(j) = Color.NULL
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
    var content = Config.size + "\n"
    for (i <- 0 until Config.size) {
      for (j <- 0 until Config.size) {
        map(i)(j) match {
          case Color.NULL => content += '.'
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
