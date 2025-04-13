package com.example.tumbuhnyata.ui.components

import com.example.tumbuhnyata.data.model.Workshop
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.*

@Composable
fun DeskripsiWorkshop(navController: NavController, workshop: Workshop) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = workshop.imageRes),
                contentDescription = "Poster Workshop",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(317.dp, 352.dp)
                    .padding(16.dp)
            )
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Nama Workshop",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E),
                fontSize = 17.sp
            )
            Text(
                text = workshop.title,
                fontSize = 17.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = "Deskripsi",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E),
                fontSize = 17.sp
            )
            Text(
                text = workshop.deskripsi,
                fontSize = 14.sp,
                textAlign = TextAlign.Justify,
                color = Color(0xFF4B4B4B)
            )

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = "Instruktur",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E),
                fontSize = 17.sp
            )
            Text(
                text = workshop.speaker,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp,
                color = Color(0xFF4B4B4B)
            )

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = "Tanggal",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E),
                fontSize = 17.sp
            )
            Text(
                text = workshop.tanggal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                color = Color(0xFF4B4B4B)
            )

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = "Materi",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E),
                fontSize = 17.sp
            )
            workshop.materi.forEachIndexed { index, materi ->
                Text(
                    text = "${index + 1}. $materi",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Justify,
                    color = Color(0xFF4B4B4B)
                )
            }

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = "Biaya",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4B4B4B),
                fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(7.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFE9E9E9), shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rp",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4B4B4B)
                )
                Text(
                    text = NumberFormat.getNumberInstance(Locale("id", "ID")).format(workshop.biaya),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4B4B4B)
                )
            }

            Spacer(modifier = Modifier.height(23.dp))

            Button(
                onClick = { navController.navigate("daftarworkshop") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27361F))
            )  {
                Text(
                    text = "Daftar Sekarang",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
            Spacer(modifier = Modifier.height(17.dp))
        }
    }
}

