package com.example.rickandmortyapp

import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

open class AppExecutor(
    private val networkIO: Executor
) {

    @Inject
    constructor() : this(
        Executors.newFixedThreadPool(3)
    )

    fun networkIO(): Executor {
        return networkIO
    }
}