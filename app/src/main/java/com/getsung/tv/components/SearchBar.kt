package com.getsung.tv.components

import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchBar(
    modifier: Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        })
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    name = "Search Bar",
    uiMode = AndroidUiModes.UI_MODE_TYPE_MASK
)
@Composable
fun SearchBarPreview() {
    SearchBar(modifier = Modifier)
}