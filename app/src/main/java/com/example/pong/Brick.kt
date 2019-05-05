package com.example.pong

import android.graphics.RectF

class Brick(row: Int, column: Int, screenWidth: Int, screenHeight: Int) {
    private val topMargin = 120
    private val padding = 1
    private val width = screenWidth / 4
    private val height = screenHeight / 20

    var rectangle: RectF = RectF(
        (column * width + padding).toFloat(),
        topMargin + (row * height + padding).toFloat(),
        (column * width + width - padding).toFloat(),
        topMargin + (row * height + height - padding).toFloat()
    )

    var visible: Boolean = true
}