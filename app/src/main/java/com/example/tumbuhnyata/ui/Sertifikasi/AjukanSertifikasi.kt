package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tumbuhnyata.viewmodel.*

@Composable
fun AjukanSertifikasiScreen(navController: NavController) {
    val viewModel: AjukanSertifikasiViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 30.dp, end = 20.dp)
    ) {
        TopBarProfile(
            title = "Ajukan Sertifikasi",
            step = "",
            iconResId = R.drawable.btn_back,
            onBackClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Pilih Sertifikasi Anda",
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily,
            fontSize = 21.sp,
            color = Color(0xFF1A4218)
        )

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.error ?: "An error occurred")
                }
            }
            else -> {
                AjukanSertifikasiSection(navController, state.availableSertifikasiList, viewModel)
            }
        }
    }
}

data class AjukanSertifikasi(
    val title: String,
    val code: String,
    val deskripsi: String,
    val imageRes: Int
)

@Composable
fun AjukanSertifikasiSection(
    navController: NavController,
    sertifikasiList: List<AjukanSertifikasi>,
    viewModel: AjukanSertifikasiViewModel
) {
    Spacer(modifier = Modifier.height(8.dp))

    sertifikasiList.forEach { sertifikasi ->
        AjukanSertifikasiCard(
            data = sertifikasi,
            navController = navController,
            onSelect = { viewModel.selectSertifikasi(sertifikasi) }
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun AjukanSertifikasiCard(
    data: AjukanSertifikasi,
    navController: NavController,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp)
            .clickable { 
                onSelect()
                navController.navigate("detailsertifikasi")
            },
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
    val navController = rememberNavController()
    AjukanSertifikasiScreen(navController)
}