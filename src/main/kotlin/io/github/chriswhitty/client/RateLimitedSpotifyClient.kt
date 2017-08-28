package io.github.chriswhitty.client

import io.github.chriswhitty.ratelimiter.RateLimiter


class RateLimitedSpotifyClient(
        private val spotifyClient: SpotifyClient,
        private val rateLimiter: RateLimiter): SpotifyClient {

    override fun searchArtist(name: String): Artist? {
        val artist = rateLimiter.start { spotifyClient.searchArtist(name) }
        return artist
    }

}