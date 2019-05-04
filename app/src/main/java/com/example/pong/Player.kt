package com.example.pong

data class Player(var score: Int = 0, var lives: Int = 3) {
    fun addPoints(points: Int) {
        score += points
    }

    fun kill() {
        lives--
    }

    fun isDead(): Boolean {
        return lives <= 0
    }

    fun reset() {
        lives = 3
        score = 0
    }
}