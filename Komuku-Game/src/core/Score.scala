package core

import enumeration.Color.Color

object Score {

  val directX = Array(0, 1, 1, 1, 0, -1, -1, -1)
  val directY = Array(1, 1, 0, -1, -1, 1, 0, -1)

  val scoreOne = 1
  val scoreOneClose = 2
  val scoreTwo = 10
  val scoreTwoClose = 5
  val scoreThree = 100
  val scoreThreeClose = 50
  val scoreFour = 10000
  val scoreForeClose = 100

  def getMapScore(map: Array[Array[Color]]): Int = {
    var score = 0
    score += 1
    score
  }


}
