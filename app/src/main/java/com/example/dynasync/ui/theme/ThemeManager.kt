package com.example.dynasync.ui.theme

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


enum class AppThemeMode {
    DARK, LIGHT, SYSTEM
}

object ThemeManager {
    private const val PREFS_NAME = "dynasycn_prefs"
    private const val KEY_THEME_MODE = "theme_mode"

    private val _themeMode = MutableStateFlow(AppThemeMode.SYSTEM)
    val themeMode = _themeMode.asStateFlow()

    private lateinit var prefs: SharedPreferences


    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val savedName = prefs.getString(KEY_THEME_MODE, AppThemeMode.SYSTEM.name)
        _themeMode.value = AppThemeMode.valueOf(savedName ?: AppThemeMode.SYSTEM.name)
    }

    fun saveTheme(mode: AppThemeMode) {
        Log.d("ThemeManager", "Guardando tema: $mode")
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
        _themeMode.value = mode
    }

}