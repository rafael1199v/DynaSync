package com.example.dynasync.data.repository

import android.util.Log
import com.example.dynasync.data.supabase.SupabaseClientObject
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

sealed class AuthResult {
    data object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

object AuthRepository {
    private val authClient = SupabaseClientObject.client.auth

    suspend fun signIn(email: String, password: String): AuthResult {
        println("Auth: ${email}")
        println("Auth: ${password}")
        return try {
            authClient.signInWith(Email) {
                this.email = email
                this.password = password
            }
            AuthResult.Success
        } catch (e: Exception) {
            Log.d("login", "Error en el inicio de sesión: ${e.message}")

            var errorMessage = e.message ?: ""

            if(errorMessage.contains("invalid_credentials")) {
                errorMessage = "Credenciales inválidas."
            }
            else {
                errorMessage = "Error desconocido en el inicio de sesión. Verifica tu conexión a internet"
            }

            AuthResult.Error(errorMessage)
        }
    }

    suspend fun signUp(email: String, password: String): AuthResult {
        return try {
            authClient.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            AuthResult.Success
        } catch (e: Exception) {
            println("Error en el registro: ${e.message}")
            AuthResult.Error(e.message ?: "Error desconocido durante el registro")
        }
    }


    fun getUserId() : String? {
        return authClient.currentUserOrNull()?.id
    }

    suspend fun signOut() {
        try {
            authClient.signOut()
        } catch (e: Exception) {
            println("Error al cerrar sesión: ${e.message}")
        }
    }
}