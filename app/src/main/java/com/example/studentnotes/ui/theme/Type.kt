package com.example.studentnotes.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.studentnotes.R

val interFont = FontFamily(Font(R.font.inter))

val Typography = Typography(
    h4 = TextStyle(
        fontFamily = interFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp
    ),
    h5 = TextStyle(
        fontFamily = interFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = interFont,
        fontWeight = FontWeight.Normal,
        fontSize = 21.sp
    ),
    body1 = TextStyle(
        fontFamily = interFont,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    body2 = TextStyle(
        fontFamily = interFont,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    caption = TextStyle(
        fontFamily = interFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)