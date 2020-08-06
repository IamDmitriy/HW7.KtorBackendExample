package ru.netology.dto

data class PostsCreatedBeforeRequestDto(
    val idCurPost: Long,
    val countUploadedPosts: Int
)