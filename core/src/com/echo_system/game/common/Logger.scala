package com.echo_system.game.common

object Logger {

  import com.badlogic.gdx.Gdx

  var time = 0L

  def info(s: String): Unit = {
    Gdx.app.log("aut", s)
  }

  def info(args: Double*): Unit = {
    var s = ""
    for (s1 <- args) {
      s = s + "par : " + s1
    }
    Gdx.app.log("aut", s)
  }

  def error(s: String): Unit = {
    Gdx.app.error("aut", s)
  }

  def debug(s: String): Unit = {
    Gdx.app.debug("aut", s)
  }

  def timerBegin(): Unit = {
    time = System.nanoTime
  }

  def timerEnd(s: String): Unit = {
    val l = System.nanoTime
    info(s + " " + (l - time) / 1000 + "us")
    time = l
  }
}
