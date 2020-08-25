package io.github.sainak.trackmysleepquality.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * This ViewModel can be shared across activities and fragments
 */
class SharedViewModel : ViewModel() {

    // live data to hold the value of trackingStatus
    val trackingStatus = MutableLiveData<Boolean>()
}