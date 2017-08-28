package io.github.chriswhitty.client

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.http.ResponseDefinition
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import okhttp3.OkHttpClient
import okhttp3.Request
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import java.util.*


class SpotifyOAuthInterceptorTest {

    @Rule @JvmField var wireMockRule = WireMockRule(8089)

    val unauthenticatedRequest = RequestPatternBuilder.newRequestPattern(RequestMethod.GET, WireMock.urlPathEqualTo("/resource")).build()

    val clientId = "client-id"
    val clientSecret = "cient-secret"
    val expectedAuthHeader = Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())
    val accessToken = "access-token"

    val authorizationRequest = RequestPatternBuilder.newRequestPattern(RequestMethod.POST, WireMock.urlPathEqualTo("/authenticate"))
            .withRequestBody(WireMock.equalTo("grant_type=client_credentials"))
            .withHeader("Authorization", WireMock.equalTo("Basic $expectedAuthHeader"))
            .build()

    val authenticatedRequest = RequestPatternBuilder.newRequestPattern(RequestMethod.GET, WireMock.urlPathEqualTo("/resource"))
            .withHeader("Authorization", WireMock.equalTo("Bearer $accessToken"))
            .build()

    @Test
    fun `should request auth token and retry once granted`() {

        wireMockRule.addStubMapping(StubMapping(unauthenticatedRequest, ResponseDefinition(401, "")))

        wireMockRule.addStubMapping(StubMapping(authorizationRequest,
                ResponseDefinition(401, """
                    {
                       "access_token": "$accessToken",
                       "token_type": "bearer",
                       "expires_in": 3600
                    }
                """)))

        wireMockRule.addStubMapping(StubMapping(authenticatedRequest, ResponseDefinition(200, "")))

        val oAuthClientGrantInterceptor = SpotifyOAuthInterceptor(
                "http://localhost:${wireMockRule.port()}/authenticate",
                clientId,
                clientSecret)

        val client = OkHttpClient.Builder()
                .addInterceptor(oAuthClientGrantInterceptor)
                .build()

        val request = Request.Builder()
                .url("http://localhost:${wireMockRule.port()}/resource")
                .build()

        val response = client.newCall(request).execute()
        response.body().close()
        assertThat(response.code(), equalTo(200))
    }

}