package io.github.chriswhitty.ratelimiter

import io.github.chriswhitty.clock.Clock
import java.time.Duration
import java.time.Instant

interface RateLimiter {
    fun <R : Any?, T : () -> R> start(callable: T): R
}

class BlockingRateLimiter(
        private val clock: Clock,
        private val requestLimit: Duration = Duration.ofMillis(100)) : RateLimiter {

    private var lastCall = Instant.MIN

    override fun <R : Any?, T : () -> R> start(callable: T): R {
        val canCallAfter = lastCall + requestLimit
        while (clock.now().isBefore(canCallAfter)) {
            Thread.sleep(50)
        }
        lastCall = clock.now()

        return callable()
    }

}