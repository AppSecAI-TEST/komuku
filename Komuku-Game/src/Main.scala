import java.io.{File, PrintWriter}

import core.{Config, Game}
import entity.Point
import enumeration.{Color, Deep}

import scala.io.Source

object Main {

  val game = new Game

  val map = readMap()

  var progress: Int = 0

  var currentProgress: Int = 0

  var result: Point = _

  def main(args: Array[String]): Unit = {
    println("正在初始化数据...")
    game.init(map, Deep.SIX)
    println("开始计算...")
    if (game.win() != null) {
      println(game.win() + " win")
      return
    }
    listen
    result = game.search(Color.WHITE)
    map(result.getX)(result.getY) = Color.WHITE
    printMap()
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

  def listen: Unit = {
    val thread = new Thread(new Runnable {
      override def run(): Unit = {
        while (true) {
          val data = game.getCountData
          if (data.getAllStep > 0 && progress == 0) {
            progress = data.getAllStep
            for (i <- 1 to progress) {
              print("=")
            }
            println
          }
          if (data.getFinishStep > currentProgress) {
            for (i <- 1 to data.getFinishStep - currentProgress) {
              print(">")
            }
            currentProgress = data.getFinishStep
          }
          Thread.sleep(1000)
          if (progress == currentProgress && progress > 0) {
            println();
            println(result.getX + " " + result.getY)
            return
          }
        }
      }
    }).start()
  }
}
