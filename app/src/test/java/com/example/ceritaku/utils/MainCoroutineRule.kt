package com.example.ceritaku.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val dispatche : TestCoroutineDispatcher = TestCoroutineDispatcher()
): TestWatcher(),TestCoroutineScope by TestCoroutineScope(dispatche) {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatche)
    }

    override fun finished(description: Description?) {
        super.finished(description)

        Dispatchers.resetMain()
    }

}