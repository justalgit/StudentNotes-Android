package com.example.studentnotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun UiProgressDialog() {
    Dialog(
        onDismissRequest = {},
        DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
       Column(
           modifier = Modifier
               .wrapContentSize()
               .background(Color.Transparent)
               .padding(24.dp)
       ) {
           CircularProgressIndicator()
       }
    }
}