package com.example.ceritaku.view.utils

import android.icu.number.Precision.increment
import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResources {
    private const val resource = "global"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(resource)

    fun increment(){
        countingIdlingResource.increment()
    }

    fun decrement(){
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapperIdling(function: () -> T): T {
    EspressoIdlingResources.increment()
    return try {
        function()
    } finally {
        EspressoIdlingResources.decrement()
    }
}