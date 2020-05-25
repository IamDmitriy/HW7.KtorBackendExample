package ru.netology

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.NotFoundException
import io.ktor.features.ParameterConversionException
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.cio.EngineMain
import kotlinx.coroutines.runBlocking
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.ktor.KodeinFeature
import org.kodein.di.ktor.kodein
import ru.netology.dto.PostRequestDto
import ru.netology.dto.PostResponseDto
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryInMemoryImpl

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
        }
    }


    install(StatusPages) {
        exception<NotImplementedError> { e ->
            call.respond(HttpStatusCode.NotImplemented, Error("Error"))
            throw e
        }
        exception<ParameterConversionException> { e ->
            call.respond(HttpStatusCode.BadRequest)
            throw e
        }
        exception<Throwable> { e ->
            call.respond(HttpStatusCode.InternalServerError)
            throw e
        }
    }

    install(KodeinFeature) {
        bind<PostRepository>() with singleton {
            PostRepositoryInMemoryImpl().apply {
                runBlocking {
                    main()
                }
            }
        }
    }

    //TODO вынести в v1
    install(Routing) {
        val repo by kodein().instance<PostRepository>()

        route("/api/v1/posts") {
            get {
                val response = repo.getAll().map(PostResponseDto.Companion::fromModel)
                call.respond(response)
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                val model = repo.getById(id) ?: throw NotFoundException()
                val response = PostResponseDto.fromModel(model)
                call.respond(response)
            }
            post {
                val request = call.receive<PostRequestDto>()
                val model = PostRequestDto.toModel(request)
                val modelForResponse = repo.save(model) //Почему мы отпрвляем модель , а не ResponseDTO?
                val response = PostResponseDto.fromModel(modelForResponse)
                call.respond(response)
            }
            delete("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                val model = repo.getById(id) ?: throw NotFoundException()
                val response = PostResponseDto.fromModel(model)
                repo.removeById(id)
                call.respond(response)
                //Нужно ли что-нибудь возвращать? true/false? или Exception
            }
            post("/{id}/likes") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                val modelForResponse = repo.likeById(id) ?: throw NotFoundException()
                val response = PostResponseDto.fromModel(modelForResponse)
                call.respond(response)
            }

            delete("/{id}/likes") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                val modelForResponse = repo.dislikeById(id) ?: throw NotFoundException()
                val response = PostResponseDto.fromModel(modelForResponse)
                call.respond(response)
            }
        }
    }
}