package com.example.dynasync.ui.factory

data class LayoutConfiguration(
    val isVisible: Boolean,
    val title: String?,
    val navigationIconId: Int?,

    val actionsList: List<ActionItem> = emptyList(),

    val onFloatActionButtonClick: (() -> Unit)? = null,
    val onNavigationIconClick: (() -> Unit)? = null,
    val floatingActionButtonText: String? = null,
    val floatingActionButtonIconId: Int? = null,
)

data class ActionItem(
    val iconId: Int,
    val contentDescription: String? = null,
    val onClick: () -> Unit
)