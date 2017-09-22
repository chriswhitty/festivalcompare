package io.github.chriswhitty.client

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component

data class Artist(val name : String, val popularity: Int)

interface SpotifyClient {
    fun searchArtist(name: String): Artist?
}

class RatelimitExceededException : Exception()

class SpotifyClientImpl(
        val apiEndpoint: String,
        oauthInterceptor: Interceptor) : SpotifyClient {


    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(oauthInterceptor)
        .build()

    override fun searchArtist(name: String): Artist? {

        val request = Request.Builder()
                .url("$apiEndpoint/v1/search?q=$name&type=artist")
                .build()

        val response = okHttpClient.newCall(request).execute()
        if(response.code() == 492) {
            throw RatelimitExceededException()
        }

        if (!response.isSuccessful) {
            print(response.body().string())
            return null
        }

        val objectMapper = ObjectMapper()
                .registerKotlinModule()
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)

        response.body().byteStream().use { responseStream ->
            val wrapper = objectMapper.readValue(responseStream, WrapperDto::class.java)

            val found = wrapper.artists.items.firstOrNull { artist ->
                artist.name.toLowerCase() == name.toLowerCase()
            }

            found ?: return null
            return Artist(found.name, found.popularity)
        }
    }

    private data class WrapperDto(val artists: ArtistsDto)
    private data class ArtistsDto(val items: List<ArtistDto>)
    private data class ArtistDto(val name : String, val popularity: Int)
}
