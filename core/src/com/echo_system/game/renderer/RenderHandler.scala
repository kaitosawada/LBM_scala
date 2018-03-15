package com.echo_system.game.renderer

import com.badlogic.gdx.graphics.{Color, Texture}
import com.echo_system.game.renderer.RenderHandler._
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.IntArray
import com.badlogic.gdx.utils.viewport.Viewport
import com.echo_system.game.world.FieldManager

object RenderHandler {

  val batch = new SpriteBatch
  val fpslogger = new FPSLogger
  val font = new BitmapFont
  var camera = new OrthographicCamera(1280, 720)
  var viewport = new FitViewport(1280, 720, camera)
  val renderer = new ShapeRenderer
  val background = new Pixmap(1280, 720, Pixmap.Format.RGBA8888)
  val tex = new Texture(background)
  //val colorTheme = Vector(colorInt(225, 36, 115, 255), colorInt(222, 214, 207, 255), colorInt(85, 239, 204, 255), colorInt(64, 79, 61, 255), colorInt(21, 25, 17, 255))

  def init(): Unit = {
    renderer.setAutoShapeType(true)
    background.setColor(Color.DARK_GRAY)
    background.fill()
    tex.draw(background, 0, 0)
  }

  def colorInt(r: Int, g: Int, b: Int, a: Int): Int = (a << 24) | (b << 16) | (g << 8) | r

  private def getPosition(a: Double, b: Double, c: Double) = Math.max(a, Math.min(b, c))

  def preRender(): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    camera.update
    batch.setProjectionMatrix(camera.combined)
    renderer.setProjectionMatrix(camera.combined)
    camera.position.x = 640//getPosition(800, 800, -800 + Core.INSTANCE.world.fixedField.x).toFloat //Core.INSTANCE.player.pos.x;
    camera.position.y = 360//getPosition(450, 450, -450 + Core.INSTANCE.world.fixedField.y).toFloat //Core.INSTANCE.player.pos.y;

  }

  def render(): Unit = {
    fpslogger.log
    batch.begin
    batch.draw(tex, camera.position.x - 640, camera.position.y - 360, 1280, 10)
    FieldManager.render(batch)
    //sprite.draw(batch);
    //font.draw(batch, "Heloo", 200, 400);
    batch.end
    //core.box2d.render(camera);
  }

  def dispose(): Unit = {
    font.dispose()
    batch.dispose()
  }

  def resize(width: Int, height: Int): Unit = {
    viewport.update(width, height)
  }


}
