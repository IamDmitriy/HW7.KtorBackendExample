package ru.netology.route

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.features.ParameterConversionException
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.*
import ru.netology.dto.*
import ru.netology.model.UserModel
import ru.netology.repository.PostRepository
import ru.netology.service.FileService
import ru.netology.service.PostService
import ru.netology.service.UserService

class RoutingV1(
    private val staticPath: String,
    private val repo: PostRepository,
    private val fileService: FileService,
    private val userService: UserService,
    private val postService: PostService
) {
    fun setup(configuration: Routing) {
        with(configuration) {

            route("/api/v1/") {
                static("/static") {
                    files(staticPath)
                }

                route("/") {
                    post("/registration") {
                        val input = call.receive<RegistrationRequestDto>()
                        val response = userService.register(input)
                        call.respond(response)
                    }
                    post("/authentication") {
                        val input = call.receive<AuthenticationRequestDto>()
                        val response = userService.authenticate(input)
                        call.respond(response)
                    }
                }

                authenticate {
                    route("/me") {
                        get {
                            val me = call.authentication.principal<UserModel>()
                            call.respond(UserResponseDto.fromModel(me!!))
                        }
                    }

                    route("/media") {
                        post {
                            val multipart = call.receiveMultipart()
                            val response = fileService.save(multipart)
                            call.respond(response)
                        }
                    }

                    route("/posts") {
                        get {
                            call.respond(postService.getAll())
                        }
                        get("/{id}") {
                            val id =
                                call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                                )
                            call.respond(postService.getById(id))
                        }
                        post {
                            val me = call.authentication.principal<UserModel>()
                            val request = call.receive<PostRequestDto>()
                            call.respond(postService.save(request, me!!))
                        }
                        delete("/{id}") {
                            val id =
                                call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                                )
                            val me = call.authentication.principal<UserModel>()
                            call.respond(postService.deleteById(id, me!!))
                        }
                        post("/{id}/likes") {
                            val id =
                                call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                                )
                            call.respond(postService.likeById(id))
                        }
                        delete("/{id}/likes") {
                            val id =
                                call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                                )
                            call.respond(postService.dislikeById(id))
                        }
                        post("/{id}/reposts") {
                            val id =
                                call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                                )
                            val repostRequestDto = call.receive<RepostRequestDto>()
                            val me = call.authentication.principal<UserModel>()
                            call.respond(postService.repostById(id, me!!, repostRequestDto))
                        }
                        get("/{count}/recent/") { ///recent/10 (
                            val count =
                                call.parameters["count"]?.toIntOrNull() ?: throw ParameterConversionException(
                                    "count",
                                    "Int"
                                )
                            call.respond(postService.getRecent(count))
                        }
                        get("/{id}/after") {
                            val id =
                                call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                                )
                            call.respond(postService.getPostsAfter(id))
                        }
                        post("/before") {
                            val postsCreatedBeforeRequestDto = call.receive<PostsCreatedBeforeRequestDto>()
                            call.respond(postService.getPostsCreatedBefore(postsCreatedBeforeRequestDto))
                        }
                        post("/create") {
                            val createPostRequestDto = call.receive<CreatePostRequestDto>()
                            val me = call.authentication.principal<UserModel>()
                            val postResponseDto = postService.createPost(createPostRequestDto, me!!)
                            call.respond(postResponseDto)
                        }

                    }
                }

            }
        }
    }
}

