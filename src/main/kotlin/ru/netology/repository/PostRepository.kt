package ru.netology.repository

import ru.netology.model.PostModel

interface PostRepository {
    suspend fun getAll(): List<PostModel>
    suspend fun getById(id: Long): PostModel?
    suspend fun save(item: PostModel): PostModel
    suspend fun removeById(id: Long)
    suspend fun getRecent(count: Int): List<PostModel>
    suspend fun getPostsAfter(id: Long): List<PostModel>
    suspend fun getPostsCreatedBefore(idCurPost: Long, countUploadedPosts: Int): List<PostModel>
}