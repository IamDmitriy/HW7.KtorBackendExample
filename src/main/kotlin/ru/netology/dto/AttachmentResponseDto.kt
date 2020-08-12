package ru.netology.dto

import ru.netology.model.AttachmentType


class AttachmentResponseDto(
    val id: String,
    val type: AttachmentType
)