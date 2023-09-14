package com.example.ax_homework_an.ui.model

data class Lottery(val y: Int, val x: Int) {
    companion object {
        const val UNDEFINED = 0
    }
    val row: Int
        get() = y -1
    val column: Int
        get() = x -1
}
