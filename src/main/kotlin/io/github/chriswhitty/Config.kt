package io.github.chriswhitty

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class Config {

    @Bean open fun spotifyHost(): String {
        return "https://api.spotify.com"
    }

}

