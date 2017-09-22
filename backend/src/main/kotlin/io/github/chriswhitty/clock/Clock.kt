package io.github.chriswhitty.clock

import org.springframework.stereotype.Component
import java.time.Instant

interface Clock {
    fun now(): Instant
}

@Component
class RealClock() : Clock {
    override fun now() = Instant.now()
}