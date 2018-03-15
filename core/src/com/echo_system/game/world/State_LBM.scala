package com.echo_system.game.world

import com.badlogic.gdx.graphics.g2d.{Batch, SpriteBatch}
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}

import scala.util.Random

class State_LBM(val width: Int, val height: Int, scale: Int = 4, color: Double => Int) extends State {

  val pmap = new Pixmap(width, height, Pixmap.Format.RGBA8888)
  pmap.setColor(Color.DARK_GRAY)
  pmap.fill()
  val tex = new Texture(pmap)

  val viscosity = 0.05

  val lattice = LBM_solver.LATTICE.twoD_8Direction;
  val solver: LBM_solver = new LBM_solver(width, height, 1 / (3 * viscosity + 0.5), lattice)

  val f = lattice.wi.map(a => Array.fill(width * height)(Random.nextDouble() * a))
  val ux = Array.fill[Double](width * height)(0.0)
  val uy = Array.fill[Double](width * height)(0.0)
  val rho = Array.fill[Double](width * height)(0.0)
  val block_raw = Array.fill[Boolean](width * height)(false)
  for (i <- Range(20 + width * 20, 20 + width * 50, width)) {
    block_raw(i) = true
  }
  val block = lattice.iei.map(a => solver.rotate(block_raw, a(0), a(1)))
  val vec = rho.indices.toVector.par
  val vec1 = lattice.wi.indices.toVector.par

  override def init(): Unit = {
  }

  override def render(batch: SpriteBatch): Unit = {
    val buf = pmap.getPixels
    val cl = solver.curl(ux, uy)
    buf.position(0)
    (cl, block_raw).zipped.foreach((a, b) => if (!b) {
      buf.putInt(color(a * 10))
    } else {
      buf.putInt(color(1.0))
    })
    buf.position(0)
    tex.draw(pmap, 0, 0)
    batch.draw(tex, 0, 0, width * scale, height * scale)
  }

  override def onUpdate(): Unit = {
    //Logger.timerBegin()
    for (i <- Range(1, 10)) {
      vec.foreach(a => solver.collide(f, rho, ux, uy, a))
      solver.stream(f, block)
      vec.foreach(a => solver.update(f, rho, ux, uy, a))
      //if (y % width == 80 && y < width * height / 2 && ux(y) < 0.5) ux(y) += 0.01
    }
  }


  override val pos = (0, 0)
}
