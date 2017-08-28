package io.github.chriswhitty.util

import java.time.Duration
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit


class TimeoutAssertionError(msg: String): AssertionError(msg)

class Waits {

    companion object{

        fun wait(amount: Long = 5,
                         unit: TemporalUnit = ChronoUnit.SECONDS,
                         interval: Duration = Duration.ofMillis(100),
                         test: () -> Boolean) {

            var durationLeft = unit.duration.multipliedBy(amount)

            while(!test()) {
                Thread.sleep(interval.toMillis())
                durationLeft = durationLeft.minus(interval)

                if(durationLeft.isNegative || durationLeft.isZero) {
                    throw TimeoutAssertionError("Expectation timed out")
                }
            }
        }
    }

}