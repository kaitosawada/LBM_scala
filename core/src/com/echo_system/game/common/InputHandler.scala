package com.echo_system.game.common

import com.badlogic.gdx.{Input, InputProcessor}

object InputHandler extends InputProcessor {
  override def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    false
  }

  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    if (button == Input.Buttons.LEFT) {

    }
    false
  }

  override def keyUp(keycode: Int): Boolean = {
    false
  }

  override def scrolled(amount: Int): Boolean = {
    false
  }

  override def keyTyped(character: Char): Boolean = {
    false
  }

  override def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = {
    false
  }

  override def keyDown(keycode: Int): Boolean = {
    false
  }

  override def mouseMoved(screenX: Int, screenY: Int): Boolean = {
    false
  }
}
