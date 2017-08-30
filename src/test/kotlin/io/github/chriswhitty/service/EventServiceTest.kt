package io.github.chriswhitty.service

import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Test


class EventServiceTest {

    @Test
    fun shouldCreateEventObjectWithScores() {
        val scoreCalculator = BandScores(mapOf(
                "The National" to 70,
                "Nirvana" to 50
        ))

        val calculator = EventServiceImpl(scoreCalculator)

        val event = calculator.calculate("", listOf("The National", "Nirvana"))

        assertThat(event.artists[0].name, equalTo("The National"))
        assertThat(event.artists[0].scores[0].score, equalTo(70))
        assertThat(event.artists[0].scores[0].type, equalTo("Spotify"))

        assertThat(event.artists[1].name, equalTo("Nirvana"))
        assertThat(event.artists[1].scores[0].score, equalTo(50))
        assertThat(event.artists[1].scores[0].type, equalTo("Spotify"))
    }

    @Test
    fun shouldAverageScoreForEvent() {
        val scoreCalculator = BandScores(mapOf(
                "The National" to 70,
                "Nirvana" to 50
        ))

        val calculator = EventServiceImpl(scoreCalculator)

        val event = calculator.calculate("", listOf("The National", "Nirvana"))
        assertThat(event.eventScore, equalTo(60.0))
    }

    @Test
    fun shouldNotAddScoreAndExcludeFromTotal_whenNotFound() {
        val scoreCalculator = BandScores(mapOf(
                "The National" to 70
        ))

        val calculator = EventServiceImpl(scoreCalculator)

        val event = calculator.calculate("", listOf("The National", "FakeBand"))

        assertThat(event.eventScore, equalTo(70.0))
        assertThat(event.artists[1].scores, hasSize(0))
    }

    @Test
    fun shouldReturn0_whenNoArtistsMatched() {
        val scoreCalculator = BandScores(mapOf())

        val calculator = EventServiceImpl(scoreCalculator)

        val event = calculator.calculate("", listOf("FakeBand"))

        assertThat(event.eventScore, equalTo(0.0))
    }

    class BandScores(val scores: Map<String, Int>) : ArtistScoreCalculator {
        override val type = "Spotify"

        override fun calculate(artistName: String): Int? {
            return scores[artistName]
        }
    }

}



