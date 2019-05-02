package com.example.pong

import android.graphics.RectF

class Brick(row: Int, column: Int, width: Int, height: Int) {
    val rect: RectF

    var visibility: Boolean = false
        private set

    init {

        visibility = true

        val padding = 1

        rect = RectF(
            (column * width + padding).toFloat(),
            (row * height + padding).toFloat(),
            (column * width + width - padding).toFloat(),
            (row * height + height - padding).toFloat()
        )
    }

    fun setInvisible() {
        visibility = false
    }
}