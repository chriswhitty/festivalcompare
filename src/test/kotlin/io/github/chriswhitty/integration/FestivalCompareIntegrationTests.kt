package io.github.chriswhitty.integration

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class FestivalCompareIntegrationTests {

    val okHttpClient = OkHttpClient()

    @LocalServerPort lateinit var port: Integer

    @Test
    fun returnsScore_whenPostingFestival() {
        val nonExistingBand = "sdohaosdfhashduahfhad"

        val formBody = FormBody.Builder()
                .add("name", "Festival")
                .add("artists", "The National\nNirvana\n$nonExistingBand")
                .build()

        val request = Request.Builder()
                .url("http://localhost:${port}/")
                .post(formBody)
                .build()

        val response = okHttpClient.newCall(request).execute()
        assertThat(response.isSuccessful, equalTo(true))
        val responseBody = response.body().string()

        val matchResult = "Average score: <span>(\\d+)</span>".toRegex().find(responseBody, 0)
        assertThat("Could not find score in page", matchResult, notNullValue())
        assertThat(Integer.parseInt(matchResult?.groupValues?.get(1)), allOf(greaterThan(50), lessThan(100)))
        assertThat(responseBody, Matchers.containsString("""
            |    <ul>
            |        <li>$nonExistingBand</li>
            |    </ul>""".trimMargin()))
    }

    //TODO Handle empty rows
    //TODO artist name match should be case insensitive
}

