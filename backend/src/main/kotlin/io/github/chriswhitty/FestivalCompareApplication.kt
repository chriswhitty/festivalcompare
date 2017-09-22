package io.github.chriswhitty

import io.github.chriswhitty.client.*
import io.github.chriswhitty.clock.Clock
import io.github.chriswhitty.ratelimiter.BlockingRateLimiter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
@PropertySource("classpath:/local.properties")
open class FestivalCompareApplication {

//    @Bean
//    fun spotifyClient(@Value("\${spotify.apiEndpoint}") apiEndpoint: String,
//                      @Value("\${spotify.authenticationEndpoint}") authenticationEndpoint: String,
//                      @Value("\${spotify.clientId}") clientId: String,
//                      @Value("\${spotify.clientSecret}") clientSecret: String): SpotifyClient {
//
//        return SpotifyClientImpl(apiEndpoint, SpotifyOAuthInterceptor(authenticationEndpoint, clientId, clientSecret))
//    }

    @Profile("!e2e")
    @Bean
    fun rateLimitedSpotifyClient(@Value("\${spotify.apiEndpoint}") apiEndpoint: String,
                                 @Value("\${spotify.authenticationEndpoint}") authenticationEndpoint: String,
                                 @Value("\${spotify.clientId}") clientId: String,
                                 @Value("\${spotify.clientSecret}") clientSecret: String,
                                 clock: Clock): SpotifyClient {

        // TODO this could be an annotation on a service which could do this through proxy
        val spotifyClient = SpotifyClientImpl(apiEndpoint, SpotifyOAuthInterceptor(authenticationEndpoint, clientId, clientSecret))
        return RateLimitedSpotifyClient(spotifyClient, BlockingRateLimiter(clock))
    }

    @Profile("e2e")
    @Bean
    fun mockSpotifyClient(): SpotifyClient {
        val artists = mapOf(
                "The National" to Artist("The National", 74),
                "The Smiths" to Artist("The Smiths", 70),
                "Leonard Cohen" to Artist("Leonard Cohen", 80),
                "PJ Harvey" to Artist("PJ Harvey", 72)
        )
        return object : SpotifyClient {
            override fun searchArtist(name: String): Artist? = artists[name]
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(FestivalCompareApplication::class.java, *args)
}
