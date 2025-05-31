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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun SertifikasiAndaScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 40.dp, end = 20.dp)
    ) {
        TopBarProfile(
            title = "Sertifikasi Anda",
            step = "",
            iconResId = R.drawable.btn_back,
            onBackClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(24.dp))
        SertifikasiAndaSection()
    }
}

data class SertifikasiAnda(
    val title: String,
    val code: String,
    val issued: String,
    val credentialId: String,
    val imageRes: Int
)

@Composable
fun SertifikasiAndaSection() {
    val sertifikasiList = listOf(
        SertifikasiAnda(
            title = "Environmental Management System",
            code = "ISO 14001",
            issued = "Issued Jun 2024 - Expires Jun 2027",
            credentialId = "Credential ID ABC123XYZ",
            imageRes = R.drawable.iso_14001
        ),
        SertifikasiAnda(
            title = "Social Responsibility",
            code = "ISO 26000",
            issued = "Issued Feb 2023 - Expires Feb 2026",
            credentialId = "Credential ID DEF456LMN",
            imageRes = R.drawable.iso_26000
        ),
        SertifikasiAnda(
                title = "Carbon Footprint Certification",
        code = "ISCC",
        issued = "Issued Feb 2023 - Expires Feb 2026",
        credentialId = "Credential ID DEF456LMN",
        imageRes = R.drawable.iscc
        )
    )

    Spacer(modifier = Modifier.height(8.dp))

    sertifikasiList.forEach {
        SertifikasiAndaCard(it)
        Spacer(modifier = Modifier.height(12.dp))
    }
}


@Composable
fun SertifikasiAndaCard(data: SertifikasiAnda) {
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
                Text(data.issued, fontFamily = PoppinsFontFamily, fontSize = 12.sp)
                Text(data.credentialId, fontFamily = PoppinsFontFamily, fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = { /* show credential */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF8F8F8),
                        contentColor = Color(0xFF4B4B4B)
                    ),
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(1.dp, Color(0xFF4B4B4B))
                ) {
                    Text("show credential", fontFamily = PoppinsFontFamily, color = Color(0xFF4B4B4B), fontSize = 12.sp)
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF4B4B4B)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSertifikasiAndaScreen() {
    val navController = rememberNavController()
    SertifikasiAndaScreen(navController)
}