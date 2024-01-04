package com.maxim.foregroundservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class PomodoroWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val viewModel = (context.applicationContext as ProvideViewModel).viewModel()
    override suspend fun doWork(): Result {
        setForeground(
            createForegroundInfo(
                viewModel.currentPeriod().title() + "" + viewModel.title(),
                "Pomodoro start"
            )
        )
        while (viewModel.notStopped()) {
            var time = viewModel.currentPeriod().timeInMillis()
            while (time > 0) {
                time -= 1000
                delay(1000)
                val timeUi = viewModel.timeUi(time)
                notificationManager.notify(
                    NOTIFICATION_ID,
                    makeNotification(viewModel.currentPeriod().title() + " " + viewModel.title(), timeUi)
                )
                viewModel.update(timeUi)
            }
            viewModel.playSound()
            viewModel.next()
        }
        return Result.success()
    }

    private fun createForegroundInfo(title: String, text: String): ForegroundInfo {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            createChannel()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            ForegroundInfo(
                NOTIFICATION_ID,
                makeNotification(title, text),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        else
            ForegroundInfo(NOTIFICATION_ID, makeNotification(title, text))
    }

    private fun makeNotification(title: String, text: String): Notification {
        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val resultPendingIntent = TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(resultIntent)
            .getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setContentIntent(resultPendingIntent)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(CHANNEL_ID, "Pomodoro", NotificationManager.IMPORTANCE_LOW)
        channel.description = "Pomodoro time"
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_ID = 123456789
        private const val CHANNEL_ID = "Pomodoro"
    }
}