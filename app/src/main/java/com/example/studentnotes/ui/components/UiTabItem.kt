package com.example.studentnotes.ui.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentnotes.R
import com.example.studentnotes.ui.theme.Blue500
import com.example.studentnotes.ui.theme.Typography

@Composable
fun UiTabItem(
    modifier: Modifier = Modifier,
    textLabel: String,
    iconPainter: Painter,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val contentColor by animateColorAsState(
        if (isSelected) Blue500 else Color.Gray
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onClick = onClick
            )
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = null,
            tint = contentColor
        )
        Text(
            text = textLabel,
            color = contentColor,
            style = Typography.caption
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TabItemPreview() {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        UiTabItem(
            textLabel = stringResource(R.string.events),
            iconPainter = painterResource(R.drawable.ic_events_24)
        ) {}
        UiTabItem(
            textLabel = stringResource(R.string.events),
            iconPainter = painterResource(R.drawable.ic_events_24),
            isSelected = true
        ) {}
    }
}