package io.github.chriswhitty.service

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.http.ResponseDefinition
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder.newRequestPattern
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import io.github.chriswhitty.client.SpotifyClientImpl
import org.apache.commons.io.IOUtils
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import java.nio.charset.Charset


class SpotifyClientTest {

    @Rule @JvmField var wireMockRule = WireMockRule(8089)
    val searchHost = "http://localhost:8089"


    @Test
    fun shouldRetrieveArtistFromSpotify_ignoringCase() {
        wireMockRule.addStubMapping(StubMapping(
                newRequestPattern(RequestMethod.GET, WireMock.urlPathEqualTo("/v1/search"))
                        .withQueryParam("q", WireMock.equalTo("The National"))
                        .withQueryParam("type", WireMock.equalTo("artist"))
                    .build(),
                ResponseDefinition(200, file("/spotify_artist_search_response.json"))))

        val spotify = SpotifyClientImpl(searchHost)

        val (name, popularity) = spotify.searchArtist("The National")!!
        assertThat(name, equalTo("The national"))
        assertThat(popularity, equalTo(69))
    }

    @Test
    fun shouldReturnNull_whenApiRequestFails() {
        wireMockRule.addStubMapping(StubMapping(
                newRequestPattern(RequestMethod.GET, WireMock.urlPathEqualTo("/v1/search"))
                        .withQueryParam("q", WireMock.equalTo("Too Many Requests"))
                        .withQueryParam("type", WireMock.equalTo("artist"))
                        .build(),
                ResponseDefinition(492, file("/spotify_artist_search_rate_limit.json"))))

        val spotify = SpotifyClientImpl(searchHost)

        val artist = spotify.searchArtist("Too Many Requests")
        assertThat(artist, nullValue())
    }

    @Test
    fun shouldReturnNull_whenNoArtistFound() {
        wireMockRule.addStubMapping(StubMapping(
                newRequestPattern(RequestMethod.GET, WireMock.urlPathEqualTo("/v1/search"))
                        .withQueryParam("q", WireMock.equalTo("FakeBand"))
                        .withQueryParam("type", WireMock.equalTo("artist"))
                        .build(),
                ResponseDefinition(200, file("/spotify_artist_search_not_found.json"))))

        val spotify = SpotifyClientImpl(searchHost)

        val artist = spotify.searchArtist("FakeBand")
        assertThat(artist, nullValue())
    }

    fun file(fileName: String) : String {
        return IOUtils.toString(javaClass.getResourceAsStream(fileName), Charset.defaultCharset())!!
    }
}

