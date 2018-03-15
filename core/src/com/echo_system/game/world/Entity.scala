package com.echo_system.game.world

import com.badlogic.gdx.graphics.g2d.SpriteBatch

trait Entity {
  val pos: (Int, Int)
  val field: Field

  def init(): Unit
  def render(batch: SpriteBatch): Unit
  def onUpdate(): Unit

}
