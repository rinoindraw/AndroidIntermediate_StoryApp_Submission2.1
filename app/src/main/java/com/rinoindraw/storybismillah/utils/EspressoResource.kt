package com.rinoindraw.storybismillah.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    EspressoResource.increment()
    return try {
        function()
    } finally {
        EspressoResource.decrement()
    }
}