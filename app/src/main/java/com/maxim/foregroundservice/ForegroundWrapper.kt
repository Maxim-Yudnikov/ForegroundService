package com.maxim.foregroundservice

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

interface ForegroundWrapper {
    fun start()
    fun stop()

    class Base(applicationContext: Context): ForegroundWrapper {
        private val workManager = WorkManager.getInstance(applicationContext)
        override fun start() {
            val request = OneTimeWorkRequestBuilder<PomodoroWorker>().build()
            workManager.enqueueUniqueWork(NAME, ExistingWorkPolicy.REPLACE, request)
        }

        override fun stop() {
            workManager.cancelUniqueWork(NAME)
        }

        companion object {
            private const val NAME = "pomodoro"
        }
    }
}