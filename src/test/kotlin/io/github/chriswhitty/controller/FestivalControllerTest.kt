package io.github.chriswhitty.service

import io.github.chriswhitty.controller.FestivalController
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model


@RunWith(SpringRunner::class)
@WebMvcTest(FestivalController::class)
class FestivalControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean lateinit var festivalScoreCalculator: FestivalScoreCalculator

    @Test
    fun postForm_shouldAddFestivalScoreToModel() {

        `when`(festivalScoreCalculator.calculate(
                Festival(listOf(
                        Artist("The National"),
                        Artist("The Smiths")
                        ))))
            .thenReturn(88)

        val request = post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "festival name")
                .param("artists", "The National\r\nThe Smiths")

        mockMvc.perform(request)
                .andExpect(model().attribute("score", 88))
    }

}

