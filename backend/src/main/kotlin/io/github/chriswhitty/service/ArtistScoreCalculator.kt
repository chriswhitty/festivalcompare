package io.github.chriswhitty.service

import io.github.chriswhitty.client.SpotifyClient
import org.springframework.stereotype.Service


interface ArtistScoreCalculator {
    val type: String
    fun calculate(name: String): Int?
}

@Service
class SpotifyArtistScoreCalculator(val spotifyClient: SpotifyClient) : ArtistScoreCalculator {

    override val type = SPOTIFY_TYPE_ID

    override fun calculate(name: String): Int? {
        val artist = spotifyClient.searchArtist(name)

        artist ?: return null
        return artist.popularity
    }

    companion object {
        val SPOTIFY_TYPE_ID = "Spotify"
    }

}

