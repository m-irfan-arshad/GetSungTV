package com.getsung.tv.ui.theme


import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppSpacing(
    val none: Dp = 0.dp,
    val xxs: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp,
    val xxxl: Dp = 64.dp
)

val DefaultAppSpacing = AppSpacing()

@Immutable
data class AppElevation(
    val none: Dp = 0.dp,
    val xs: Dp = 1.dp,    // Subtle separation (e.g., divider shadow)
    val sm: Dp = 2.dp,    // Resting cards
    val md: Dp = 4.dp,    // Pressed cards, dropdowns
    val lg: Dp = 8.dp,    // Modals, navigation drawer
    val xl: Dp = 16.dp,   // Dialogs
    val xxl: Dp = 24.dp   // Highest emphasis
)

val DefaultAppElevation = AppElevation()

@Immutable
data class AppRadius(
    val flat: Dp = 0.dp,
    val small: Dp = 2.dp,
    val medium: Dp = 4.dp,
    val large: Dp = 8.dp,
    val xlarge: Dp = 12.dp
)

val DefaultAppRadious = AppRadius()