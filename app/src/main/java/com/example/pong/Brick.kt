package com.example.pong

import android.graphics.RectF

class Brick(row: Int, column: Int, width: Int, height: Int) {
    private val padding = 1
    val rectangle: RectF = RectF(
        (column * width + padding).toFloat(),
        (row * height + padding).toFloat(),
        (column * width + width - padding).toFloat(),
        (row * height + height - padding).toFloat()
    )

    var visibility: Boolean = true
}