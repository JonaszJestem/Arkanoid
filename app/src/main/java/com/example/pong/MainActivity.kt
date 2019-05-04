package com.example.pong

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.Color.*
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceView
import com.example.pong.Paddle.Move.*


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

        private val pointsForBrick = 100

        private var player: Player = Player()
        private var paddle: Paddle = Paddle(screenWidth, screenHeight)
        private var ball: Ball = Ball()
        private var bricks: ArrayList<Brick> = ArrayList()

        init {
            createBricksAndRestart()
        }

        private fun createBricksAndRestart() {
            ball.reset(paddle)

            for (column in 0..3) {
                for (row in 0..4) {
                    bricks.add(Brick(row, column, screenWidth, screenHeight))
                }
            }
            if (player.lives == 0) {
                player.reset()
            }
        }

        override fun run() {
            var framesToUpdate: Long = 0
            while (playing) {
                val beforeDrawing = System.currentTimeMillis()

                if (!paused) {
                    update(framesToUpdate)
                }

                draw()
                val afterDrawing = System.currentTimeMillis() - beforeDrawing
                if (afterDrawing >= 1) {
                    framesToUpdate = 1000 / afterDrawing
                }
            }
        }

        private fun update(framesToUpdate: Long) {
            paddle.update(framesToUpdate)
            ball.update(framesToUpdate)

            checkForBricksIntersection()
            bounceBallFromPaddle()
            checkForDeath()
            bounceFromTop()
            bounceFromSides()
            checkForGameEnd()
        }

        private fun checkForGameEnd() {
            if (player.score == bricks.size * pointsForBrick) {
                paused = true
                createBricksAndRestart()
            }
        }

        private fun bounceFromSides() {
            if (ball.rectangle.left < 0 || ball.rectangle.right > screenWidth) {
                ball.bounceHorizontal()
            }
        }

        private fun bounceFromTop() {
            if (ball.rectangle.top < 0) {
                ball.bounceVertical()

            }
        }

        private fun checkForDeath() {
            if (ball.rectangle.bottom > screenHeight) {
                ball.reset(paddle)
                player.kill()

                if (player.isDead()) {
                    paused = true
                    createBricksAndRestart()
                }
            }
        }

        private fun bounceBallFromPaddle() {
            if (RectF.intersects(paddle.rectangle, ball.rectangle)) {
                ball.bounceHorizontalAtRandom()
                ball.bounceVertical()
            }
        }

        private fun checkForBricksIntersection() {
            for (brick in bricks) {
                if (brick.visible && RectF.intersects(brick.rectangle, ball.rectangle)) {
                    brick.visible = false
                    ball.bounceVertical()
                    player.addPoints(pointsForBrick)
                }
            }
        }

        private fun draw() {
            if (holder.surface.isValid) {
                canvas = holder.lockCanvas()

                canvas.drawColor(BLACK)
                drawBallAndPaddle()
                drawBricks()
                drawPoints()

                holder.unlockCanvasAndPost(canvas)
            }
        }

        private fun drawBallAndPaddle() {
            val paint = getPaint(WHITE)
            canvas.drawRect(paddle.rectangle, paint)
            canvas.drawRect(ball.rectangle, paint)
        }

        private fun drawBricks() {
            val paint = getPaint(WHITE)

            for (i in 0 until bricks.size) {
                if (bricks[i].visible) {
                    canvas.drawRect(bricks[i].rectangle, paint)
                }
            }
        }

        private fun drawPoints() {
            val paint = getPaint(YELLOW)
            paint.textSize = 80f
            canvas.drawText("Score: ${player.score} Lives: ${player.lives}", 10f, 100f, paint)
        }

        private fun getPaint(paintColor: Int): Paint {
            return Paint().apply { color = paintColor }
        }

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
                        paddle.changeMoveDirection(RIGHT)
                    else paddle.changeMoveDirection(LEFT)
                }

                MotionEvent.ACTION_UP -> paddle.changeMoveDirection(STOPPED)
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