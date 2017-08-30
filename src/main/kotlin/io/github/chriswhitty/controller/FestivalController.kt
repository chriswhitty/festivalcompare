package io.github.chriswhitty.controller

import io.github.chriswhitty.service.EventService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping


@Controller class FestivalController(val eventService: EventService) {

    @GetMapping("/") fun displayPostFestivalForm(model: Model): String {
        model.addAttribute("message", "boo")
        return "addFestival"
    }

    @PostMapping("/") fun handlePostFestivalForm(@ModelAttribute form: FestivalFormDto, model: Model): String {

        val artists = form.artists.split("\n")
                .filter { it.isNotBlank() }
                .map { it.trim() }

        val event = eventService.calculate(form.name, artists)

        model.addAttribute("score", event.eventScore.toInt())
        model.addAttribute("notFound", event.artists.filter { it.scores.isEmpty() })
        return "festivalResult"
    }

    data class FestivalFormDto(var name: String = "", var artists: String = "")
}

