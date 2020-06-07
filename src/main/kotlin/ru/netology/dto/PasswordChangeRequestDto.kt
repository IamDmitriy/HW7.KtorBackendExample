package ru.netology.dto

data class PasswordChangeRequestDto(
    val old: String,
    val new: String
)