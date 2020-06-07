package ru.netology.service

import io.ktor.features.NotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import ru.netology.PasswordChangeException
import ru.netology.RegistrationException
import ru.netology.dto.*
import ru.netology.model.UserModel
import ru.netology.repository.UserRepository

class UserService(
    private val repo: UserRepository,
    private val tokenService: JWTTokenService,
    private val passwordEncoder: PasswordEncoder
) {
    suspend fun getModelById(id: Long): UserModel? {
        return repo.getById(id)
    }

    suspend fun getById(id: Long): UserResponseDto {
        val model = repo.getById(id) ?: throw NotFoundException()
        return UserResponseDto.fromModel(model)
    }

    suspend fun changePassword(id: Long, input: PasswordChangeRequestDto) {
        // TODO: handle concurrency
        val model = repo.getById(id) ?: throw NotFoundException()
        if (!passwordEncoder.matches(input.old, model.password)) {
            throw PasswordChangeException("Wrong password!")
        }
        val copy = model.copy(password = passwordEncoder.encode(input.new))
        repo.save(copy)
    }

    suspend fun authenticate(input: AuthenticationRequestDto): AuthenticationResponseDto {
        val model = repo.getByUsername(input.username) ?: throw NotFoundException()

        if (!passwordEncoder.matches(input.password, model.password)) {
            throw PasswordChangeException("Wrong password!")
        }
        val token = tokenService.generate(model.id)
        return AuthenticationResponseDto(token)
    }

    suspend fun saveNewModel(username: String, password: String): UserModel {
        val model = UserModel(username = username, password = passwordEncoder.encode(password))
        return repo.save(model)
    }

    suspend fun register(input: RegistrationRequestDto): RegistrationResponseDto {
        if (repo.getByUsername(input.username) != null) {
            throw RegistrationException()
        }

        val model = saveNewModel(input.username, input.password)

        val token = tokenService.generate(model.id)
        return RegistrationResponseDto(token)
    }
}