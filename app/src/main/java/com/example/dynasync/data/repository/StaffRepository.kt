package com.example.dynasync.data.repository

import android.util.Log
import com.example.dynasync.data.dto.PaymentDto
import com.example.dynasync.data.dto.PersonalDto
import com.example.dynasync.data.mapper.toDomain
import com.example.dynasync.data.mapper.toDto
import com.example.dynasync.data.repository.PaymentRepository.supabase
import com.example.dynasync.domain.model.Personal
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Instant

object StaffRepository {
    val staff = mutableListOf(
        Personal(
            id = 1,
            name = "Carlos",
            lastname = "Salazar",
            charge = "Marketing",
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Personal(
            id = 2,
            name = "Rafael",
            lastname = "Vargas",
            charge = "Developer",
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Personal(
            id = 3,
            name = "Bill",
            lastname = "Gates",
            charge = "Developer",
            imageUrl = "https://imgs.search.brave.com/Hd7SJ3X5pzzzZAymcfMDi-U2IYy9mfyVQHqaZSYwTis/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJjYXQuY29t/L3cvZnVsbC8xL2Mv/MS8yMzk2OC0xOTQw/eDEwOTEtZGVza3Rv/cC1oZC1iaWxsLWdh/dGVzLWJhY2tncm91/bmQtcGhvdG8uanBn",
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Personal(
            id = 4,
            name = "Linus",
            lastname = "Torvals",
            charge = "Software Architect",
            imageUrl = "https://imgs.search.brave.com/kLAi2FJoe1m0aSPk7FWSxVBUt4OWGGr0552zBv2_430/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9jZG4u/bW9zLmNtcy5mdXR1/cmVjZG4ubmV0L3NL/ZnJlTDV3YlgySmpR/U0pnRmpjdlMuanBn",
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Personal(
            id = 5,
            name = "Bill",
            lastname = "Gates",
            charge = "Developer",
            imageUrl = "https://imgs.search.brave.com/Hd7SJ3X5pzzzZAymcfMDi-U2IYy9mfyVQHqaZSYwTis/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJjYXQuY29t/L3cvZnVsbC8xL2Mv/MS8yMzk2OC0xOTQw/eDEwOTEtZGVza3Rv/cC1oZC1iaWxsLWdh/dGVzLWJhY2tncm91/bmQtcGhvdG8uanBn",
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Personal(
            id = 6,
            name = "Linus",
            lastname = "Torvals",
            charge = "Software Architect",
            imageUrl = "https://imgs.search.brave.com/kLAi2FJoe1m0aSPk7FWSxVBUt4OWGGr0552zBv2_430/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9jZG4u/bW9zLmNtcy5mdXR1/cmVjZG4ubmV0L3NL/ZnJlTDV3YlgySmpR/U0pnRmpjdlMuanBn",
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Personal(
            id = 7,
            name = "Bill",
            lastname = "Gates",
            charge = "Developer",
            imageUrl = "https://imgs.search.brave.com/Hd7SJ3X5pzzzZAymcfMDi-U2IYy9mfyVQHqaZSYwTis/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJjYXQuY29t/L3cvZnVsbC8xL2Mv/MS8yMzk2OC0xOTQw/eDEwOTEtZGVza3Rv/cC1oZC1iaWxsLWdh/dGVzLWJhY2tncm91/bmQtcGhvdG8uanBn",
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Personal(
            id = 8,
            name = "Linus",
            lastname = "Torvals",
            charge = "Software Architect",
            imageUrl = "https://imgs.search.brave.com/kLAi2FJoe1m0aSPk7FWSxVBUt4OWGGr0552zBv2_430/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9jZG4u/bW9zLmNtcy5mdXR1/cmVjZG4ubmV0L3NL/ZnJlTDV3YlgySmpR/U0pnRmpjdlMuanBn",
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        )
    )

    suspend fun getStaff(userId: String): List<Personal> {
        try {
            val staffDto = supabase.from("personal").select(
                columns = Columns.raw("""
                    *
                """.trimIndent())
            ) {
                filter {
                    eq("profile_id", userId)
                }
            }.decodeList<PersonalDto>()

            Log.d("debug", "Staff: ${staffDto}")
            return staffDto.map { it.toDomain() }
        }
        catch (e: Exception) {
            Log.e("main", "Hubo un error al cargar al personal. ${e}")
            return emptyList()
        }
    }

    suspend fun getStaffById(staffId: Int) : Personal {
        delay(2000)
        return staff.find { it.id == staffId }!!
    }

    suspend fun updateStaff(staff: Personal) {
        delay(2000)

        val index = this.staff.indexOfFirst { it.id == staff.id }
        this.staff[index] = staff
    }

    suspend fun addStaff(newStaff: Personal, newImageBytes: ByteArray? = null, userId: String) {
        var finalImageUrl: String? = null

        try {
            if (newImageBytes != null) {
                finalImageUrl = StorageHelper.uploadImage(
                    bucketName = "staff-images",
                    byteArray = newImageBytes,
                    fileNamePrefix = "staff-${userId}"
                )
            }

            val staff = newStaff.copy(
                imageUrl = finalImageUrl,
                createdAt = Clock.System.now()
            )

            supabase.from("personal").insert(staff.toDto())
        }
        catch (e: Exception) {
            Log.e("debug", "Error al registrar al empleado: $e")
        }



    }

    suspend fun deleteStaff(staffId: Int) {
        delay(2000)
        staff.removeIf { it.id == staffId }
    }
}