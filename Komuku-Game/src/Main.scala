import core.Game
import enumeration.Color

import scala.io.Source

object Main {

  def main(args: Array[String]): Unit = {
    val map = readMap()
    Game.init(map)
  }

  def readMap(): Array[Array[Color.Value]] = {
    val iterator = Source.fromFile("Komuku-core.Game/src/input.txt").getLines()
    val boardSize = iterator.next().toInt
    val map = Array.ofDim[Color.Value](boardSize, boardSize)
    for (i <- 0 until boardSize - 1) {
      val line = iterator.next()
      for (j <- 0 until boardSize - 1) {
        line.charAt(j) match {
          case '□' => map(i)(j) = Color.NULL
          case '×' => map(i)(j) = Color.BLACK
          case '●' => map(i)(j) = Color.WHITE
        }
      }
    }
    map
  }
}
