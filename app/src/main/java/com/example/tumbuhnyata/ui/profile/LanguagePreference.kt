package com.example.tumbuhnyata.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.viewmodel.ProfileViewModel

@Composable
fun LanguagePreference(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val profileState by viewModel.profileState.collectAsState()
    var selectedLanguage by rememberSaveable { mutableStateOf<String?>(null) }

    // Fallback ke "id" hanya jika belum ada nilai yang tersimpan
    if (selectedLanguage == null) {
        selectedLanguage = "id" // atau bisa dari sumber lain seperti DataStore nanti
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 94.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pilih Bahasa",
            fontSize = 30.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF27361F),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .height(45.dp)
                .width(352.dp)
        )
        Text(
            text = "Silahkan pilih bahasa untuk digunakan\ndalam aplikasi Tumbuh Nyata",
            fontSize = 17.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF27361F),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .height(51.dp)
                .width(352.dp)
        )

        Spacer(modifier = Modifier.height(46.dp))

        LanguageOption(
            flagEmoji = "🇮🇩",
            label = "Indonesia",
            value = "id",
            selected = selectedLanguage == "id",
            onSelect = { selectedLanguage = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        LanguageOption(
            flagEmoji = "🇺🇸",
            label = "English",
            value = "en",
            selected = selectedLanguage == "en",
            onSelect = { selectedLanguage = it }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("profile") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 1.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF27361F)
            )
        ) {
            Text(
                text = "Simpan",
                color = Color.White,
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LanguageOption(
    flagEmoji: String,
    label: String,
    value: String,
    selected: Boolean,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 3.dp,
                color = if (selected) Color(0xFF27361F) else Color(0xFF4B4B4B),
                shape = RoundedCornerShape(15.dp)
            )
            .clickable { onSelect(value) }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = flagEmoji,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(11.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF686868),
            modifier = Modifier.weight(1f)
        )
        RadioButton(
            selected = selected,
            onClick = null, // handled by Row.clickable
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF27361F)
            )
        )
    }
}

@Preview
@Composable
fun PreviewLanguagePreference() {
    LanguagePreference(navController = rememberNavController())
}