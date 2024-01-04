package com.maxim.foregroundservice

interface Period {
    fun timeInMillis(): Long
    fun title(): String

    abstract class Abstract(private val timeInMinutes: Int, private val title: String): Period {
        override fun timeInMillis() = timeInMinutes * 60_000L
        override fun title() = title
    }

    object Pomodoro: Abstract(2, "Work time!")
    object Break: Abstract(1, "Break time!")
}