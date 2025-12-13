package com.example.dynasync.domain.model

import kotlin.time.Instant

//Representa a la clase al Staff
data class Personal(
    val id: Int,
    val name: String,
    val lastname: String,
    val charge: String,
    val imageUrl: String? = null,
    val createdAt: Instant? = null
)
