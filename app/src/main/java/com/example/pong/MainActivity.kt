package com.example.pong

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceView
import android.widget.Toast


class MainActivity : Activity() {
    private lateinit var arkanoid: Arkanoid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val display = windowManager.defaultDisplay
        val displaySize = Point()
        display.getSize(displaySize)

        arkanoid = Arkanoid(this, displaySize.x, displaySize.y)
        setContentView(arkanoid)
    }

    internal inner class Arkanoid(context: Context, private val screenWidth: Int, private val screenHeight: Int) :
        SurfaceView(context),
        Runnable {
        private lateinit var canvas: Canvas
        private var gameThread = Thread(this)
        private var playing: Boolean = false
        private var paused = true

        private var fps: Long = 0
        private var timeThisFrame: Long = 0

        var player: Player = Player()
        private var paddle: Paddle = Paddle(screenWidth, screenHeight)
        var ball: Ball = Ball(screenWidth, screenHeight)
        var bricks: ArrayList<Brick> = ArrayList()

        init {
            paddle
            ball

            createBricksAndRestart()
        }

        private val POINTS_FOR_BRICK = 10

        private fun createBricksAndRestart() {
            ball.reset(screenWidth, screenHeight)

            val brickWidth = screenWidth / 8
            val brickHeight = screenHeight / 10

            for (column in 0..7) {
                for (row in 0..2) {
                    bricks.add(Brick(row, column, brickWidth, brickHeight))
                }
            }
            if (player.lives == 0) {
                player.score = 0
                player.lives = 3
            }
        }

        override fun run() {
            while (playing) {
                val startFrameTime = System.currentTimeMillis()
                if (!paused) {
                    update()
                }
                draw()
                timeThisFrame = System.currentTimeMillis() - startFrameTime
                if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame
                }
            }
        }

        private fun update() {
            paddle.update(fps)
            ball.update(fps)

            for (i in 0 until bricks.size) {
                if (bricks[i].visibility) {
                    if (RectF.intersects(bricks[i].rect, ball.rect)) {
                        bricks[i].setInvisible()
                        ball.reverseYVelocity()
                        player.addPoints(100)
                    }
                }
            }
            if (RectF.intersects(paddle.rect, ball.rect)) {
                ball.setRandomXVelocity()
                ball.reverseYVelocity()
                ball.clearObstacleY(paddle.rect.top - 2)
            }
            if (ball.rect.bottom > screenHeight) {
                ball.reverseYVelocity()
                ball.clearObstacleY((screenHeight - 2).toFloat())

                player.kill()

                if (player.isDead()) {
                    paused = true
                    createBricksAndRestart()
                }
            }

            if (ball.rect.top < 0) {
                ball.reverseYVelocity()
                ball.clearObstacleY(12f)

            }

            if (ball.rect.left < 0) {
                ball.reverseXVelocity()
                ball.clearObstacleX(2f)
            }

            if (ball.rect.right > screenWidth - POINTS_FOR_BRICK) {

                ball.reverseXVelocity()
                ball.clearObstacleX((screenWidth - 22).toFloat())

            }

            if (player.score == bricks.size * POINTS_FOR_BRICK) {
                paused = true
                createBricksAndRestart()
            }
        }

        private fun draw() {
            if (holder.surface.isValid) {
                canvas = holder.lockCanvas()

                canvas.drawColor(Color.argb(255, 26, 128, 182))

                drawBallAndPaddle()

                drawBricks()
                drawPoints()
                handleGameEnd()
                holder.unlockCanvasAndPost(canvas)
            }
        }

        private fun drawBallAndPaddle() {
            Paint().color = Color.argb(255, 255, 255, 255)
            canvas.drawRect(paddle.rect, Paint())
            canvas.drawRect(ball.rect, Paint())
        }

        private fun drawBricks() {
            Paint().color = Color.argb(255, 249, 129, 0)

            for (i in 0 until bricks.size) {
                if (bricks[i].visibility) {
                    canvas.drawRect(bricks[i].rect, Paint())
                }
            }
        }

        private fun drawPoints() {
            Paint().color = Color.argb(255, 255, 255, 255)

            Paint().textSize = 40f
            canvas.drawText("Score: ${player.score} Lives: ${player.lives}", 10f, 50f, Paint())
        }

        private fun handleGameEnd() {
            if (hasWon()) {
                Toast.makeText(context, "You won!", Toast.LENGTH_LONG).show()
            } else if (player.isDead()) {
                Toast.makeText(context, "You lost!", Toast.LENGTH_LONG).show()
            }
        }

        private fun hasWon() = player.score == bricks.size * POINTS_FOR_BRICK

        fun pause() {
            playing = false
            gameThread.join()
        }

        fun resume() {
            playing = true
            gameThread.start()
        }

        override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    paused = false
                    if (motionEvent.x > screenWidth / 2)
                        paddle.setMovementState(paddle.RIGHT)
                    else paddle.setMovementState(paddle.LEFT)
                }

                MotionEvent.ACTION_UP -> paddle.setMovementState(paddle.STOPPED)
            }
            return true
        }
    }

    override fun onResume() {
        super.onResume()
        arkanoid.resume()
    }

    override fun onPause() {
        super.onPause()
        arkanoid.pause()
    }

}