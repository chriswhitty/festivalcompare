package io.github.chriswhitty

import io.github.chriswhitty.client.SpotifyClient
import io.github.chriswhitty.client.SpotifyClientImpl
import io.github.chriswhitty.client.SpotifyOAuthInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class FestivalCompareApplication {

    @Bean
    fun spotifyClient(@Value("\${spotify.apiEndpoint}") apiEndpoint: String,
                      @Value("\${spotify.authenticationEndpoint}") authenticationEndpoint: String,
                      @Value("\${spotify.clientId}") clientId: String,
                      @Value("\${spotify.clientSecret}") clientSecret: String): SpotifyClient {

        return SpotifyClientImpl(apiEndpoint, SpotifyOAuthInterceptor(authenticationEndpoint, clientId, clientSecret))
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(FestivalCompareApplication::class.java, *args)
}
