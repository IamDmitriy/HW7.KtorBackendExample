package ru.netology.dto

import ru.netology.model.UserModel

class UserResponseDto(
    val id: Long,
    val username: String
) {
    companion object {
        fun fromModel(model: UserModel) = UserResponseDto(
            id = model.id,
            username = model.username
        )
    }
}
