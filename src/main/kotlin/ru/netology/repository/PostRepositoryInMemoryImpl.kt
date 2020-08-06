package ru.netology.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.netology.PostNotFoundException
import ru.netology.model.Location
import ru.netology.model.PostModel
import ru.netology.model.PostType
import kotlin.coroutines.EmptyCoroutineContext

class PostRepositoryInMemoryImpl : PostRepository {
    private val items = mutableListOf<PostModel>()
    private var nextId: Long = 0
    private val mutex = Mutex()

    override suspend fun getAll(): List<PostModel> = mutex.withLock {
        items.asReversed().toList()
    }

    override suspend fun getById(id: Long): PostModel? = mutex.withLock {
        items.find { it.id == id }
    }

    override suspend fun save(item: PostModel): PostModel = mutex.withLock {
        when (val index = items.indexOfFirst { it.id == item.id }) {
            -1 -> {
                val copy = item.copy(id = nextId++)
                items.add(copy)
                copy
            }
            else -> {
                items[index] = item
                item
            }
        }
    }

    override suspend fun removeById(id: Long) {
        mutex.withLock {
            items.removeIf { it.id == id }
        }
    }

    override suspend fun getRecent(count: Int): List<PostModel> {
        if (count >= items.size) {
            return items.asReversed().toList()
        }
        if (count == 0) {
            return emptyList()
        }

        val recent = mutableListOf<PostModel>()
        for (i in 1..count) {
            recent.add(items[items.size - i])
        }
        return recent
    }

    override suspend fun getPostsAfter(id: Long): List<PostModel> {
        val targetPost = items.find { it.id == id } ?: throw PostNotFoundException()
        val newPosts = mutableListOf<PostModel>()
        items.forEach {
            if (it.created > targetPost.created) {
                newPosts.add(it)
            }
        }

        return newPosts.sortedByDescending { it.created }
    }

    override suspend fun getPostsCreatedBefore(idCurPost: Long, countUploadedPosts: Int): List<PostModel> {
        val targetPost = items.find { it.id == idCurPost } ?: throw PostNotFoundException()
        val oldPosts = mutableListOf<PostModel>()
        items.forEach {
            if (it.created < targetPost.created) {
                oldPosts.add(it)
            }
        }

        return oldPosts.sortedByDescending { it.created }
    }

    private fun generateContent() {
        items.addAll(
            listOf(
                PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1588712400000,
                    type = PostType.EVENT,
                    address = "Варшавское ш., 1, с. 17. Бизнес-центр W Plaza-2",
                    sharedByMe = true,
                    countShares = 1,
                    commentedByMe = true,
                    countComments = 1
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1583010000000,
                    type = PostType.EVENT,
                    location = Location(55.703810, 37.623851)
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "REPOST: First post in our network!",
                    created = 1583010000000,
                    source = PostModel(
                        id = nextId++,
                        author = "Netology",
                        content = "someContent",
                        created = 1551819600000
                    ),
                    type = PostType.REPOST
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "REPOST: First post in our network!",
                    created = 1583010000000,
                    type = PostType.REPOST
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1520283600000,
                    type = PostType.ADVERTISEMENT,
                    link = "https://www.google.com/"
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1520283600000,
                    videoUrl = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
                    type = PostType.VIDEO
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1520283600000,
                    type = PostType.POST
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1588712400000,
                    type = PostType.EVENT,
                    address = "Варшавское ш., 1, с. 17. Бизнес-центр W Plaza-2"
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1583010000000,
                    type = PostType.EVENT,
                    location = Location(55.703810, 37.623851)
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "REPOST: First post in our network!",
                    created = 1583010000000,
                    source = PostModel(
                        id = nextId++,
                        author = "Netology",
                        content = "someContent",
                        created = 1551819600000
                    ),
                    type = PostType.REPOST
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1520283600000,
                    type = PostType.ADVERTISEMENT,
                    link = "https://www.google.com/"
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1520283600000,
                    videoUrl = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
                    type = PostType.VIDEO
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1520283600000,
                    type = PostType.POST
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1588712400000,
                    type = PostType.EVENT,
                    address = "Варшавское ш., 1, с. 17. Бизнес-центр W Plaza-2"
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1583010000000,
                    type = PostType.EVENT,
                    location = Location(55.703810, 37.623851)
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "REPOST: First post in our network!",
                    created = 1583010000000,
                    source = PostModel(
                        id = nextId++,
                        author = "Netology",
                        content = "someContent",
                        created = 1551819600000
                    ),
                    type = PostType.REPOST
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1520283600000,
                    type = PostType.ADVERTISEMENT,
                    link = "https://www.google.com/"
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1520283600000,
                    videoUrl = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
                    type = PostType.VIDEO
                ), PostModel(
                    id = nextId++,
                    author = "Netology",
                    content = "First post in our network!",
                    created = 1520283600000,
                    type = PostType.POST
                )
            ).sortedBy { it.created }
        )
    }

    fun main() {
        CoroutineScope(EmptyCoroutineContext).launch {
            mutex.withLock {
                generateContent()
            }
        }

    }
}
