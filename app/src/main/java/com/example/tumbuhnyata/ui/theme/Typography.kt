package com.example.tumbuhnyata.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.tumbuhnyata.R

val PoppinsFontFamily = FontFamily(
    Font(R.font.poppins_thin, FontWeight.Thin),
    Font(R.font.poppins_thinitalic, FontWeight.Thin, FontStyle.Italic),

    Font(R.font.poppins_extralight, FontWeight.ExtraLight),
    Font(R.font.poppins_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),

    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_lightitalic, FontWeight.Light, FontStyle.Italic),

    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),

    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_mediumitalic, FontWeight.Medium, FontStyle.Italic),

    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),

    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_bolditalic, FontWeight.Bold, FontStyle.Italic),

    Font(R.font.poppins_extrabold, FontWeight.ExtraBold),
    Font(R.font.poppins_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),

    Font(R.font.poppins_black, FontWeight.Black),
    Font(R.font.poppins_blackitalic, FontWeight.Black, FontStyle.Italic)
)

val TumbuhNyataTypography = Typography(
    bodyLarge = TextStyle(
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp

    ),
    bodyMedium = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
)