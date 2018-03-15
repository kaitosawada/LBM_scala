package com.echo_system.game.world

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.echo_system.game.common.Logger

import scala.collection.immutable.Vector

object FieldManager {

  var fields: Vector[Field] = Vector.empty[Field]
  val sian = 100 << 8 | 210 << 16 | 20 << 24
  val red = 100 << 8 | 0 << 16 | 255 << 24

  def init(): Unit = {
    val test_field = new Field((0, 0), (1280, 720))
    test_field.states = new State_inmr(320, 180, 4, getHoe) +: test_field.states
    fields = test_field +: fields
    for (i <- fields) {
      i.init()
    }
  }

  def getWhite(alpha: Int, color: Int, maxu: Double): Double => Int = {
    a: Double => (alpha * Math.min(1.0, Math.max(0, a / maxu))).toInt + color
  }

  def getHoe(a: Double): Int = {
    val c = Math.min(1.0, Math.max(0.0, a))
    val i = ((c * 6 - (c * 6).floor) * 255).toInt
    (c * 6).floor.toInt match {
      case 0 => getColor(255, i, 0)
      case 1 => getColor(255 - i, 255, 0)
      case 2 => getColor(0, 255, i)
      case 3 => getColor(0, 255 - i, 255)
      case 4 => getColor(i, 0, 255)
      case 5 => getColor(255, 0, 255 - i)
      case _ => getColor(255, 0, 0)
    }

  }

  def getColor(red: Int, blue: Int, green: Int): Int = {
    red << 24 | blue << 16 | green << 8 | 255
  }

  def getWhite2(alpha: Int, color: Int, color2: Int, maxu: Double): Double => Int = {
    a => {
      val c = Math.min(1.0, Math.max(0, a / maxu))
      (color * c + color2 * (1 - c)).toInt + alpha
    }
  }


  def render(batch: SpriteBatch): Unit = {
    for (i <- fields) {
      i.render(batch)
    }
  }

  def onUpdate(): Unit = {
    for (i <- fields) {
      i.onUpdate()
    }
  }

}
