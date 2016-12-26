package io.github.chriswhitty.controller

import io.github.chriswhitty.service.Artist
import io.github.chriswhitty.service.Festival
import io.github.chriswhitty.service.FestivalScoreCalculator
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping


@Controller class FestivalController(val festivalScoreCalculator: FestivalScoreCalculator) {

    @GetMapping("/") fun displayPostFestivalForm(model: Model): String {
        model.addAttribute("message", "boo")
        return "addFestival"
    }

    @PostMapping("/") fun handlePostFestivalForm(@ModelAttribute form: FestivalFormDto, model: Model): String {

        val artists = form.artists.split("\n")
                .filter { it.isNotBlank() }
                .map { Artist(it.trim()) }

        val scoreResult = festivalScoreCalculator.calculate(Festival(artists))

        model.addAttribute("score", scoreResult.score)
        model.addAttribute("notFound", scoreResult.notFound)
        return "festivalResult"
    }

    data class FestivalFormDto(var name: String = "", var artists: String = "")
}

