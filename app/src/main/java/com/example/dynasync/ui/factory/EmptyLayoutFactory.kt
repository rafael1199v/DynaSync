package com.example.dynasync.ui.factory

class EmptyLayoutFactory: LayoutFactory {
    override fun create(): LayoutConfiguration {
        return LayoutConfiguration(
            isVisible = false,
            title = null,
            navigationIconId = null
        )
    }
}