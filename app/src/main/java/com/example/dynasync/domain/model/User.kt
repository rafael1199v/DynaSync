package com.example.dynasync.domain.model

import kotlin.time.Instant

data class User(
    val id: String,
    val name: String,
    val lastName: String,
    val age: Int,
    val profileImageUrl: String? = null,
    val createdAt: Instant? = null
)
