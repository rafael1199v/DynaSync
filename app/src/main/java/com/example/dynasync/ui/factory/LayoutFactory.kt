package com.example.dynasync.ui.factory

sealed interface LayoutFactory {
    fun create() : LayoutConfiguration
}