package ru.netology

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.Assert.assertEquals
import org.junit.Test

class ApplicationTest {

    @Test
    fun `test get all`() {
        withTestApplication({ module() }) {
            with(handleRequest(HttpMethod.Get, "/api/v1/posts")) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    ContentType.Application.Json.withCharset(Charsets.UTF_8),
                    response.contentType()
                )
            }
        }
    }

    @Test
    fun `test get by id`() {
        withTestApplication({ module() }) {
            with(handleRequest(HttpMethod.Get, "/api/v1/posts/1")) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    ContentType.Application.Json.withCharset(Charsets.UTF_8),
                    response.contentType()
                )
            }
        }
    }


    @Test
    fun `test delete by id`() {
        withTestApplication({ module() }) {
            with(handleRequest(HttpMethod.Delete, "/api/v1/posts/1")) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    ContentType.Application.Json.withCharset(Charsets.UTF_8),
                    response.contentType()
                )
            }
        }
    }

    @Test
    fun `test like by id`() {
        withTestApplication({ module() }) {
            with(handleRequest(HttpMethod.Post, "/api/v1/posts/1/likes")) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    ContentType.Application.Json.withCharset(Charsets.UTF_8),
                    response.contentType()
                )
            }
        }
    }

    @Test
    fun `test dislike by id`() {
        withTestApplication({ module() }) {
            with(handleRequest(HttpMethod.Delete, "/api/v1/posts/1/likes")) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    ContentType.Application.Json.withCharset(Charsets.UTF_8),
                    response.contentType()
                )
            }
        }
    }


}