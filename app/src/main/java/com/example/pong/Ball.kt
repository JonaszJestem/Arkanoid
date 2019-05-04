package com.example.pong

import android.graphics.RectF
import java.lang.Math.abs

import java.util.Random

class Ball {
    var rectangle: RectF = RectF()
    private var horizontalSpeed: Float = 200f
    private var verticalSpeed: Float = -600f
    private var ballWidth = 30f
    private var ballHeight = 30f


    fun update(fps: Long) {
        horizontalSpeed = if (abs(horizontalSpeed) < 100) 200f else horizontalSpeed

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

    fun reset(paddle: Paddle) {
        val paddleCenter = paddle.rectangle.left + (paddle.rectangle.right - paddle.rectangle.left) / 2
        val paddleTop = paddle.rectangle.top

        rectangle.left = paddleCenter - ballWidth / 2
        rectangle.right = paddleCenter + ballWidth / 2
        rectangle.top = paddleTop + ballHeight
        rectangle.bottom = paddleTop
    }
}