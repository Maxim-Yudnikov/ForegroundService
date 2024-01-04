package com.maxim.foregroundservice

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communication {
    fun update(value: UiState)
    fun observe(owner: LifecycleOwner, observer: Observer<UiState>)

    class Base: Communication {
        private val liveData = MutableLiveData<UiState>()
        override fun update(value: UiState) {
            liveData.postValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<UiState>) {
            liveData.observe(owner, observer)
        }
    }
}