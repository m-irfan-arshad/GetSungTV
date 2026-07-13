package com.getsung.tv.ui.theme

import androidx.compose.runtime.compositionLocalOf

// staticCompositionLocalOf - for values that change rarely
// (changing them invalidates ALL readers - but we want that for theme switching)
val LocalAppColors = compositionLocalOf<AppColor> {
    error("AppColors not provided. Wrap your content in AppTheme.")
}
val LocalAppTypography = compositionLocalOf<AppTypography> {
    error("AppTypography not provided. Wrap your content in AppTheme.")
}
val LocalAppSpacing = compositionLocalOf<AppSpacing> {
    error("AppSpacing not provided. Wrap your content in AppTheme.")
}
val LocalAppShapes = compositionLocalOf<AppShapes> {
    error("AppShapes not provided. Wrap your content in AppTheme.")
}
val LocalAppElevation = compositionLocalOf<AppElevation> {
    error("AppElevation not provided. Wrap your content in AppTheme.")
}

// LocalContentColor - the "default text color" for the current context
// Changes when nested in containers (e.g., text on primary button is white,
// text on surface is black)
//val LocalAppContentColor = compositionLocalOf<Color> {
//    PrimitiveColors.Gray900
//}