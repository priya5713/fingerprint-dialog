package com.livinglife.utilities

import java.util.Comparator

class Point2D(x: Double, y: Double) : Comparable<Point2D> {
    val POLAR_ORDER: Comparator<Point2D> = PolarOrder()

    private val x: Double // x coordinate
    private val y: Double // y coordinate

    init {
        var x = x
        var y = y
        if (java.lang.Double.isInfinite(x) || java.lang.Double.isInfinite(y))
            throw IllegalArgumentException("Coordinates must be finite")
        if (java.lang.Double.isNaN(x) || java.lang.Double.isNaN(y))
            throw IllegalArgumentException("Coordinates cannot be NaN")
        if (x == 0.0)
            x = 0.0 // convert -0.0 to +0.0
        if (y == 0.0)
            y = 0.0 // convert -0.0 to +0.0
        this.x = x
        this.y = y
    }

    fun x(): Double {
        return x
    }

    fun y(): Double {
        return y
    }

    fun r(): Double {
        return Math.sqrt(x * x + y * y)
    }

    override fun compareTo(that: Point2D): Int {
        if (this.y < that.y)
            return -1
        if (this.y > that.y)
            return +1
        if (this.x < that.x)
            return -1
        return if (this.x > that.x) +1 else 0
    }

    private inner class PolarOrder : Comparator<Point2D> {
        override fun compare(q1: Point2D, q2: Point2D): Int {
            val dx1 = q1.x - x
            val dy1 = q1.y - y
            val dx2 = q2.x - x
            val dy2 = q2.y - y

            return if (dy1 >= 0 && dy2 < 0)
                -1 // q1 above; q2 below
            else if (dy2 >= 0 && dy1 < 0)
                +1 // q1 below; q2 above
            else if (dy1 == 0.0 && dy2 == 0.0) { // 3-collinear and horizontal
                if (dx1 >= 0 && dx2 < 0)
                    -1
                else if (dx2 >= 0 && dx1 < 0)
                    +1
                else
                    0
            } else
                -ccw(this@Point2D, q1, q2) // both above or below
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other == null)
            return false
        if (other.javaClass != this.javaClass)
            return false
        val that = other as Point2D?
        return this.x == that!!.x && this.y == that.y
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    override fun hashCode(): Int {
        val hashX = x.hashCode()
        val hashY = y.hashCode()
        return 31 * hashX + hashY
    }

    companion object {

        fun ccw(a: Point2D, b: Point2D, c: Point2D): Int {
            val area2 = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)
            return if (area2 < 0)
                -1
            else if (area2 > 0)
                +1
            else
                0
        }
    }
}