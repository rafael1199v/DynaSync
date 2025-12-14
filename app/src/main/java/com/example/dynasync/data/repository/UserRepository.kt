package com.example.dynasync.data.repository

import com.example.dynasync.domain.model.User
import kotlin.time.Instant

object UserRepository {

    suspend fun getUser(uuid: String): User {
        return User(
            id = "uuid",
            name = "Rafael Andres",
            lastName = "Vargas Mamani",
            age = 20,
            profileImageUrl = "https://media.gettyimages.com/id/495992888/es/foto/portland-or-october-16-linus-torvalds-a-software-engineer-and-principal-creator-of-the-linux.jpg?s=1024x1024&w=gi&k=20&c=qNKDim5DkJnPZhA-IUfIOxW60qwujf90Td7sL3FR7GU=",
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        )
    }
}