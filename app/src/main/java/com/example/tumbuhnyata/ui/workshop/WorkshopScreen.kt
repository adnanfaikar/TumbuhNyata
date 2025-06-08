package com.example.tumbuhnyata.ui.workshop

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.tumbuhnyata.ui.components.BottomNavigationBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.SectionWithTitle
import com.example.tumbuhnyata.ui.components.WorkshopCard
import com.example.tumbuhnyata.ui.components.WorkshopListItem
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.viewmodel.WorkshopViewModel

@Composable
fun WorkshopScreen(
    navController: NavController,
    viewModel: WorkshopViewModel
) {
    val recommended by viewModel.recommended
    val recent by viewModel.recent
    val hasPendingSync by viewModel.hasPendingSync.collectAsState()
    val syncInProgress by viewModel.syncInProgress.collectAsState()
    val syncMessage by viewModel.syncMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.checkPendingSync()
        // Cek koneksi database
        if (!viewModel.isDatabaseOnline()) {
            Toast.makeText(
                context,
                "Koneksi database terputus. Menggunakan data offline",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("riwayatworkshop")
                },
                shape = CircleShape,
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF5A7C47),
                                    Color(0xFF415A33),
                                    Color(0xFF27361F)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_unwork),
                        contentDescription = "Riwayat",
                        modifier = Modifier
                            .fillMaxSize(0.5f),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(androidx.compose.ui.graphics.Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            if (hasPendingSync) {
                SyncIndicator(
                    isLoading = syncInProgress,
                    message = syncMessage,
                    onSyncClick = { viewModel.syncRegistrations() }
                )
            }

            SectionWithTitle(
                title = "Rekomendasi",
                onSeeAllClick = { navController.navigate("rekomendasiworkshop") }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recommended) { workshop ->
                    WorkshopCard(workshop = workshop) {
                        viewModel.setWorkshopId(workshop.id)
                        Log.d("WorkshopScreen", "Recomend workshop clicked - ID: ${workshop.id}, Title: ${workshop.title}")
                        navController.navigate("deskripsiworkshop/${workshop.id}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionWithTitle(
                title = "Terbaru",
                onSeeAllClick = {
                    navController.navigate("workshopterbaru")
                }
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                recent.forEach { workshop ->
                    WorkshopListItem(
                        workshop = workshop,
                        onClick = {
                            viewModel.setWorkshopId(workshop.id)
                            navController.navigate("deskripsiworkshop/${workshop.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SyncIndicator(
    isLoading: Boolean,
    message: String?,
    onSyncClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF5A7C47),
                            Color(0xFF415A33),
                            Color(0xFF27361F)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Pendaftaran Offline",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Text(
                        text = message ?: "Ada pendaftaran yang belum disinkronkan",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    IconButton(
                        onClick = onSyncClick,
                        modifier = Modifier
                            .background(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "Sync",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
