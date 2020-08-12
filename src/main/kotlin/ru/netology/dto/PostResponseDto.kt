package ru.netology.dto

import ru.netology.model.AttachmentModel
import ru.netology.model.Location
import ru.netology.model.PostModel
import ru.netology.model.PostType

class PostResponseDto(
    val id: Long,
    val author: String,
    val content: String,
    val created: Long,
    val likedByMe: Boolean,
    val countLikes: Int,
    val commentedByMe: Boolean,
    val countComments: Int,
    val sharedByMe: Boolean,
    val countShares: Int,
    val videoUrl: String?,
    val type: PostType,
    val source: PostModel?,
    val address: String?,
    val location: Location?,
    val link: String?,
    val attachment: AttachmentModel?
) {

    companion object {
        fun fromModel(model: PostModel) = PostResponseDto(
            model.id,
            model.author,
            model.content,
            model.created,
            model.likedByMe,
            model.countLikes,
            model.commentedByMe,
            model.countComments,
            model.sharedByMe,
            model.countShares,
            model.videoUrl,
            model.type,
            model.source,
            model.address,
            model.location,
            model.link,
            model.attachment
        )
    }
}
