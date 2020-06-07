package ru.netology

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.ContentNegotiation
import io.ktor.features.ParameterConversionException
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.server.cio.EngineMain
import kotlinx.coroutines.runBlocking
import org.kodein.di.generic.*
import org.kodein.di.ktor.KodeinFeature
import org.kodein.di.ktor.kodein
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryInMemoryImpl
import ru.netology.repository.UserRepository
import ru.netology.repository.UserRepositoryInMemoryWithMutexImpl
import ru.netology.route.RoutingV1
import ru.netology.service.FileService
import ru.netology.service.JWTTokenService
import ru.netology.service.PostService
import ru.netology.service.UserService
import javax.naming.ConfigurationException

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
        exception<ForbiddenException> { e ->
            call.respond(HttpStatusCode.Forbidden)
            throw e
        }
        exception<RegistrationException> { e ->
            call.respond(HttpStatusCode.BadRequest, Error("Пользователь с таким логином уже существует!"))
            throw e
        }
    }

    install(KodeinFeature) {
        constant(tag = "upload-dir") with (
                environment.config.propertyOrNull("ncraft.upload.dir")?.getString()
                    ?: throw ConfigurationException("Upload dir is not specified")
                )
        bind<PostRepository>() with singleton {
            PostRepositoryInMemoryImpl().apply {
                runBlocking {
                    main()
                }
            }
        }
        bind<UserRepository>() with eagerSingleton { UserRepositoryInMemoryWithMutexImpl() }

        bind<PostService>() with eagerSingleton { PostService(instance()) }
        bind<FileService>() with eagerSingleton { FileService(instance(tag = "upload-dir")) }
        bind<UserService>() with eagerSingleton {
            UserService(
                repo = instance(),
                tokenService = instance(),
                passwordEncoder = instance()
            ).apply {
                runBlocking {
                    saveNewModel(username = "Dmitriy", password = "qwerty")
                }
            }
        }
        bind<JWTTokenService>() with eagerSingleton { JWTTokenService() }

        bind<PasswordEncoder>() with eagerSingleton { BCryptPasswordEncoder() }

        bind<RoutingV1>() with eagerSingleton {
            RoutingV1(
                staticPath = instance(tag = "upload-dir"),
                repo = instance(),
                fileService = instance(),
                userService = instance(),
                postService = instance()
            )
        }
    }

    install(Authentication) {
        jwt {
            val jwtService by kodein().instance<JWTTokenService>()
            verifier(jwtService.verifier)
            val userService by kodein().instance<UserService>()

            validate {
                val id = it.payload.getClaim("id").asLong()
                userService.getModelById(id)
            }
        }
    }

    install(Routing) {

        val routingV1 by kodein().instance<RoutingV1>()
        routingV1.setup(this)
    }
}