package ru.netology.service

import io.ktor.features.NotFoundException
import ru.netology.ForbiddenException
import ru.netology.dto.PostRequestDto
import ru.netology.dto.PostResponseDto
import ru.netology.model.UserModel
import ru.netology.repository.PostRepository

class PostService(
    private val repo: PostRepository
) {
    suspend fun getAll(): List<PostResponseDto> = repo.getAll().map(PostResponseDto.Companion::fromModel)

    suspend fun getById(id: Long): PostResponseDto {
        val model = repo.getById(id) ?: throw NotFoundException()
        return PostResponseDto.fromModel(model)
    }

    suspend fun save(request: PostRequestDto, user: UserModel): PostResponseDto {
        if (user.username != request.author) { //TODO реализовать через id
            throw ForbiddenException("Невозможно редактировать!")
        }
        val model = repo.save(
            PostRequestDto.toModel(request)
        )
        return PostResponseDto.fromModel(model)
    }

    suspend fun deleteById(id: Long, user: UserModel): PostResponseDto {
        val model = repo.getById(id) ?: throw NotFoundException()

        if (user.username != model.author) {
            throw ForbiddenException("Невозможно удалить пост!")
        }

        val response = PostResponseDto.fromModel(model)
        repo.removeById(id)
        return response
    }

    suspend fun likeById(id: Long): PostResponseDto {
        val model = repo.getById(id) ?: throw NotFoundException()
        val copy =
            model.copy(countLikes = model.countLikes.inc()) //TODO реализовать учёт авторства лайков на строне сервера
        return PostResponseDto.fromModel(repo.save(copy))
    }

    suspend fun dislikeById(id: Long): PostResponseDto {
        val model = repo.getById(id) ?: throw NotFoundException()
        val copy =
            model.copy(countLikes = model.countLikes.dec())
        return PostResponseDto.fromModel(repo.save(copy))
    }
}