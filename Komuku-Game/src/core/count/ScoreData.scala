package core.count

import enumeration.Color

object ScoreData {

  var scoreOpen = Array(0, 1, 10, 100, 10000, 1000000)
  var scoreClose = Array(0, 1, 5, 50, 100, 100, 1000000)

  var currentColor: Color.Value = _
  var otherColor: Color.Value = _

  def init(color: Color.Value): Unit = {
    this.currentColor = color
    this.otherColor = Color.getOtherColor(color)
  }
 }
