package com.example.tumbuhnyata.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.model.recommendedWorkshops
import com.example.tumbuhnyata.ui.components.WorkshopListItem

@Composable
fun RekomWorkshop(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.btn_back),
                    contentDescription = "Kembali",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController.navigate("workshop") }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Rekomendasi",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp,
                    color = Color(0xFF1E1E1E)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            recommendedWorkshops.forEach { workshop ->
                WorkshopListItem(
                    workshop = workshop,
                    onClick = { navController.navigate("deskripsiworkshop/${workshop.id}") }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewRekomWorkshop() {
    RekomWorkshop(navController = rememberNavController())
}