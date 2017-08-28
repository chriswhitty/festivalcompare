package io.github.chriswhitty

import io.github.chriswhitty.client.RateLimitedSpotifyClient
import io.github.chriswhitty.client.SpotifyClient
import io.github.chriswhitty.client.SpotifyClientImpl
import io.github.chriswhitty.client.SpotifyOAuthInterceptor
import io.github.chriswhitty.clock.Clock
import io.github.chriswhitty.ratelimiter.BlockingRateLimiter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
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

}

fun main(args: Array<String>) {
    SpringApplication.run(FestivalCompareApplication::class.java, *args)
}
