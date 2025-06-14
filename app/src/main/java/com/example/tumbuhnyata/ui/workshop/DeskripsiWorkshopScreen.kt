package com.example.tumbuhnyata.ui.workshop

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.DeskripsiWorkshop
import com.example.tumbuhnyata.viewmodel.WorkshopDetailViewModel
import com.example.tumbuhnyata.viewmodel.WorkshopViewModel

@Composable
fun DeskripsiWorkshopScreen(
    navController: NavController,
    workshopId: String,
    viewModelDetail: WorkshopDetailViewModel = viewModel(),
    viewModel: WorkshopViewModel
) {
    LaunchedEffect(workshopId) {
        viewModelDetail.loadWorkshopById(workshopId)
        viewModel.setWorkshopId(workshopId)
        Log.d("DeskripsiWorkshopScreen", "Workshop loaded - ID: $workshopId")
    }

    val workshop by viewModelDetail.selectedWorkshop.collectAsState()

    if (workshop != null) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
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
                            .clickable { navController.popBackStack() }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Deskripsi Workshop",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 25.sp,
                        color = Color(0xFF1E1E1E)
                    )
                }
            }

            DeskripsiWorkshop(
                navController = navController, workshop = workshop!!, workshopId = workshopId
            )
        }
    }
}