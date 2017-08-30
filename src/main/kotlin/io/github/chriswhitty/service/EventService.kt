package io.github.chriswhitty.service

import org.springframework.stereotype.Service


interface EventService {
    fun calculate(name: String, artistNames: List<String>): Event
}

data class ScoreResult(val score: Int, val notFound: List<Artist>)


data class Score(
        val type: String,
        val score: Int
)

data class Artist(
        val name: String,
        val scores: List<Score>
)

data class Event(
        val eventName: String,
        val eventScore: Double,
        val artists: List<Artist>
)

@Service
class EventServiceImpl(val artistScoreCalculator: ArtistScoreCalculator) : EventService {

    override fun calculate(name: String, artistNames: List<String>): Event {

        val artists = artistNames.map { name ->
            val scores = mutableListOf<Score>()
            artistScoreCalculator.calculate(name)?.let {
                scores.add(Score(artistScoreCalculator.type, it))
            }

            Artist(name = name, scores = scores)
        }

        val spotifyScores = artists.map {
            it.scores.find { it.type == "Spotify" }
        }.filterNotNull()

        val average = if (spotifyScores.isEmpty()) {
            0.0
        } else {
            spotifyScores
                    .map { it.score }
                    .average()
        }

        return Event(name, average, artists)
    }

}

