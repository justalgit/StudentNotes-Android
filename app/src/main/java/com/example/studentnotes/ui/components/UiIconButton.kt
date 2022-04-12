package com.example.studentnotes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.studentnotes.ui.theme.Blue500

@Composable
fun UiIconButton(
    painter: Painter,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .background(Blue500, CircleShape)
            .clip(CircleShape)
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onClick = onClick
            )
    ) {
        Image(
            painter = painter,
            contentDescription = null
        )
    }
}