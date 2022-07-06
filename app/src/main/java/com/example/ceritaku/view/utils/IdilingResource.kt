package com.example.ceritaku.view.utils

import androidx.test.espresso.idling.CountingIdlingResource

object IdlingConfig {
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
    IdlingConfig.increment()
    return try {
        function()
    } finally {
        IdlingConfig.decrement()
    }
}