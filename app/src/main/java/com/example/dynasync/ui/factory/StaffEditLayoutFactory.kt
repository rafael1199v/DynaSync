package com.example.dynasync.ui.factory

import com.example.dynasync.R

class StaffEditLayoutFactory (
    private val onFloatActionButtonClick: (() -> Unit)?,
    private val onNavigationIconClick: (() -> Unit)?,
    private val listActions: List<ActionItem>
): LayoutFactory {
    override fun create(): LayoutConfiguration {
        return LayoutConfiguration(
            isVisible = true,

            navigationIconId = R.drawable.baseline_arrow_back_24,
            onNavigationIconClick = onNavigationIconClick,
            title = "Actualizar informacion",

            actionsList = listActions,

            floatingActionButtonText = "Personal",
            onFloatActionButtonClick = onFloatActionButtonClick,
            floatingActionButtonIconId = R.drawable.baseline_add_24
        )
    }
}