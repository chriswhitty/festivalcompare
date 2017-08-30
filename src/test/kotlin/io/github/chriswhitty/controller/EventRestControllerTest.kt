package io.github.chriswhitty.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.chriswhitty.client.SpotifyClient
import io.github.chriswhitty.controller.EventRestController
import io.github.chriswhitty.controller.NewEvent
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@RunWith(SpringRunner::class)
@WebMvcTest(EventRestController::class)
class EventRestControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean lateinit var eventService: EventService
    @MockBean lateinit var spotifyClient: SpotifyClient

    @Test
    fun post_shouldCalculateScore() {

        val newEvent = NewEvent(
                eventName = "festival name",
                artistNames = listOf("The National", "The Smiths", "Missing Band")
        )

        `when`(eventService.calculate("festival name",
                listOf("The National", "The Smiths", "Missing Band")))
                .thenReturn(Event("festival name", 85.0, listOf(
                        Artist("The National", listOf(
                                Score("Spotify", 90)
                        )),
                        Artist("The Smiths", listOf(
                                Score("Spotify", 80)
                        )),
                        Artist("Missing Band", listOf())
                )))

        val request = post("/api/new-event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(newEvent))

        mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.eventName", equalTo("festival name")))
                .andExpect(jsonPath("$.eventScore", equalTo(85.0)))
                .andExpect(jsonPath("$.artists[0].name", equalTo("The National")))
                .andExpect(jsonPath("$.artists[0].scores[0].type", equalTo("Spotify")))
                .andExpect(jsonPath("$.artists[0].scores[0].score", equalTo(90)))
                .andExpect(jsonPath("$.artists[1].name", equalTo("The Smiths")))
                .andExpect(jsonPath("$.artists[1].scores[0].type", equalTo("Spotify")))
                .andExpect(jsonPath("$.artists[1].scores[0].score", equalTo(80)))
                .andExpect(jsonPath("$.artists[2].name", equalTo("Missing Band")))
    }

    fun toJson(obj: Any): ByteArray {
        return ObjectMapper()
                .registerKotlinModule()
                .writeValueAsBytes(obj)
    }


}

