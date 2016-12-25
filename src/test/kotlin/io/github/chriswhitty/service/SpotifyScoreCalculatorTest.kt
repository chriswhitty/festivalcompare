package io.github.chriswhitty.service

import io.github.chriswhitty.client.Artist
import io.github.chriswhitty.client.SpotifyClient
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`


class SpotifyScoreCalculatorTest {

    @Test
    fun shouldRetrievePopularityFromSpotify() {

        val mockSpotifyClient = Mockito.mock(SpotifyClient::class.java)
        `when`(mockSpotifyClient.searchArtist("The National")).thenReturn(Artist("The National", 90))

        val calculator = SpotifyArtistScoreCalculator(mockSpotifyClient)
        assertThat(calculator.calculate(Artist("The National")), equalTo(90))
    }

    @Test
    fun shouldReturnNull_whenNoArtistFound() {

        val mockSpotifyClient = Mockito.mock(SpotifyClient::class.java)
        `when`(mockSpotifyClient.searchArtist("FakeBand")).thenReturn(null)

        val calculator = SpotifyArtistScoreCalculator(mockSpotifyClient)
        assertThat(calculator.calculate(Artist("FakeBand")), nullValue())
    }
}

