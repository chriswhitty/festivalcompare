package io.github.chriswhitty.service

import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test


class FestivalScoreCalculatorTest {

    @Test
    fun shouldAverageScoreForEachBand() {
        val scoreCalculator = BandScores(mapOf(
                "The National" to 70,
                "Nirvana" to 50
        ))

        val festival = Festival(listOf(
            Artist("The National"),
            Artist("Nirvana")
        ))

        val calculator = FestivalScoreCalculatorImpl(scoreCalculator)

        val score = calculator.calculate(festival)
        assertThat(score, equalTo(60))
    }

    @Test
    fun shouldExcludeArtists_whenTheyWereNotFound() {
        val scoreCalculator = BandScores(mapOf(
                "The National" to 70
        ))

        val festival = Festival(listOf(
                Artist("The National"),
                Artist("FakeBand")
        ))

        val calculator = FestivalScoreCalculatorImpl(scoreCalculator)

        val score = calculator.calculate(festival)
        assertThat(score, equalTo(70))
    }

    @Test
    fun shouldReturn0_whenNoArtistsMatched() {
        val scoreCalculator = BandScores(mapOf())

        val festival = Festival(listOf(
                Artist("FakeBand")
        ))

        val calculator = FestivalScoreCalculatorImpl(scoreCalculator)

        val score = calculator.calculate(festival)
        assertThat(score, equalTo(0))
    }

    class BandScores(val scores: Map<String, Int>): ArtistScoreCalculator {
        override fun calculate(band: Artist): Int? {
            return scores[band.name]
        }
    }

}



