package io.github.chriswhitty.ratelimiter

import io.github.chriswhitty.clock.Clock
import io.github.chriswhitty.ratelimiter.BlockingRateLimiter
import io.github.chriswhitty.util.TimeoutAssertionError
import io.github.chriswhitty.util.Waits.Companion.wait
import org.junit.Assert.fail
import org.junit.Test
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit


class TestClock(var now: Instant = Instant.now()) : Clock {
    override fun now(): Instant {
        return now;
    }

    fun tick(amount: Long, unit: TemporalUnit) {
        this.now = this.now.plus(amount, unit)
    }
}


class BlockingRateLimiterTest {

    @Test
    fun `should limit requests to the configured ammount`() {

        val testClock = TestClock()
        val limiter = BlockingRateLimiter(testClock, Duration.ofSeconds(1))

        val clientWaiter = ClientWaiter(limiter)
        Thread(clientWaiter).start()

        wait { clientWaiter.finished }

        val clientWaiter2 = ClientWaiter(limiter)
        Thread(clientWaiter2).start()

        try {
            wait { clientWaiter2.finished }
            fail("Should have timed out")
        } catch(ex: TimeoutAssertionError) {
        }

        testClock.tick(1, ChronoUnit.SECONDS)
        wait { clientWaiter2.finished }
    }


    class ClientWaiter(val limiter: BlockingRateLimiter) : Runnable {

        var finished: Boolean = false

        override fun run() {
            val result = limiter.start {
                true
            }
            finished = result //TODO inlining this causes error
        }
    }

}

