package core.count

import enumeration.Color

object ScoreData {

  var scoreOpen = Array(0, 1, 100, 1000, 100000, 10000000)
  var scoreClose = Array(0, 1, 50, 500, 1000, 1000, 10000000)

  var currentColor: Color.Value = _
  var otherColor: Color.Value = _

  def init(color: Color.Value): Unit = {
    this.currentColor = color
    this.otherColor = Color.getOtherColor(color)
  }
 }
