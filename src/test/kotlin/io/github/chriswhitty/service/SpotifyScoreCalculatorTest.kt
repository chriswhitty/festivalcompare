package io.github.chriswhitty.service

import io.github.chriswhitty.client.Artist
import io.github.chriswhitty.client.SpotifyClient
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`


class SpotifyScoreCalculatorTest {
    lateinit var mockSpotifyClient: SpotifyClient
    lateinit var calculator: SpotifyArtistScoreCalculator

    @Before
    fun setUp() {
        mockSpotifyClient = Mockito.mock(SpotifyClient::class.java)
        calculator = SpotifyArtistScoreCalculator(mockSpotifyClient)
    }

    @Test
    fun shouldRetrievePopularityFromSpotify() {
        `when`(mockSpotifyClient.searchArtist("The National")).thenReturn(Artist("The National", 90))
        assertThat(calculator.calculate(Artist("The National")), equalTo(90))
    }

    @Test
    fun shouldReturnNull_whenNoArtistFound() {
        `when`(mockSpotifyClient.searchArtist("FakeBand")).thenReturn(null)
        assertThat(calculator.calculate(Artist("FakeBand")), nullValue())
    }
}

