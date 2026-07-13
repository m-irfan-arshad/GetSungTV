package com.getsung.tv.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class AppShapes(
    val none: Shape,
    val small: Shape,
    val medium: Shape,
    val large: Shape,
    val xlarge: Shape,
    val pill: Shape,
    val circle: Shape
)

val DefaultAppShapes = AppShapes(
    none = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp),
    xlarge = RoundedCornerShape(16.dp),
    pill = RoundedCornerShape(percent = 50),  // Fully rounded ends (pill shape)
    circle = CircleShape
)

val shape = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)