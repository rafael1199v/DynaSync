package com.example.dynasync.ui.factory

import com.example.dynasync.R

class HomeLayoutFactory(
    private val onFloatActionButtonClick: (() -> Unit),
    private val onNavigationIconClick: (() -> Unit),
    private val listActions: List<ActionItem>
): LayoutFactory {
    override fun create(): LayoutConfiguration {
        return LayoutConfiguration(
            isVisible = true,

            navigationIconId = R.drawable.outline_logout_24,
            onNavigationIconClick = onNavigationIconClick,
            title = "Home",

            actionsList = listActions,

            floatingActionButtonText = "Proyectos",
            onFloatActionButtonClick = onFloatActionButtonClick,
            floatingActionButtonIconId = R.drawable.baseline_add_24
        )
    }
}