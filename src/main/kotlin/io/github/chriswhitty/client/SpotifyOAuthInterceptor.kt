package io.github.chriswhitty.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.*
import java.util.*


data class SpotifyAuthResponse (
        var access_token: String,
        var token_type: String,
        var expires_in: String
)

open class SpotifyOAuthInterceptor(
        private val authenticationEndpoint: String,
        private val clientId: String,
        private val clientSecret: String,
        private val client: OkHttpClient = OkHttpClient.Builder().build()) : Interceptor {

    private var authResponse: SpotifyAuthResponse? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val response = chain.proceed(addAuthHeader(request))

        if(response.code() == 401) {
             authResponse = updateClientGrantAuthToken()
        }

        return chain.proceed(addAuthHeader(request))
    }

    private fun addAuthHeader(request: Request): Request {
        val response = authResponse ?: return request
        return request.newBuilder()
                .addHeader("Authorization", "Bearer ${response.access_token}")
                .build()
    }

    private fun updateClientGrantAuthToken(): SpotifyAuthResponse {
        synchronized(client) {

            val authHeader = Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())
            val formBody = FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .build()

            val authRequest = Request.Builder()
                    .post(formBody)
                    .url(authenticationEndpoint)
                    .header("Authorization", ("Basic $authHeader"))
                    .build()

            val objectMapper = ObjectMapper()
                    .registerKotlinModule()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            val authResponse = client.newCall(authRequest).execute()
            return objectMapper.readValue(authResponse.body().byteStream(), SpotifyAuthResponse::class.java)
        }
    }

}

