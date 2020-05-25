package ru.netology.model

data class PostModel(
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
)