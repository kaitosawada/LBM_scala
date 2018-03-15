package com.echo_system.game.world

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.echo_system.game.common.Logger

class Field(val pos: (Int, Int), val size: (Int, Int)) {

  var entities: Vector[Entity] = Vector.empty[Entity]
  var states: Vector[State] = Vector.empty[State]

  def init(): Unit = {
    for(i <- entities){
      i.init()
    }
    for(i <- states){
      i.init()
    }
  }

  def render(batch: SpriteBatch): Unit = {
    for(i <- entities){
      i.render(batch)
    }
    for(i <- states){
      i.render(batch)
    }
  }

  def onUpdate(): Unit = {
    for(i <- entities){
      i.onUpdate()
    }
    for(i <- states){
      i.onUpdate()
    }
  }

  def interact(): Boolean = {
    false
  }

}
