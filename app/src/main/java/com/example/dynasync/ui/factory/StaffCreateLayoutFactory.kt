package com.example.dynasync.ui.factory

import com.example.dynasync.R

class StaffCreateLayoutFactory (
    private val onFloatActionButtonClick: (() -> Unit)?,
    private val onNavigationIconClick: (() -> Unit)?,
    private val listActions: List<ActionItem>
): LayoutFactory {
    override fun create(): LayoutConfiguration {
        return LayoutConfiguration(
            isVisible = true,

            navigationIconId = R.drawable.baseline_arrow_back_24,
            onNavigationIconClick = onNavigationIconClick,
            title = "Registrar nuevo miembro",

            actionsList = listActions,

            floatingActionButtonText = "Pago",
            onFloatActionButtonClick = onFloatActionButtonClick,
            floatingActionButtonIconId = R.drawable.baseline_add_24
        )
    }
}