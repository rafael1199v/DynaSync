package com.example.dynasync.data.repository

import android.util.Log
import com.example.dynasync.data.dto.PaymentDto
import com.example.dynasync.data.dto.PersonalDto
import com.example.dynasync.data.mapper.toDomain
import com.example.dynasync.data.mapper.toDto
import com.example.dynasync.data.repository.PaymentRepository.supabase
import com.example.dynasync.data.repository.ProjectRepository.getProjectById
import com.example.dynasync.data.supabase.SupabaseClientObject
import com.example.dynasync.domain.model.Personal
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Instant

object StaffRepository {

    val supabase = SupabaseClientObject.client

//    val staff = mutableListOf(
//        Personal(
//            id = 1,
//            name = "Carlos",
//            lastname = "Salazar",
//            charge = "Marketing",
//            createdAt = Instant.parse("2023-11-01T12:00:00Z")
//        ),
//        Personal(
//            id = 2,
//            name = "Rafael",
//            lastname = "Vargas",
//            charge = "Developer",
//            createdAt = Instant.parse("2023-11-01T12:00:00Z")
//        ),
//        Personal(
//            id = 3,
//            name = "Bill",
//            lastname = "Gates",
//            charge = "Developer",
//            imageUrl = "https://imgs.search.brave.com/Hd7SJ3X5pzzzZAymcfMDi-U2IYy9mfyVQHqaZSYwTis/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJjYXQuY29t/L3cvZnVsbC8xL2Mv/MS8yMzk2OC0xOTQw/eDEwOTEtZGVza3Rv/cC1oZC1iaWxsLWdh/dGVzLWJhY2tncm91/bmQtcGhvdG8uanBn",
//            createdAt = Instant.parse("2023-11-01T12:00:00Z")
//        ),
//        Personal(
//            id = 4,
//            name = "Linus",
//            lastname = "Torvals",
//            charge = "Software Architect",
//            imageUrl = "https://imgs.search.brave.com/kLAi2FJoe1m0aSPk7FWSxVBUt4OWGGr0552zBv2_430/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9jZG4u/bW9zLmNtcy5mdXR1/cmVjZG4ubmV0L3NL/ZnJlTDV3YlgySmpR/U0pnRmpjdlMuanBn",
//            createdAt = Instant.parse("2023-11-01T12:00:00Z")
//        ),
//        Personal(
//            id = 5,
//            name = "Bill",
//            lastname = "Gates",
//            charge = "Developer",
//            imageUrl = "https://imgs.search.brave.com/Hd7SJ3X5pzzzZAymcfMDi-U2IYy9mfyVQHqaZSYwTis/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJjYXQuY29t/L3cvZnVsbC8xL2Mv/MS8yMzk2OC0xOTQw/eDEwOTEtZGVza3Rv/cC1oZC1iaWxsLWdh/dGVzLWJhY2tncm91/bmQtcGhvdG8uanBn",
//            createdAt = Instant.parse("2023-11-01T12:00:00Z")
//        ),
//        Personal(
//            id = 6,
//            name = "Linus",
//            lastname = "Torvals",
//            charge = "Software Architect",
//            imageUrl = "https://imgs.search.brave.com/kLAi2FJoe1m0aSPk7FWSxVBUt4OWGGr0552zBv2_430/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9jZG4u/bW9zLmNtcy5mdXR1/cmVjZG4ubmV0L3NL/ZnJlTDV3YlgySmpR/U0pnRmpjdlMuanBn",
//            createdAt = Instant.parse("2023-11-01T12:00:00Z")
//        ),
//        Personal(
//            id = 7,
//            name = "Bill",
//            lastname = "Gates",
//            charge = "Developer",
//            imageUrl = "https://imgs.search.brave.com/Hd7SJ3X5pzzzZAymcfMDi-U2IYy9mfyVQHqaZSYwTis/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJjYXQuY29t/L3cvZnVsbC8xL2Mv/MS8yMzk2OC0xOTQw/eDEwOTEtZGVza3Rv/cC1oZC1iaWxsLWdh/dGVzLWJhY2tncm91/bmQtcGhvdG8uanBn",
//            createdAt = Instant.parse("2023-11-01T12:00:00Z")
//        ),
//        Personal(
//            id = 8,
//            name = "Linus",
//            lastname = "Torvals",
//            charge = "Software Architect",
//            imageUrl = "https://imgs.search.brave.com/kLAi2FJoe1m0aSPk7FWSxVBUt4OWGGr0552zBv2_430/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9jZG4u/bW9zLmNtcy5mdXR1/cmVjZG4ubmV0L3NL/ZnJlTDV3YlgySmpR/U0pnRmpjdlMuanBn",
//            createdAt = Instant.parse("2023-11-01T12:00:00Z")
//        )
//    )

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

    suspend fun getStaffById(staffId: Int) : Personal? {
        try {
            val staffDto = supabase.from("personal").select(
                columns = Columns.raw("""
                    *
                """.trimIndent()))
            {
                filter {
                    eq("id", staffId)
                }
            }.decodeSingle<PersonalDto>()


            Log.d("debug", "Staff: ${staffDto}")
            return staffDto.toDomain()
        }
        catch (e: Exception) {
            Log.e("debug", "Hubo un error al cargar al personal. ${e}")
            return null
        }
    }

    suspend fun updateStaff(staff: Personal, newImageBytes: ByteArray? = null) {
        val oldStaffData = getStaffById(staff.id)
        val oldImageUrl = oldStaffData?.imageUrl

        var finalImageUrl = staff.imageUrl

        try {
            if (!oldImageUrl.isNullOrEmpty() && oldImageUrl.contains("supabase")) {

                if (newImageBytes != null || finalImageUrl.isNullOrEmpty()) {
                    try {
                        StorageHelper.deleteImage("staff-images", oldImageUrl)
                    } catch (e: Exception) {
                        Log.e("debug", "No se pudo borrar la imagen antigua: $e")
                    }
                }
            }

            if (newImageBytes != null) {
                finalImageUrl = StorageHelper.uploadImage("staff-images", newImageBytes, "staff-upd-${staff.id}")
            }

            val updatedStaff = staff.copy(
                imageUrl = finalImageUrl
            )

            Log.d("debug", "${updatedStaff}")
            supabase.from("personal").update(updatedStaff.toDto()) {
                filter { eq("id", staff.id) }
            }
        }
        catch (e: Exception) {
            Log.d("debug", "Error al actualizar al empleado: $e")
        }


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
        val staffToDelete = getStaffById(staffId)
        val imageUrl = staffToDelete?.imageUrl

        if (!imageUrl.isNullOrEmpty() && imageUrl.contains("supabase")) {
            try {
                StorageHelper.deleteImage("staff-images", imageUrl)
            } catch (e: Exception) {
                Log.e("debug", "Error borrando imagen al eliminar al personal: $e")
            }
        }

        try {
            supabase.from("personal").delete {
                filter {
                    eq("id", staffId)
                }
            }
        } catch (e: Exception) {
            Log.e("ProjectRepo", "Error eliminando al personal de la BD: $e")
        }

    }
}