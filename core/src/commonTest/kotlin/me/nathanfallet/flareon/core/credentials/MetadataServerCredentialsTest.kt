package me.nathanfallet.flareon.core.credentials

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class MetadataServerCredentialsTest {
    private val tokenJson = """{"access_token":"ya29.token","expires_in":3599,"token_type":"Bearer"}"""
    private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")

    private fun client(requests: MutableList<HttpRequestData>, handler: (HttpRequestData) -> String): HttpClient =
        HttpClient(MockEngine { request ->
            requests += request
            respond(handler(request), HttpStatusCode.OK, jsonHeaders)
        }) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
            expectSuccess = true
        }

    @Test
    fun getsTokenFromMetadataServerWithFlavorHeader() = runTest {
        val requests = mutableListOf<HttpRequestData>()
        val credentials = GoogleCredentials.fromMetadataServer(client(requests) { tokenJson })

        assertEquals("ya29.token", credentials.getAccessToken())

        val request = requests.single()
        assertEquals(
            "http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/token",
            request.url.toString()
        )
        assertEquals("Google", request.headers["Metadata-Flavor"])
    }

    @Test
    fun cachesTokenBetweenCalls() = runTest {
        val requests = mutableListOf<HttpRequestData>()
        val credentials = GoogleCredentials.fromMetadataServer(client(requests) { tokenJson })

        credentials.getAccessToken()
        credentials.getAccessToken()
        assertEquals(1, requests.size)

        credentials.clearCache()
        credentials.getAccessToken()
        assertEquals(2, requests.size)
    }

    @Test
    fun sendsRequestedScopes() = runTest {
        val requests = mutableListOf<HttpRequestData>()
        val credentials = GoogleCredentials.fromMetadataServer(
            client(requests) { tokenJson },
            baseUrl = "http://localhost:8080/",
            scopes = listOf("scope-a", "scope-b"),
        )

        credentials.getAccessToken()

        val url = requests.single().url
        assertEquals("localhost", url.host)
        assertEquals("scope-a,scope-b", url.parameters["scopes"])
    }

    @Test
    fun readsAndCachesProjectId() = runTest {
        val requests = mutableListOf<HttpRequestData>()
        val credentials = GoogleCredentials.fromMetadataServer(client(requests) { "my-project\n" })

        assertEquals("my-project", credentials.getProjectId())
        assertEquals("my-project", credentials.getProjectId())

        assertEquals(1, requests.size)
        assertEquals("/computeMetadata/v1/project/project-id", requests.single().url.encodedPath)
        assertEquals("Google", requests.single().headers["Metadata-Flavor"])
    }
}
