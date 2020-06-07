package ru.netology.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.netology.model.Location
import ru.netology.model.PostModel
import ru.netology.model.PostType
import kotlin.coroutines.EmptyCoroutineContext

class PostRepositoryInMemoryImpl : PostRepository {
    private val items = mutableListOf<PostModel>()
    private var maxId: Long = 0
    private val mutex = Mutex()

    override suspend fun getAll(): List<PostModel> = mutex.withLock {
        items.toList()
    }

    override suspend fun getById(id: Long): PostModel? = mutex.withLock {
            items.find { it.id == id }
        }

    override suspend fun save(item: PostModel): PostModel = mutex.withLock {
        when (val index = items.indexOfFirst { it.id == item.id }) {
            -1 -> {
                val copy = item.copy(id = maxId++)
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

    private fun generateContent(): List<PostModel> {

        return listOf(
            PostModel(
                id = maxId++,
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
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1583010000000,
                type = PostType.EVENT,
                location = Location(55.703810, 37.623851)
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "REPOST: First post in our network!",
                created = 1583010000000,
                source = PostModel(
                    id = maxId++,
                    author = "Netology",
                    content = "someContent",
                    created = 1551819600000
                ),
                type = PostType.REPOST
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "REPOST: First post in our network!",
                created = 1583010000000,
                type = PostType.REPOST
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1520283600000,
                type = PostType.ADVERTISEMENT,
                link = "https://www.google.com/"
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1520283600000,
                videoUrl = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
                type = PostType.VIDEO
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1520283600000,
                type = PostType.POST
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1588712400000,
                type = PostType.EVENT,
                address = "Варшавское ш., 1, с. 17. Бизнес-центр W Plaza-2"
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1583010000000,
                type = PostType.EVENT,
                location = Location(55.703810, 37.623851)
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "REPOST: First post in our network!",
                created = 1583010000000,
                source = PostModel(
                    id = maxId++,
                    author = "Netology",
                    content = "someContent",
                    created = 1551819600000
                ),
                type = PostType.REPOST
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1520283600000,
                type = PostType.ADVERTISEMENT,
                link = "https://www.google.com/"
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1520283600000,
                videoUrl = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
                type = PostType.VIDEO
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1520283600000,
                type = PostType.POST
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1588712400000,
                type = PostType.EVENT,
                address = "Варшавское ш., 1, с. 17. Бизнес-центр W Plaza-2"
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1583010000000,
                type = PostType.EVENT,
                location = Location(55.703810, 37.623851)
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "REPOST: First post in our network!",
                created = 1583010000000,
                source = PostModel(
                    id = maxId++,
                    author = "Netology",
                    content = "someContent",
                    created = 1551819600000
                ),
                type = PostType.REPOST
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1520283600000,
                type = PostType.ADVERTISEMENT,
                link = "https://www.google.com/"
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1520283600000,
                videoUrl = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
                type = PostType.VIDEO
            ), PostModel(
                id = maxId++,
                author = "Netology",
                content = "First post in our network!",
                created = 1520283600000,
                type = PostType.POST
            )
        )
    }

    fun main() {

            CoroutineScope(EmptyCoroutineContext).launch {
                mutex.withLock{
                    items.addAll(generateContent())
                }
            }

    }

}
