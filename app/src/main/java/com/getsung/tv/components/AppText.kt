package com.getsung.tv.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.getsung.tv.R
import com.getsung.tv.ui.theme.AppTheme

private const val DEFAULT_MAX_LINES = Int.MAX_VALUE

enum class CommonTextTypeEnum {
    TITLE_LARGE,
    TITLE_MEDIUM,
    TITLE_SMALL,
    LABEL_LARGE,
    LABEL_MEDIUM,
    LABEL_SMALL,
    BODY_SMALL,
    BODY_MEDIUM,
    BODY_LARGE,
    HEADLINE_SMALL,
    HEADLINE_MEDIUM,
    HEADLINE_LARGE
}

@Composable
fun AppText(
    modifier: Modifier = Modifier,
    type: CommonTextTypeEnum,
    @StringRes titleRes: Int? = null,
    titleText: String? = null,
    singleLine: Boolean = false,
    textBold: Boolean = false,
    maxLines: Int = DEFAULT_MAX_LINES,
    textColor: Color? = null,
    textAlign: TextAlign? = null
) {
    with(AppTheme.colors) {
        AppTextComponent(
            modifier = modifier,
            singleLine = singleLine,
            text = titleRes?.let {
                stringResource(id = it)
            } ?: titleText ?: stringResource(id = R.string.no_text_value),
            maxLines = maxLines,
            textColor = textColor ?: onPrimary,
            textAlign = textAlign,
            textBold = textBold,
            textStyle = with(AppTheme.typography) {
                when (type) {
                    CommonTextTypeEnum.TITLE_LARGE -> titleLarge
                    CommonTextTypeEnum.TITLE_MEDIUM -> titleMedium
                    CommonTextTypeEnum.TITLE_SMALL -> titleSmall
                    CommonTextTypeEnum.LABEL_LARGE -> labelLarge
                    CommonTextTypeEnum.LABEL_MEDIUM -> labelMedium
                    CommonTextTypeEnum.LABEL_SMALL -> labelSmall
                    CommonTextTypeEnum.BODY_SMALL -> bodySmall
                    CommonTextTypeEnum.BODY_MEDIUM -> bodyMedium
                    CommonTextTypeEnum.BODY_LARGE -> bodyLarge
                    CommonTextTypeEnum.HEADLINE_SMALL -> headlineSmall
                    CommonTextTypeEnum.HEADLINE_MEDIUM -> headlineMedium
                    CommonTextTypeEnum.HEADLINE_LARGE -> headlineLarge
                }
            }
        )
    }
}

@Composable
private fun AppTextComponent(
    modifier: Modifier = Modifier,
    text: String,
    singleLine: Boolean,
    maxLines: Int,
    textColor: Color,
    textBold: Boolean,
    textAlign: TextAlign?,
    textStyle: TextStyle
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        color = textColor,
        style = textStyle,
        fontWeight = if (textBold) {
            FontWeight.Bold
        } else {
            null
        },
        maxLines = if (singleLine) {
            1
        } else {
            maxLines
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AppTextPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppText(
                type = CommonTextTypeEnum.HEADLINE_LARGE,
                titleText = "Headline Large",
                textColor = AppTheme.colors.textPrimary
            )
            AppText(
                type = CommonTextTypeEnum.TITLE_MEDIUM,
                titleText = "Title Medium",
                textColor = AppTheme.colors.textPrimary
            )
            AppText(
                type = CommonTextTypeEnum.BODY_MEDIUM,
                titleText = "Body Medium",
                textColor = AppTheme.colors.textPrimary
            )
            AppText(
                type = CommonTextTypeEnum.LABEL_SMALL,
                titleText = "Label Small",
                textColor = AppTheme.colors.textPrimary,
                textAlign = TextAlign.Center
            )
        }
    }

}
