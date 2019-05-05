package com.example.pong

import android.content.SharedPreferences
import android.graphics.RectF

class GameSaver(private val preferences: SharedPreferences) {
    fun getSavedBall(): Ball {
        val bottom = preferences.getFloat(BALL_BOTTOM, 0f)
        val top = preferences.getFloat(BALL_TOP, 0f)
        val right = preferences.getFloat(BALL_RIGHT, 0f)
        val left = preferences.getFloat(BALL_LEFT, 0f)

        return Ball().apply {
            rectangle = RectF(left, top, right, bottom)
        }
    }

    fun getSavedBricks(): ArrayList<Brick> {
        val bricks = ArrayList<Brick>()
        var i = 0
        while (preferences.contains("$BRICK_VISIBLE$i")) {
            val bottom = preferences.getFloat("$BRICK_BOTTOM$i", 0f)
            val top = preferences.getFloat("$BRICK_TOP$i", 0f)
            val right = preferences.getFloat("$BRICK_RIGHT$i", 0f)
            val left = preferences.getFloat("$BRICK_LEFT$i", 0f)
            val isVisible = preferences.getBoolean("$BRICK_VISIBLE$i", false)

            val brick = Brick(0, 0, 0, 0).apply {
                rectangle = RectF(left, top, right, bottom)
                visible = isVisible
            }
            bricks.add(brick)
            i++
        }
        return bricks
    }

    fun getSavedPaddle(): Paddle {
        val bottom = preferences.getFloat(PADDLE_BOTTOM, 0f)
        val top = preferences.getFloat(PADDLE_TOP, 0f)
        val right = preferences.getFloat(PADDLE_RIGHT, 0f)
        val left = preferences.getFloat(PADDLE_LEFT, 0f)

        return Paddle(0, 0).apply {
            rectangle = RectF(left, top, right, bottom)
        }
    }

    fun getSavedPlayer(): Player {
        val lives = preferences.getInt(PLAYER_LIVES, 3)
        val score = preferences.getInt(PLAYER_SCORE, 0)
        return Player(score, lives)
    }

    fun save(
        ball: Ball,
        paddle: Paddle,
        bricks: ArrayList<Brick>,
        player: Player
    ) {
        val editor = preferences.edit()
        saveBall(editor, ball)
        savePaddle(editor, paddle)
        savePlayer(editor, player)
        saveBricks(editor, bricks)
        editor.apply()
    }

    private fun saveBricks(
        editor: SharedPreferences.Editor,
        bricks: ArrayList<Brick>
    ) {
        for (i in 0 until bricks.size) {
            val brick = bricks[i]
            editor
                .putFloat("$BRICK_BOTTOM$i", brick.rectangle.bottom)
                .putFloat("$BRICK_TOP$i", brick.rectangle.top)
                .putFloat("$BRICK_LEFT$i", brick.rectangle.left)
                .putFloat("$BRICK_RIGHT$i", brick.rectangle.right)
                .putBoolean("$BRICK_VISIBLE$i", brick.visible)
        }
    }

    private fun savePlayer(editor: SharedPreferences.Editor, player: Player) {
        editor.putInt(PLAYER_LIVES, player.lives)
            .putInt(PLAYER_SCORE, player.score)
    }

    private fun savePaddle(editor: SharedPreferences.Editor, paddle: Paddle) {
        editor.putFloat(PADDLE_BOTTOM, paddle.rectangle.bottom)
            .putFloat(PADDLE_LEFT, paddle.rectangle.left)
            .putFloat(PADDLE_TOP, paddle.rectangle.top)
            .putFloat(PADDLE_RIGHT, paddle.rectangle.right)
    }

    private fun saveBall(editor: SharedPreferences.Editor, ball: Ball) {
        editor.putFloat(BALL_BOTTOM, ball.rectangle.bottom)
            .putFloat(BALL_TOP, ball.rectangle.top)
            .putFloat(BALL_RIGHT, ball.rectangle.right)
            .putFloat(BALL_LEFT, ball.rectangle.left)
    }

    fun clearPreferences() {
        preferences.edit().clear().apply()
    }

    fun wasSaved(): Boolean {
        return preferences.all.isNotEmpty()
    }

    companion object {
        private const val BALL_LEFT = "BALL_LEFT"
        private const val BALL_TOP = "BALL_TOP"
        private const val BALL_BOTTOM = "BALL_BOTTOM"
        private const val BALL_RIGHT = "BALL_RIGHT"

        private const val PADDLE_LEFT = "PADDLE_LEFT"
        private const val PADDLE_TOP = "PADDLE_TOP"
        private const val PADDLE_BOTTOM = "PADDLE_BOTTOM"
        private const val PADDLE_RIGHT = "PADDLE_RIGHT"

        private const val BRICK_LEFT = "BRICK_LEFT"
        private const val BRICK_TOP = "BRICK_TOP"
        private const val BRICK_BOTTOM = "BRICK_BOTTOM"
        private const val BRICK_RIGHT = "BRICK_RIGHT"
        private const val BRICK_VISIBLE = "BRICK_VISIBLE"

        private const val PLAYER_LIVES = "PLAYER_LIVES"
        private const val PLAYER_SCORE = "PLAYER_SCORE"
    }
}