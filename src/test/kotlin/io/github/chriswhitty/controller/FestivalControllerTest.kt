package io.github.chriswhitty.service

import com.nhaarman.mockito_kotlin.any
import io.github.chriswhitty.client.SpotifyClient
import io.github.chriswhitty.controller.FestivalController
import io.github.chriswhitty.service.SpotifyArtistScoreCalculator.Companion.SPOTIFY_TYPE_ID
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@RunWith(SpringRunner::class)
@WebMvcTest(FestivalController::class)
class FestivalControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean lateinit var eventService: EventService
    @MockBean lateinit var spotifyClient: SpotifyClient

    @Test
    fun postForm_shouldAddFestivalScoreToModel() {

        val missingBand = "Missing Band"

        `when`(eventService.calculate("festival name",
                listOf("The National", "The Smiths", "Missing Band")))
                .thenReturn(Event("festival name", 88.0, listOf(
                        Artist("The National", listOf(
                                Score(SPOTIFY_TYPE_ID, 90)
                        )),
                        Artist("The Smiths", listOf(
                                Score(SPOTIFY_TYPE_ID, 80)
                        )),
                        Artist("Missing Band", listOf())
                )))

        val request = post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "festival name")
                .param("artists", "The National\r\nThe Smiths\r\n${missingBand}")

        mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(model().attribute("score", 88))
                .andExpect(model().attribute("notFound", listOf(Artist(name = missingBand, scores = listOf()))))
    }

    @Test
    fun postForm_shouldExcludeEmptyLines() {

        val expectedEvent = Event("festival name", 85.0, listOf(
                Artist("The National", listOf(
                        Score(SPOTIFY_TYPE_ID, 90)
                )),
                Artist("The Smiths", listOf(
                        Score(SPOTIFY_TYPE_ID, 80)
                ))))

        `when`(eventService.calculate(any(), any()))
                .thenReturn(expectedEvent)

        val request = post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "festival name")
                .param("artists", "The National\r\n  \r\nThe Smiths\r\n")

        mockMvc.perform(request)
                .andExpect(status().isOk)

        verify(eventService).calculate("festival name", listOf("The National", "The Smiths"))
    }

}

