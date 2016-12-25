package io.github.chriswhitty.service

import org.springframework.stereotype.Service


interface FestivalScoreCalculator{
    fun calculate(festival: Festival): Int
}

@Service
class FestivalScoreCalculatorImpl(val bandScoreCalculator: ArtistScoreCalculator): FestivalScoreCalculator {

    override fun calculate(festival: Festival): Int {
        var total = 0
        var matched = 0

        festival.bands.forEach { band ->
            val score = bandScoreCalculator.calculate(band)
            if (score != null) {
                matched++
                total+= score
            }
        }

        if(matched == 0) {
            return 0
        }

        return total / matched
    }

}

data class Festival(val bands: List<Artist>)
data class Artist(val name: String)
