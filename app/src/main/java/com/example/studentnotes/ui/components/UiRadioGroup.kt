package com.example.studentnotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studentnotes.ui.theme.LightGreen
import com.example.studentnotes.ui.theme.LightRed
import com.example.studentnotes.ui.theme.Typography

@Composable
fun UiRadioGroup(
    modifier: Modifier = Modifier
) {
    val options = listOf("Открытая", "Приватная")
    var selectedOption by remember {
        mutableStateOf("Открытая")
    }
    val onSelectionChange = { text: String ->
        selectedOption = text
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        options.forEach { text ->
            Box(
                modifier = Modifier
                    .padding(
                        all = 12.dp,
                    ),
            ) {
                Text(
                    text = text,
                    style = Typography.body1,
                    color = Color.Black,
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(
                                size = 12.dp,
                            ),
                        )
                        .clickable {
                            onSelectionChange(text)
                        }
                        .background(
                            if (text == selectedOption) {
                                if (selectedOption == "Открытая") LightGreen else LightRed
                            } else {
                                Color.LightGray
                            }
                        )
                        .padding(
                            vertical = 12.dp,
                            horizontal = 16.dp,
                        )
                )
            }
        }
    }
}
