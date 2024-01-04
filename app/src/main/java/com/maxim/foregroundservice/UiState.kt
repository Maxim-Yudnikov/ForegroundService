package com.maxim.foregroundservice

import android.widget.Button
import android.widget.EditText
import android.widget.TextView

interface UiState {
    fun show(title: EditText, textView: TextView, start: Button, stop: Button)

    data class Stopped(private val title: String) : UiState {
        override fun show(title: EditText, textView: TextView, start: Button, stop: Button) {
            title.setText(this.title)
            textView.text = ""
            start.isEnabled = true
            stop.isEnabled = false
        }
    }

    data class Running(private val title: String) : UiState {
        override fun show(title: EditText, textView: TextView, start: Button, stop: Button) {
            textView.text = this.title
            start.isEnabled = false
            stop.isEnabled = true
        }
    }
}