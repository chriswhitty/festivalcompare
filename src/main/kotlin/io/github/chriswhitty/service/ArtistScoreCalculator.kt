package io.github.chriswhitty.service

import io.github.chriswhitty.client.SpotifyClient
import org.springframework.stereotype.Service


interface ArtistScoreCalculator {
    fun calculate(band: Artist): Int?
}

@Service
class SpotifyArtistScoreCalculator(val spotifyClient: SpotifyClient) : ArtistScoreCalculator {

    override fun calculate(band: Artist): Int? {
        val artist = spotifyClient.searchArtist(band.name)

        artist ?: return null
        return artist.popularity
    }

}

