package com.example.studentnotes.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.studentnotes.ui.theme.Orange200

@Composable
fun UiTextArea(
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Orange200,
            focusedIndicatorColor = Orange200,
            backgroundColor = Color.White
        ),
        label = { Text(label) },
        singleLine = false,
        modifier = Modifier
            .fillMaxWidth()
    )
}