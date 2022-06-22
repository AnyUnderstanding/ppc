@file:OptIn(DelicateCoroutinesApi::class)

package control.remoteclient

import kotlinx.coroutines.*
import java.util.concurrent.LinkedBlockingQueue

object ConcurrentExecutionController {
    private val jobs = LinkedBlockingQueue<suspend CoroutineScope.() -> Unit>()


    init {
        start()
    }

    fun cancel() {
        GlobalScope.cancel()
    }

    fun start() {
        GlobalScope.launch {
            while (isActive) {
                if (jobs.size != 0) {
                    val job = withContext(Dispatchers.IO) {
                        jobs.take()
                    }
                    launch {
                        job()
                    }
                }
            }
        }
    }

    fun scheduleJob(job: suspend CoroutineScope.() -> Unit) {
        jobs.add(job)
    }

    // can be used to block at the end of the program to ensure all jobs finish before the program is closed
    fun isEmpty(): Boolean {
        return jobs.size == 0
    }
}