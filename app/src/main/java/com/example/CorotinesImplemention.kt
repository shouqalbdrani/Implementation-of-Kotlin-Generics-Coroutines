package com.example

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

// Function to fetching data
suspend fun getData(): String = withContext(Dispatchers.IO) {
    delay(2000)
    "Get Data successfully"
}

// Function to waiting for data
suspend fun waitForData(): String = withContext(Dispatchers.IO) {
    delay(1000)
    "Waiting for data completed"
}

// Function to filtering data
suspend fun filterData(): String = withContext(Dispatchers.IO) {
    delay(1500)
    "Data filtered successfully"
}

// Sequential Execution
suspend fun sequentialExecution() {
    val time = measureTimeMillis {
        println(getData())
        println(waitForData())
        println(filterData())
    }
    println("Sequential Execution completed in $time ms\n")
}

// Concurrent Execution
suspend fun concurrentExecution() = coroutineScope {
    val time = measureTimeMillis {
        val data = async { getData() }
        val wait = async { waitForData() }
        val filter = async { filterData() }

        println(data.await())
        println(wait.await())
        println(filter.await())
    }
    println("Concurrent Execution completed in $time ms\n")
}

fun main(): Unit = runBlocking {
    launch(Dispatchers.Unconfined) { // runs on the main thread
        println(" Unconfined      : I'm working in thread ${Thread.currentThread().name}") // for name thread that working on
        delay(1500)
        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
    }

    launch { // main runBlocking coroutine
        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        delay(1000)
        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
    }

    println("Sequential Execution.")
    safeCall { sequentialExecution() }

    println("Concurrent Execution.")
    safeCall { concurrentExecution() }
}

// Error Handling
suspend fun <T> safeCall(action: suspend () -> T): T? {
    return try {
        action()
    } catch (e: Exception) {
        println("Error occurred: ${e.message}")
        null
    }
}
