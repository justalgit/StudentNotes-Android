package com.example.studentnotes.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.studentnotes.ui.theme.LightGreen
import com.example.studentnotes.ui.theme.LightRed

@Composable
fun UiChoiceButton(
    modifier: Modifier = Modifier,
    isAcceptButton: Boolean,
    onClick: () -> Unit = {}
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White
        ),
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = if (isAcceptButton) "Принять" else "Отклонить",
            color = if (isAcceptButton) LightGreen else LightRed
        )
    }
}