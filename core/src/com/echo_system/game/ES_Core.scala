package com.echo_system.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.echo_system.game.common.InputHandler
import com.echo_system.game.renderer.RenderHandler
import com.echo_system.game.world.FieldManager

class ES_Core extends ApplicationAdapter {
  private var batch: SpriteBatch = _
  private var img: Texture = _

  override def create() {
    batch = new SpriteBatch
    img = new Texture("badlogic.jpg")
    Gdx.input.setInputProcessor(InputHandler)
    RenderHandler.init()
    FieldManager.init()
  }

  var time = 0

  override def render() {
    time += 1
    if(time == 1){
      time = 0
      FieldManager.onUpdate()
    }

    RenderHandler.preRender()
    RenderHandler.render()
  }

  override def dispose(): Unit = {
    RenderHandler.dispose()
  }

  override def resize(width: Int, height: Int): Unit = {
    RenderHandler.resize(width, height)
  }
}