package core

import enumeration.Color

object Game {

  var map: Array[Array[Color.Value]] = _

  def init(map: Array[Array[Color.Value]]): Unit = {
    this.map = map
  }


}
