package com.example.tumbuhnyata.ui.workshop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.local.entity.OfflineWorkshopRegistration
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.viewmodel.WorkshopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatWorkshopScreen(
    navController: NavController,
    viewModel: WorkshopViewModel,
    modifier: Modifier = Modifier
) {
    val hasPendingSync by viewModel.hasPendingSync.collectAsState()
    val syncInProgress by viewModel.syncInProgress.collectAsState()
    val syncMessage by viewModel.syncMessage.collectAsState()
    val workshopHistory by viewModel.workshopHistory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var isSelectionMode by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Riwayat Workshop",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(
                            painter = painterResource(R.drawable.btn_back),
                            contentDescription = "backbutton"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (hasPendingSync) {
                    SyncIndicator(
                        isLoading = syncInProgress,
                        message = syncMessage,
                        onSyncClick = { viewModel.syncRegistrations() }
                    )
                }

                if (workshopHistory.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_unwork),
                                contentDescription = "No Data",
                                modifier = Modifier.size(120.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Belum ada riwayat workshop",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            isSelectionMode = !isSelectionMode
                            if (!isSelectionMode) selectedItems.clear()
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27361F)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (isSelectionMode) "Batal Pilih" else "Pilih Workshop",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(workshopHistory) { registration ->
                            val isChecked = selectedItems[registration.id] ?: false

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateContentSize()
                            ) {
                                AnimatedVisibility(visible = isSelectionMode) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { checked ->
                                            selectedItems[registration.id] = checked
                                        },
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                }

                                WorkshopHistoryCard(
                                    registration = registration,
                                    viewModel = viewModel,
                                    isSelectionMode = isSelectionMode,
                                    isChecked = isChecked,
                                    onCheckedChange = {}
                                )
                            }
                        }
                    }

                    if (isSelectionMode && selectedItems.any { it.value }) {
                        Button(
                            onClick = {
                                val idsToDelete = selectedItems.filter { it.value }.map { it.key }
                                viewModel.deleteRegistrationsByIds(idsToDelete)
                                selectedItems.clear()
                                isSelectionMode = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDF3737)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "Hapus Workshop (${selectedItems.count { it.value }})",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center),
                    color = Color(0xFF5A7C47)
                )
            }
        }
    }
}

@Composable
fun WorkshopHistoryCard(
    registration: OfflineWorkshopRegistration,
    viewModel: WorkshopViewModel,
    isSelectionMode: Boolean,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val workshop = viewModel.getWorkshopById(registration.workshopId)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(155.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFE2E2E2),
                shape = RoundedCornerShape(10.dp)
            ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = workshop?.imageRes ?: R.drawable.work1),
                contentDescription = "Workshop image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(140.dp)
                    .fillMaxHeight()
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = workshop?.title ?: "Workshop tidak ditemukan",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E1E1E)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = registration.companyName,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = registration.email,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status: ${if (registration.isSynced) "Tersinkronisasi" else "Belum Tersinkronisasi"}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = if (registration.isSynced) Color(0xFF4CAF50) else Color(0xFFFFA000)
                    )

                    if (!registration.isSynced) {
                        Surface(
                            modifier = Modifier
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF5A7C47),
                                            Color(0xFF415A33),
                                            Color(0xFF27361F)
                                        )
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Pending",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily
                            )
                        }
                    }
                }
            }
        }
    }
}