package com.echo_system.game.utilitiy

import scala.collection.mutable.{ArrayBuffer, Buffer}

class N2DArray[T <: AnyVal] private(val shape: (Int, Int), val array: Seq[T]) extends Seq[T] {

  def roll(x: Int, y: Int): N2DArray[T] = {
    new N2DArray(shape, N2DArray.rotatey(N2DArray.rotatex(array.grouped(shape._1).toVector, x), y).flatten)
  }

  def +(x : N2DArray[T])(implicit num: Numeric[T]) : N2DArray[T] = {
    new N2DArray[T](shape, (array, x.array).zipped.map((a, b) => num.plus(a, b)))
  }

  def +(x : T)(implicit num: Numeric[T]) : N2DArray[T] = {
    new N2DArray[T](shape, array.map(a => num.plus(a, x)))
  }

  def *(x : N2DArray[T])(implicit num: Numeric[T]) : N2DArray[T] = {
    new N2DArray[T](shape, (array, x.array).zipped.map((a, b) => num.times(a, b)))
  }

  def *(x : T)(implicit num: Numeric[T]) : N2DArray[T] = {
    new N2DArray[T](shape, array.map(a => num.times(a, x)))
  }

  def /(x : N2DArray[T])(implicit num: Fractional[T]) : N2DArray[T] = {
    new N2DArray[T](shape, (array, x.array).zipped.map((a, b) => num.div(a, b)))
  }

  def /(x : T)(implicit num: Fractional[T]) : N2DArray[T] = {
    new N2DArray[T](shape, array.map(a => num.div(a, x)))
  }

  override def length = array.length

  override def apply(idx: Int) = array(idx)

  override def iterator = array.iterator
}

object N2DArray {

  def curl[T <: AnyVal](ux: N2DArray[T], uy: N2DArray[T])(implicit num: Numeric[T]): N2DArray[T] = {
    val xs = ux.array.combinations(ux.shape._1).toBuffer
    val ys = uy.array.combinations(uy.shape._1).toBuffer
    val p1 = rotatey(xs, 1)
    val n2 = rotatey(xs, -1)
    val n1 = rotatex(ys, 1)
    val p2 = rotatex(ys, -1)
    val a: Seq[Seq[T]] = (0 until xs.size).map(x => (0 until xs(0).size).map(y => num.minus(num.minus(num.plus(p1(x)(y), p2(x)(y)), n1(x)(y)), n2(x)(y))))
    return array[T](a)
  }

  def rotatex[T <: AnyVal](array: Seq[Seq[T]], x: Int): Seq[Seq[T]] = {
    val x0 = Math.floorMod(-x, array.length)
    array.drop(x0) ++ array.take(x0)
  }

  def rotatey[T <: AnyVal](array: Seq[Seq[T]], y: Int): Seq[Seq[T]] = {
    val y0 = Math.floorMod(-y, array(0).length)
    array.map(a => a.drop(y0) ++ a.take(y0))
  }

  def array[T <: AnyVal](a: Seq[Seq[T]]): N2DArray[T] = {
    val a_flat = a.flatten
    val t = (a.length, a(0).length)
    if (t._1 * t._2 == a_flat.length) {
      return new N2DArray[T](t, a_flat)
    } else {
      val ini: T = a(0)(0)
      return new N2DArray[T](t, Vector.fill(t._1 * t._2)(ini))
    }
  }

  def fill[T <: AnyVal](shape: (Int, Int))(init: T): N2DArray[T] = {
    new N2DArray[T](shape, Vector.fill(shape._1 * shape._2)(init))
  }
}