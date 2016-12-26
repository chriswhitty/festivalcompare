package io.github.chriswhitty.client

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component

data class Artist(val name : String, val popularity: Int)

interface SpotifyClient {
    fun searchArtist(name: String): Artist?
}

@Component
class SpotifyClientImpl(val spotifyHost: String) : SpotifyClient {

    val okHttpClient = OkHttpClient()

    override fun searchArtist(name: String): Artist? {

        val request = Request.Builder()
                .url("$spotifyHost/v1/search?q=$name&type=artist")
                .build()

        val response = okHttpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            print(response.body().string())
            return null
        }

        val objectMapper = ObjectMapper()
                .registerKotlinModule()
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)

        val wrapper = objectMapper.readValue(response.body().byteStream(), WrapperDto::class.java)

        val found = wrapper.artists.items.firstOrNull { artist ->
            artist.name.toLowerCase().equals(name.toLowerCase())
        }

        found ?: return null
        return Artist(found.name, found.popularity)
    }

    private data class WrapperDto(val artists: ArtistsDto)
    private data class ArtistsDto(val items: List<ArtistDto>)
    private data class ArtistDto(val name : String, val popularity: Int)
}
