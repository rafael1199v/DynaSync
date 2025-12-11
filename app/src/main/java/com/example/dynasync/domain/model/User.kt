package com.example.dynasync.domain.model

data class User(
    val id: Int,
    val name: String,
    val lastName: String,
    val age: Int,
    val profileImageUrl: String? = null
)
