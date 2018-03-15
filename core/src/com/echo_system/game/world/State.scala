package com.echo_system.game.world

import com.badlogic.gdx.graphics.g2d.SpriteBatch

trait State {
  val pos: (Int, Int)


  def init(): Unit
  def render(batch: SpriteBatch): Unit
  def onUpdate(): Unit

}
