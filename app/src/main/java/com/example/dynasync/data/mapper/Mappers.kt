package com.example.dynasync.data.mapper

import com.example.dynasync.data.dto.PaymentDto
import com.example.dynasync.data.dto.PaymentTypeDto
import com.example.dynasync.data.dto.PersonalDto
import com.example.dynasync.data.dto.ProfileDto
import com.example.dynasync.data.dto.ProjectDto
import com.example.dynasync.data.dto.TaskDto
import com.example.dynasync.domain.model.Payment
import com.example.dynasync.domain.model.PaymentType
import com.example.dynasync.domain.model.Personal
import com.example.dynasync.domain.model.Project
import com.example.dynasync.domain.model.Task
import com.example.dynasync.domain.model.User
import kotlin.time.Instant

private fun String?.toInstantOrNull(): Instant? {
    return if (this.isNullOrBlank()) null else try {
        Instant.parse(this)
    } catch (e: Exception) {
        null
    }
}

private fun Instant?.toDtoString(): String? {
    return this?.toString()
}


fun PersonalDto.toDomain(): Personal {
    return Personal(
        id = this.id ?: 0,
        name = this.name,
        lastname = this.lastname,
        charge = this.charge,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt.toInstantOrNull()
    )
}

fun Personal.toDto(): PersonalDto {
    return PersonalDto(
        id = if (this.id == 0) null else this.id,
        name = this.name,
        lastname = this.lastname,
        charge = this.charge,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt.toDtoString()
    )
}


fun ProjectDto.toDomain(tasks: List<Task> = emptyList()): Project {
    return Project(
        id = this.id ?: 0,
        title = this.title,
        objective = this.objective,
        description = this.description,
        finishDate = this.finishDate!!,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt.toInstantOrNull(),
        tasks = tasks
    )
}

fun Project.toDto(): ProjectDto {
    return ProjectDto(
        id = if (this.id == 0) null else this.id,
        title = this.title,
        objective = this.objective,
        description = this.description,
        finishDate = this.finishDate,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt.toDtoString()
    )
}


fun TaskDto.toDomain(personal: Personal? = null): Task {
    return Task(
        id = this.id ?: 0,
        title = this.title,
        isCompleted = this.isCompleted,
        finishDate = this.finishDate,
        personal = personal,
        createdAt = this.createdAt.toInstantOrNull()
    )
}

fun Task.toDto(projectId: Int): TaskDto {
    return TaskDto(
        id = if (this.id == 0) null else this.id,
        title = this.title,
        isCompleted = this.isCompleted,
        finishDate = this.finishDate,
        projectId = projectId, // Obligatorio para Supabase
        personalId = this.personal?.id, // Extraemos el ID del objeto personal
        createdAt = this.createdAt.toDtoString()
    )
}


fun ProfileDto.toDomain(): User {
    return User(
        id = this.id, // String (UUID)
        name = this.name,
        lastName = this.lastName,
        age = this.age,
        profileImageUrl = this.profileImageUrl,
        createdAt = this.createdAt.toInstantOrNull()
    )
}

fun User.toDto(): ProfileDto {
    return ProfileDto(
        id = this.id,
        name = this.name,
        lastName = this.lastName,
        age = this.age,
        profileImageUrl = this.profileImageUrl,
        createdAt = this.createdAt.toDtoString()
    )
}


fun PaymentDto.toDomain(): Payment {
    return Payment(
        id = this.id ?: 0,
        beneficiary = this.beneficiary,
        amount = this.amount,
        dateTime = this.dateTime,
        paymentType = this.paymentType.toDomain(),
        createdAt = this.createdAt.toInstantOrNull()
    )
}

fun Payment.toDto(): PaymentDto {
    return PaymentDto(
        id = if (this.id == 0) null else this.id,
        beneficiary = this.beneficiary,
        amount = this.amount,
        dateTime = this.dateTime,
        paymentType = this.paymentType.toDto(),
        createdAt = this.createdAt.toDtoString()
    )
}

fun PaymentTypeDto.toDomain(): PaymentType {
    return when (this) {
        PaymentTypeDto.PERSONAL -> PaymentType.PERSONAL
        PaymentTypeDto.INVENTORY -> PaymentType.INVENTORY
        PaymentTypeDto.SAVINGS -> PaymentType.SAVINGS
        PaymentTypeDto.TECHNOLOGY -> PaymentType.TECHNOLOGY
    }
}

fun PaymentType.toDto(): PaymentTypeDto {
    return when (this) {
        PaymentType.PERSONAL -> PaymentTypeDto.PERSONAL
        PaymentType.INVENTORY -> PaymentTypeDto.INVENTORY
        PaymentType.SAVINGS -> PaymentTypeDto.SAVINGS
        PaymentType.TECHNOLOGY -> PaymentTypeDto.TECHNOLOGY
    }
}