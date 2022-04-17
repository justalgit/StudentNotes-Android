package com.example.studentnotes.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import com.example.studentnotes.ui.theme.Orange200

@ExperimentalComposeUiApi
@Composable
fun UiTextArea(
    value: String,
    label: String,
    onValueChange: (String) -> Unit = {}
) {

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
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
            .wrapContentHeight()
            .onPreviewKeyEvent {
                if (it.key == Key.Tab){
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            }
    )
}