package comtest.ct.cd.bima.githubusers.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun delayedTest(maxRetry: Int = 15, delay: Long = 300, testBlock: () -> Unit) = runBlocking {
    for (i in 1..maxRetry) {
        runCatching(testBlock).getOrElse {
            if (i == maxRetry) throw it else delay(delay)
        }
    }
}