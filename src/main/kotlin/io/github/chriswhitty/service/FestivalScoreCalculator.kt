package io.github.chriswhitty.service

import org.springframework.stereotype.Service


interface FestivalScoreCalculator{
    fun calculate(festival: Festival): ScoreResult
}

data class ScoreResult(val score: Int, val notFound: List<Artist>)


@Service
class FestivalScoreCalculatorImpl(val bandScoreCalculator: ArtistScoreCalculator): FestivalScoreCalculator {

    override fun calculate(festival: Festival): ScoreResult {
        var total = 0
        var matched = 0
        val missing = mutableListOf<Artist>()

        festival.bands.forEach { artist ->
            val score = bandScoreCalculator.calculate(artist)
            if (score != null) {
                matched++
                total+= score
            } else {
                missing.add(artist)
            }
        }

        if(matched == 0) {
            return ScoreResult(0, missing)
        }

        return ScoreResult(total / matched, missing)
    }

}

data class Festival(val bands: List<Artist>)
data class Artist(val name: String)
