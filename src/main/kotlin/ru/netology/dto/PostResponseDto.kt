package ru.netology.dto

import ru.netology.model.Location
import ru.netology.model.PostModel
import ru.netology.model.PostType

class PostResponseDto(
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
) {


    companion object {
        fun fromModel(model: PostModel) = PostResponseDto(
            id = model.id,
            author = model.author,
            content = model.content,
            created = model.created,
            likedByMe = model.likedByMe,
            countLikes = model.countLikes,
            commentedByMe = model.commentedByMe,
            countComments = model.countComments,
            sharedByMe = model.sharedByMe,
            countShares = model.countShares,
            videoUrl = model.videoUrl,
            type = model.type,
            source = model.source,
            address = model.address,
            location = model.location,
            link = model.link
        )
    }
}
