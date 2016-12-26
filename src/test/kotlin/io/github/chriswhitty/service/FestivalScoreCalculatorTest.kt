package io.github.chriswhitty.service

import org.hamcrest.Matchers
import org.hamcrest.Matchers.contains
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

        val scoreResult = calculator.calculate(festival)
        assertThat(scoreResult.score, equalTo(60))
    }

    @Test
    fun shouldExcludeAndReportArtists_whenTheyWereNotFound() {
        val scoreCalculator = BandScores(mapOf(
                "The National" to 70
        ))

        val missingBand = Artist("FakeBand")
        val festival = Festival(listOf(
                Artist("The National"),
                missingBand
        ))

        val calculator = FestivalScoreCalculatorImpl(scoreCalculator)

        val (score, missing) = calculator.calculate(festival)
        assertThat(score, equalTo(70))
        assertThat(missing, contains(missingBand))
    }

    @Test
    fun shouldReturn0_whenNoArtistsMatched() {
        val scoreCalculator = BandScores(mapOf())

        val missingBand = Artist("FakeBand")
        val festival = Festival(listOf(
                missingBand
        ))

        val calculator = FestivalScoreCalculatorImpl(scoreCalculator)

        val (score, missing) = calculator.calculate(festival)
        assertThat(score, equalTo(0))
        assertThat(missing, contains(missingBand))
    }

    class BandScores(val scores: Map<String, Int>): ArtistScoreCalculator {
        override fun calculate(band: Artist): Int? {
            return scores[band.name]
        }
    }

}



