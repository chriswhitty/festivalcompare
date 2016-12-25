package io.github.chriswhitty

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class FestivalCompareApplication

fun main(args: Array<String>) {
    SpringApplication.run(FestivalCompareApplication::class.java, *args)
}
