package ru.netology.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JWTTokenService {
    private val secret = "5c2dbef6-289c-46e6-8cfd-d8b3292d373a" //TODO вынести в Application.conf
    private val algo = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT.require(algo).build()

    fun generate(id: Long): String = JWT.create()
        .withClaim("id", id)
        .withExpiresAt(Date(System.currentTimeMillis() + 1000000))
        .sign(algo)
}