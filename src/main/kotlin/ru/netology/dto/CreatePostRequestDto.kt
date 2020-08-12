package ru.netology.dto

import ru.netology.model.AttachmentModel

data class CreatePostRequestDto(val content: String, val attachment: AttachmentModel?)
