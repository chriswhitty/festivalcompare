package io.github.chriswhitty.integration

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.equalTo
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
        val formBody = FormBody.Builder()
                .add("name", "Festival")
                .add("artists", "The National\nNirvana")
                .build()

        val request = Request.Builder()
                .url("http://localhost:${port}/")
                .post(formBody)
                .build()

        val response = okHttpClient.newCall(request).execute()
        assertThat(response.isSuccessful, equalTo(true))
        assertThat(response.body().string(), containsString("Average score: <span>74</span>"))
    }

    //TODO report mismatches
}
