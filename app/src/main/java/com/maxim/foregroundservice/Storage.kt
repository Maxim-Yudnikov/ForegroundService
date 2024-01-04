package com.maxim.foregroundservice

import android.content.SharedPreferences

interface Storage {
    fun save(value: String)
    fun read(): String

    class Base(private val pref: SharedPreferences): Storage {
        override fun save(value: String) {
            pref.edit().putString(KEY, value).apply()
        }

        override fun read() = pref.getString(KEY, "") ?: ""

        companion object {
            private const val KEY = "key"
        }
    }
}