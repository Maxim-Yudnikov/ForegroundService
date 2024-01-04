package com.maxim.foregroundservice

interface Period {
    fun timeInMillis(): Long
    fun title(): String

    abstract class Abstract(private val timeInMinutes: Int, private val title: String): Period {
        override fun timeInMillis() = timeInMinutes * 60_000L
        override fun title() = title
    }

    data class Pomodoro(private val timeInMinutes: Int): Abstract(timeInMinutes, "Work time!")
    data class Break(private val timeInMinutes: Int): Abstract(timeInMinutes, "Break time!")
}