package com.example.studentnotes.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.studentnotes.ui.theme.Blue500
import com.example.studentnotes.ui.theme.Shapes
import com.example.studentnotes.ui.theme.Typography

@Composable
fun UiBigButton(
    modifier: Modifier = Modifier,
    color: Color = Blue500,
    text: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        shape = Shapes.medium,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color,
            contentColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        onClick = onClick,
        enabled = isEnabled
    ) {
        Text(
            text = text.uppercase(),
            fontWeight = FontWeight.SemiBold,
            style = Typography.caption
        )
    }
}