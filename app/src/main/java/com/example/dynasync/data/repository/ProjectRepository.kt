package com.example.dynasync.data.repository

import android.util.Log
import com.example.dynasync.data.dto.ProjectDto
import com.example.dynasync.data.mapper.toDomain
import com.example.dynasync.data.mapper.toDto
import com.example.dynasync.data.supabase.SupabaseClientObject
import com.example.dynasync.domain.model.Personal
import com.example.dynasync.domain.model.Project
import com.example.dynasync.domain.model.Task
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.time.Instant

object ProjectRepository {

    val supabase = SupabaseClientObject.client
    val projects: MutableList<Project> = mutableListOf(
        Project(
            id = 1,
            title = "DynaSync",
            objective = "Mejorar la productividad de las personas",
            description = "Esta tiene que ser una descripcion muy larga para que las personas puedan ver hasta donde se puede extender..",
            finishDate = LocalDate(year = 2023, month = 12, day = 1),
            imageUrl = "https://images.unsplash.com/photo-1761839257864-c6ccab7238de?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            tasks = listOf(
                Task(
                    id = 1,
                    title = "Task 1",
                    //description = "Description of task 1",
                    isCompleted = true,
                    personal = Personal(
                        id = 1,
                        name = "Carlos",
                        lastname = "Salazar",
                        charge = "Developer"
                    ),
                    finishDate = LocalDate(year = 2023, month = 12, day = 1),
                    createdAt = Instant.parse("2023-11-01T12:00:00Z")
                ),
                Task(
                    id = 2,
                    title = "Task 2",
                    //description = "Description of task 2",
                    isCompleted = false,
                    personal = Personal(
                        id = 2,
                        name = "Rafael",
                        lastname = "Vargas",
                        charge = "Developer"
                    ),
                    finishDate = LocalDate(year = 2023, month = 12, day = 1),
                    createdAt = Instant.parse("2023-11-01T12:00:00Z")
                ),

            ),
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Project(
            id = 2,
            title = "DynaSync 2",
            objective = "Mejorar la productividad de las personas",
            description = "Esta tiene que ser una descripcion muy larga para que las personas puedan ver hasta donde se puede extender..",
            finishDate = LocalDate(year = 2023, month = 12, day = 1),
            imageUrl = "https://images.unsplash.com/photo-1761839256791-6a93f89fb8b0?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            tasks = emptyList(),
            createdAt = Instant.parse("2022-11-01T12:00:00Z")
        )
    )

    //val projects = emptyList<Project>()

    suspend fun getProjectById(projectId: Int): Project? {
        try {
            val projectDto = supabase.from("projects").select(
                columns = Columns.raw("""
                        *,
                        tasks (
                            *,
                            personal(*)
                        )
                """.trimIndent())
            ) {
                filter {
                    eq("id", projectId)
                }
            }.decodeSingle<ProjectDto>()

            return projectDto.toDomain()
        }
        catch (e: Exception) {
            Log.e("main", "Hubo un error al cargar el proyecto. ${e}")
            return null
        }
    }

    suspend fun getProjects(userId: String) : List<Project> {

        return try {
            val projectsDto = supabase.from("projects").select(
                columns = Columns.raw("""
                        *,
                        tasks (*)
                """.trimIndent())
            ) {
                filter {
                    eq("profile_id", userId)
                }
            }.decodeList<ProjectDto>()


            val domainProjects = projectsDto.map { it.toDomain() }

            return domainProjects
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

//    suspend fun addProject(newProject: Project) {
//        delay(2000)
//        val newId = (projects.maxByOrNull { it.id }?.id ?: 0) + 1
//        val creationTime = newProject.createdAt ?: Clock.System.now()
//        projects.add(newProject.copy(
//            id = newId,
//            createdAt = creationTime
//        ))
//    }

    suspend fun addProject(newProject: Project, imageBytes: ByteArray?, userId: String) {
        var finalImageUrl: String? = null

        if (imageBytes != null) {
            finalImageUrl = StorageHelper.uploadImage(
                bucketName = "projects-images",
                byteArray = imageBytes,
                fileNamePrefix = "proj-$userId"
            )
        }

        val project = newProject.copy(
            imageUrl = finalImageUrl,
            createdAt = Clock.System.now()
        )

        supabase.from("projects").insert(project.toDto())
    }

    suspend fun deleteProject(projectId: Int) {
        val projectToDelete = getProjectById(projectId)
        val imageUrl = projectToDelete?.imageUrl

        if (!imageUrl.isNullOrEmpty() && imageUrl.contains("supabase")) {
            try {
                StorageHelper.deleteImage("projects-images", imageUrl)
            } catch (e: Exception) {
                Log.e("ProjectRepo", "Error borrando imagen al eliminar proyecto: $e")
            }
        }

        try {
            supabase.from("projects").delete {
                filter {
                    eq("id", projectId)
                }
            }
        } catch (e: Exception) {
            Log.e("ProjectRepo", "Error eliminando el proyecto de la BD: $e")
            throw e
        }
    }


    suspend fun updateProject(project: Project, newImageBytes: ByteArray? = null) {
        val oldProjectData = getProjectById(project.id)
        val oldImageUrl = oldProjectData?.imageUrl

        var finalImageUrl = project.imageUrl

        if (!oldImageUrl.isNullOrEmpty() && oldImageUrl.contains("supabase")) {

            if (newImageBytes != null || finalImageUrl.isNullOrEmpty()) {
                try {
                    StorageHelper.deleteImage("projects-images", oldImageUrl)
                } catch (e: Exception) {
                    Log.e("ProjectRepo", "No se pudo borrar la imagen antigua: $e")
                }
            }
        }

        if (newImageBytes != null) {
            finalImageUrl = StorageHelper.uploadImage("projects-images", newImageBytes, "proj-upd")
        }

        val updatedProject = project.copy(
            imageUrl = finalImageUrl
        )

        supabase.from("projects").update(updatedProject.toDto()) {
            filter { eq("id", project.id) }
        }
    }
}