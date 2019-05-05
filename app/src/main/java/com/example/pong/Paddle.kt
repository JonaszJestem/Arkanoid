package com.example.pong

import android.graphics.RectF
import com.example.pong.Paddle.Move.*

class Paddle(screenX: Int, screenY: Int) {
    private val length: Float = 200f
    private val height: Float = 50f
    private var x: Float = (screenX / 2).toFloat()
    private val y: Float = (screenY - 150).toFloat()
    var rectangle: RectF = RectF(x, y, x + length, y + height)

    private var moveDirection = STOPPED


    fun changeMoveDirection(direction: Move) {
        moveDirection = direction
    }

    fun update(fps: Long) {
        x += moveDirection.step() / fps

        rectangle.left = x
        rectangle.right = x + length
    }

    enum class Move {
        STOPPED {
            override fun step(): Float = 0f
        },
        LEFT {
            override fun step(): Float = -speed
        },
        RIGHT {
            override fun step(): Float = speed
        };

        internal val speed = 400f

        abstract fun step(): Float
    }
}