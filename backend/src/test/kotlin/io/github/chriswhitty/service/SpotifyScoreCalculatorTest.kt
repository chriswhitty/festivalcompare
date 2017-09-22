package io.github.chriswhitty.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.github.chriswhitty.client.Artist
import io.github.chriswhitty.client.SpotifyClient
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test


class SpotifyScoreCalculatorTest {
    lateinit var mockSpotifyClient: SpotifyClient
    lateinit var calculator: SpotifyArtistScoreCalculator

    @Before
    fun setUp() {
        mockSpotifyClient = mock<SpotifyClient>()
        calculator = SpotifyArtistScoreCalculator(mockSpotifyClient)
    }

    @Test
    fun shouldRetrievePopularityFromSpotify() {
        whenever(mockSpotifyClient.searchArtist("The National")).thenReturn(Artist("The National", 90))
        assertThat(calculator.calculate("The National"), equalTo(90))
    }

    @Test
    fun shouldReturnNull_whenNoArtistFound() {
        whenever(mockSpotifyClient.searchArtist("FakeBand")).thenReturn(null)
        assertThat(calculator.calculate("FakeBand"), nullValue())
    }
}

