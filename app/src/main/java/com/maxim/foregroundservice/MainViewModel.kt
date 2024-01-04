package com.maxim.foregroundservice

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class MainViewModel(
    private val communication: Communication,
    private val storage: Storage,
    private val foregroundWrapper: ForegroundWrapper,
    private val playSound: PlaySound,
    private val periodList: List<Period>
) {
    private var index = -1
    private var isStopped = true
    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(UiState.Stopped(storage.read()))
        }
    }

    fun changeTitle(title: String) {
        storage.save(title)
    }

    fun start() {
        index = 0
        isStopped = false
        foregroundWrapper.start()
    }

    fun next() {
        if (++index == periodList.size)
            index = 0
    }

    fun stop() {
        index = -1
        communication.update(UiState.Stopped(storage.read()))
        isStopped = true
        foregroundWrapper.stop()
    }

    fun observe(owner: LifecycleOwner, observer: Observer<UiState>) {
        communication.observe(owner, observer)
    }

    fun notStopped() = !isStopped

    fun currentPeriod() = periodList[index]

    fun title() = storage.read()

    fun update(time: String) {
        communication.update(UiState.Running(currentPeriod().title() + "\n" + time))
    }

    fun playSound() {
        playSound.playSound()
    }

    fun timeUi(time: Long): String {
        val minutes = time / 60_000
        val second = time / 1_000 % 60
        return "$minutes:${if (second < 10) "0$second" else second}"
    }
}