package com.maxim.foregroundservice

import android.app.Application

class App : Application(), ProvideViewModel {
    private lateinit var viewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        val periodList = listOf(
            Period.Pomodoro, Period.Break
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