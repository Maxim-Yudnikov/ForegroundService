package com.maxim.foregroundservice

import android.content.Context
import android.media.MediaPlayer

interface PlaySound {
    fun playSound()

    class Base(context: Context): PlaySound {
        private val player = MediaPlayer.create(context, R.raw.sound)
        override fun playSound() {
            player.start()
        }
    }
}