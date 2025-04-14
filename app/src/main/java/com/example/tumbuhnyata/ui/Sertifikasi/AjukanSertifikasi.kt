package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.TopBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun AjukanSertifikasiScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 95.dp, end = 20.dp)
    ) {
        TopBarProfile(
            title = "Ajukan Sertifikasi",
            step = "",
            iconResId = R.drawable.btn_back,
            onBackClick = { }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Pilih Sertifikasi Anda",
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily,
            fontSize = 21.sp,
            color = Color(0xFF1A4218)
        )

        AjukanSertifikasiSection()
    }
}

data class AjukanSertifikasi(
    val title: String,
    val code: String,
    val deskripsi: String,
    val imageRes: Int
)

@Composable
fun AjukanSertifikasiSection() {
    val sertifikasiList = listOf(
        AjukanSertifikasi(
            title = "Social Responsibility",
            code = "ISO 26000",
            deskripsi = "International Organization for Standardization (ISO)",

            imageRes = R.drawable.iso_26000
        ),
        AjukanSertifikasi(
            title = "PROPER",
            code = "Program Penilaian Peringkat Kinerja Perusahaan dalam Pengelolaan Lingkungan",
            deskripsi = "Kementerian Lingkungan Hidup dan Kehutanan (KLHK)",

            imageRes = R.drawable.proper
        ),
        AjukanSertifikasi(
            title = "Ecolabel Indonesia",
            code = "Ecolabel",
            deskripsi = "Kementerian Perindustrian RI",

            imageRes = R.drawable.ecolabel
        ),
        AjukanSertifikasi(
                title = "Social Accountability Certification",
        code = "SA8000",
        deskripsi = "Social Accountability International (SAI)",

        imageRes = R.drawable.sai
    )
    )

    Spacer(modifier = Modifier.height(8.dp))

    sertifikasiList.forEach {
        AjukanSertifikasiCard(it)
        Spacer(modifier = Modifier.height(12.dp))
    }
}


@Composable
fun AjukanSertifikasiCard(data: AjukanSertifikasi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = data.imageRes),
                contentDescription = "Sertifikasi Icon",
                modifier = Modifier
                    .size(78.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(data.title, fontWeight = FontWeight.Bold, fontFamily = PoppinsFontFamily, fontSize = 17.sp)
                Text(data.code, fontSize = 14.sp, fontFamily = PoppinsFontFamily, color = Color.Gray)
                Text(data.deskripsi, fontFamily = PoppinsFontFamily, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAjukanSertifikasiScreen() {
    AjukanSertifikasiScreen()
}