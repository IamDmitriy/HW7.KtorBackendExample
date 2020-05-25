package ru.netology.dto

import ru.netology.model.Location
import ru.netology.model.PostModel
import ru.netology.model.PostType

data class PostRequestDto(
    val id: Long,
    val author: String,
    val content: String,
    val created: Long,
    val likedByMe: Boolean = false,
    val countLikes: Int = 10,
    val commentedByMe: Boolean = false,
    val countComments: Int = 0,
    val sharedByMe: Boolean = false,
    val countShares: Int = 0,
    val videoUrl: String? = null,
    val type: PostType = PostType.POST,
    val source: PostModel? = null,
    val address: String? = null,
    val location: Location? = null,
    val link: String? = null
)  {
    companion object {
        fun toModel(dto: PostRequestDto) = PostModel(
            id = dto.id,
            author = dto.author,
            content = dto.content,
            created = dto.created,
            likedByMe = dto.likedByMe,
            countLikes = dto.countLikes,
            commentedByMe = dto.commentedByMe,
            countComments = dto.countComments,
            sharedByMe = dto.sharedByMe,
            countShares = dto.countShares,
            videoUrl = dto.videoUrl,
            type = dto.type,
            source = dto.source,
            address = dto.address,
            location = dto.location,
            link = dto.link
        )
    }
}