package com.example.dynasync.data.supabase

import com.example.dynasync.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClientObject {

    init {
        println("Auth: ${BuildConfig.SUPABASE_KEY}")
        println("Auth: ${BuildConfig.SUPABASE_URL}")
    }
    val client = createSupabaseClient(
        supabaseKey = BuildConfig.SUPABASE_KEY,
        supabaseUrl = BuildConfig.SUPABASE_URL
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}