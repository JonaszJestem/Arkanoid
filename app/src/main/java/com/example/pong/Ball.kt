package com.example.pong

import android.graphics.RectF

import java.util.Random

class Ball {
    var rectangle: RectF = RectF()
    private var horizontalSpeed: Float = 200f
    private var verticalSpeed: Float = -400f
    private var ballWidth = 10f
    private var ballHeight = 10f


    fun update(fps: Long) {
        rectangle.left = rectangle.left + horizontalSpeed / fps
        rectangle.top = rectangle.top + verticalSpeed / fps
        rectangle.right = rectangle.left + ballWidth
        rectangle.bottom = rectangle.top - ballHeight
    }

    fun bounceVertical() {
        verticalSpeed = -verticalSpeed
    }

    fun bounceHorizontal() {
        horizontalSpeed = -horizontalSpeed
    }

    fun bounceHorizontalAtRandom() {
        if (Random().nextInt() % 2 == 0) {
            bounceHorizontal()
        }
    }

    fun clearObstacleY(y: Float) {
        rectangle.bottom = y
        rectangle.top = y - ballHeight
    }

    fun clearObstacleX(x: Float) {
        rectangle.left = x
        rectangle.right = x + ballWidth
    }

    fun reset(x: Int, y: Int) {
        rectangle.left = (x / 2).toFloat()
        rectangle.top = (y - 20).toFloat()
        rectangle.right = x / 2 + ballWidth
        rectangle.bottom = y.toFloat() - 20f - ballHeight
    }
}