package com.example.pong

import android.graphics.RectF
import com.example.pong.Paddle.Move.*

class Paddle(screenX: Int, screenY: Int) {
    private val length: Float = 130f
    private val height: Float = 20f
    private var x: Float = (screenX / 2).toFloat()
    private val y: Float = (screenY - 20).toFloat()
    val rectangle: RectF = RectF(x, y, x + length, y + height)

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

        internal val speed = 350f

        abstract fun step(): Float
    }
}