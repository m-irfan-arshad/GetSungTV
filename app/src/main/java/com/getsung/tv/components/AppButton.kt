package com.getsung.tv.components

import android.R.attr.onClick
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.MaterialTheme
import com.getsung.tv.ui.theme.AppTheme
import com.getsung.tv.ui.theme.DefaultAppShapes

private val DEFAULT_BUTTON_LARGE_WIDTH = 200.dp
private val DEFAULT_BUTTON_LARGE_HEIGHT = 52.dp
private val DEFAULT_BUTTON_MEDIUM_WIDTH = 150.dp
private val DEFAULT_BUTTON_MEDIUM_HEIGHT = 40.dp
private val DEFAULT_BUTTON_SMALL_WIDTH = 100.dp
private val DEFAULT_BUTTON_SMALL_HEIGHT = 35.dp

enum class AppButtonSize {
    SMALL,
    MEDIUM,
    LARGE
}

enum class CommonButtonStyleTypeEnum {
    NORMAL,
    INVERSE,
    TRANSPARENT
}

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    type: AppButtonSize = AppButtonSize.MEDIUM,
    style: CommonButtonStyleTypeEnum = CommonButtonStyleTypeEnum.NORMAL,
    enableBorder: Boolean = true,
    buttonShape: Shape = DefaultAppShapes.circle,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    with(AppTheme.colors) {
        val (width, height, textStyle) = when (type) {
            AppButtonSize.SMALL -> Triple(
                DEFAULT_BUTTON_SMALL_WIDTH,
                DEFAULT_BUTTON_SMALL_HEIGHT,
                AppTheme.typography.labelSmall
            )

            AppButtonSize.MEDIUM -> Triple(
                DEFAULT_BUTTON_MEDIUM_WIDTH,
                DEFAULT_BUTTON_MEDIUM_HEIGHT,
                AppTheme.typography.labelMedium
            )

            AppButtonSize.LARGE -> Triple(
                DEFAULT_BUTTON_LARGE_WIDTH,
                DEFAULT_BUTTON_LARGE_HEIGHT,
                AppTheme.typography.labelLarge
            )
        }
        Button(
            onClick = onClick,
            modifier = modifier
                .then(
                    Modifier.size(width, height)
                )
                .clip(buttonShape),
            enabled = enabled,
            shape = ButtonDefaults.shape(shape = buttonShape),
            colors = ButtonDefaults.colors(
                containerColor = when (style) {
                    CommonButtonStyleTypeEnum.NORMAL -> if (enableBorder) {
                        primaryContainer.copy(alpha = 0.8f)
                    } else {
                        primaryContainer
                    }

                    CommonButtonStyleTypeEnum.INVERSE -> if (enableBorder) {
                        secondaryContainer.copy(alpha = 0.8f)
                    } else {
                        secondaryContainer
                    }

                    CommonButtonStyleTypeEnum.TRANSPARENT -> Color.Transparent
                },
                contentColor = when (style) {
                    CommonButtonStyleTypeEnum.NORMAL -> onPrimaryContainer
                    CommonButtonStyleTypeEnum.INVERSE -> onSecondaryContainer
                    CommonButtonStyleTypeEnum.TRANSPARENT -> surface
                },
                focusedContainerColor = when (style) {
                    CommonButtonStyleTypeEnum.NORMAL -> tertiary.copy(alpha = 0.8f)
                    CommonButtonStyleTypeEnum.INVERSE -> tertiary.copy(alpha = 0.8f)
                    CommonButtonStyleTypeEnum.TRANSPARENT -> tertiary.copy(alpha = 0.8f)
                }
            ),
            border = ButtonDefaults.border()
        ) {
            if (loading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    AppCircularProgress(
                        color = AppTheme.colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    leadingIcon?.invoke()
                    content()
                    trailingIcon?.invoke()
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppButton(
                style = CommonButtonStyleTypeEnum.NORMAL,
                onClick = {}
            ) {
                AppText(
                    modifier = Modifier.fillMaxWidth(),
                    type = CommonTextTypeEnum.BODY_MEDIUM,
                    titleText = "Button Text",
                    textAlign = TextAlign.Center
                )
            }
            AppButton(
                style = CommonButtonStyleTypeEnum.INVERSE,
                onClick = {}
            ) {
                AppText(
                    type = CommonTextTypeEnum.BODY_MEDIUM,
                    titleText = "Button Text",
                    textAlign = TextAlign.Center
                )
            }
            AppButton(
                style = CommonButtonStyleTypeEnum.TRANSPARENT,
                onClick = {},
            ) {
                AppText(
                    type = CommonTextTypeEnum.BODY_MEDIUM,
                    titleText = "Button Text",
                    textAlign = TextAlign.Center
                )
            }
            AppButton(
                modifier = Modifier.padding(3.dp),
                type = AppButtonSize.LARGE,
                loading = false,
                onClick = {}
            ) {
                AppText(
                    modifier = Modifier.fillMaxWidth(),
                    type = CommonTextTypeEnum.BODY_MEDIUM,
                    titleText = "Button Text",
                    textAlign = TextAlign.Center
                )


            }
        }
    }
}
