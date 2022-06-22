package control.remoteclient

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.LinkedBlockingQueue

object ConcurrentExecutionController {
    private val jobs = LinkedBlockingQueue<suspend CoroutineScope.() -> Unit>()

    init {
        GlobalScope.launch {
            while (true) {
                if (jobs.size != 0) {
                    val job = jobs.take()
                    launch {
                        println("starting scheduled job")
                        job()
                    }
                }
            }
        }
    }


    fun scheduleJob(job: suspend CoroutineScope.() -> Unit){
        jobs.add(job)
    }

    // can be used to block at the end of the program to ensure all jobs finish before the program is closed
    fun isEmpty(): Boolean{
        return jobs.size == 0
    }
}