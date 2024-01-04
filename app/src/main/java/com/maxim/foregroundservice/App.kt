package com.maxim.foregroundservice

import android.app.Application

class App : Application(), ProvideViewModel {
    private lateinit var viewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        val isDebug = BuildConfig.DEBUG
        val periodList = listOf(
            Period.Pomodoro(if (isDebug) 2 else 25), Period.Break(if (isDebug) 1 else 5)
        )
        viewModel = MainViewModel(
            Communication.Base(),
            Storage.Base(getSharedPreferences(STORAGE_NAME, MODE_PRIVATE)),
            ForegroundWrapper.Base(this),
            PlaySound.Base(this),
            periodList
        )
    }

    override fun viewModel() = viewModel

    companion object {
        private const val STORAGE_NAME = "FOREGROUND SERVICE STORAGE"
    }
}

interface ProvideViewModel {
    fun viewModel(): MainViewModel
}