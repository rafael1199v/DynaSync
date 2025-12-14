package com.example.dynasync.data.repository

import com.example.dynasync.data.dto.ProfileDto
import com.example.dynasync.data.mapper.toDomain
import com.example.dynasync.data.supabase.SupabaseClientObject
import com.example.dynasync.domain.model.User
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlin.time.Instant

object UserRepository {

    val supabaseClient = SupabaseClientObject.client

    suspend fun getUser(): User? {

        val userInfo = supabaseClient.auth.currentUserOrNull() ?: return null

        return try {
            supabaseClient.from("profiles").select(
                columns = Columns.raw("*")
            ) {
                filter {
                    eq("id", userInfo.id)
                }
            }.decodeSingleOrNull<ProfileDto>()?.toDomain()

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}