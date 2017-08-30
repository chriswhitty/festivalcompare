package io.github.chriswhitty.controller

import io.github.chriswhitty.service.Event
import io.github.chriswhitty.service.EventService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


data class NewEvent(
        val eventName: String,
        val artistNames: List<String>
)


@RestController
class EventRestController(val eventScoreCalculator: EventService) {


    @PostMapping("/api/new-event")
    fun postEvent(@RequestBody event: NewEvent): Event {

        return eventScoreCalculator.calculate(event.eventName, event.artistNames)
    }

}
