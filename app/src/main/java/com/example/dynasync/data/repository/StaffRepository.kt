package com.example.dynasync.data.repository

import com.example.dynasync.domain.model.Personal
import kotlinx.coroutines.delay
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

    suspend fun getStaff(): List<Personal> {
        delay(2000)
        return staff
    }

    suspend fun getStaffById(staffId: Int) : Personal {
        delay(2000)
        return staff.find { it.id == staffId }!!
    }

    suspend fun addStaff(newStaff: Personal) {
        delay(2000)
        val maxId = staff.maxByOrNull { it.id }?.id ?: 0
        val newStaffCopy = newStaff.copy(id = maxId + 1)
        this.staff.add(newStaffCopy)
    }
}