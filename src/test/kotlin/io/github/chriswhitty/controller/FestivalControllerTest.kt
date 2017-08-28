package io.github.chriswhitty.service

import com.nhaarman.mockito_kotlin.any
import io.github.chriswhitty.client.SpotifyClient
import io.github.chriswhitty.controller.FestivalController
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

    @MockBean lateinit var festivalScoreCalculator: FestivalScoreCalculator
    @MockBean lateinit var spotifyClient: SpotifyClient

    @Test
    fun postForm_shouldAddFestivalScoreToModel() {

        val missingBand = Artist("Missing Band")
        `when`(festivalScoreCalculator.calculate(
                Festival(listOf(
                        Artist("The National"),
                        Artist("The Smiths"),
                        missingBand
                        ))))
            .thenReturn(ScoreResult(88, listOf(missingBand)))

        val request = post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "festival name")
                .param("artists", "The National\r\nThe Smiths\r\n${missingBand.name}")

        mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(model().attribute("score", 88))
                .andExpect(model().attribute("notFound", listOf(missingBand)))
    }

    @Test
    fun postForm_shouldExcludeEmptyLines() {

        `when`(festivalScoreCalculator.calculate(any()))
                .thenReturn(ScoreResult(88, listOf()))

        val request = post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "festival name")
                .param("artists", "The National\r\n  \r\nThe Smiths\r\n")

        mockMvc.perform(request)
                .andExpect(status().isOk)

        verify(festivalScoreCalculator).calculate(Festival(
                listOf(Artist("The National"), Artist("The Smiths"))))
    }

}

