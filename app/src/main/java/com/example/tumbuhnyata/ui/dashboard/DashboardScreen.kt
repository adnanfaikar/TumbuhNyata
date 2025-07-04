package com.example.tumbuhnyata.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.dashboard.components.KPIItem
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.viewmodel.DashboardViewModel
import com.example.tumbuhnyata.viewmodel.DashboardState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF27361F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.arrow_left),
                                contentDescription = "Back",
                                modifier = Modifier.size(16.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Oops! Terjadi Kesalahan", 
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error ?: "Error tidak diketahui.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadDashboardItems() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27361F))
                        ) {
                            Text("Coba Lagi")
                        }
                    }
                }

                !uiState.isLoading && uiState.error == null && uiState.kpiItems.isEmpty() -> {
                     Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_carbonfootprint),
                            contentDescription = "Tidak ada data",
                            modifier = Modifier.size(100.dp) 
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Tidak Ada Data KPI",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                         Text(
                            text = "Belum ada data KPI untuk ditampilkan saat ini.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                         Spacer(modifier = Modifier.height(16.dp))
                         Button(
                            onClick = { viewModel.loadDashboardItems() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27361F))
                        ) {
                            Text("Muat Ulang")
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            "KPI (Key Performance Indicator)",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontWeight = FontWeight(800),
                            fontSize = 18.sp,
                            color = Color(0xFF27361F)
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(uiState.kpiItems) { kpiItem ->
                                KPIItem(
                                    title = kpiItem.title,
                                    topIcon = kpiItem.topIcon,
                                    statusText = kpiItem.statusText,
                                    statusPercentageValue = kpiItem.statusPercentageValue,
                                    isUp = kpiItem.isUp,
                                    value = kpiItem.value,
                                    unit = kpiItem.unit,
                                    targetValue = kpiItem.targetValue,
                                    onClick = { navController.navigate(kpiItem.onClickRoute) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true, name = "Dashboard Loading")
//@Composable
//fun DashboardScreenPreviewLoading() {
//    val mockViewModel = DashboardViewModel(Application())
//    DashboardScreen(navController = rememberNavController() /*, viewModel = mockViewModel */)
//}

@Preview(showBackground = true, name = "Dashboard Error")
@Composable
fun DashboardScreenPreviewError() {
    DashboardScreen(navController = rememberNavController())
}

@Preview(showBackground = true, name = "Dashboard No Data")
@Composable
fun DashboardScreenPreviewNoData() {
    DashboardScreen(navController = rememberNavController())
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "Dashboard Loaded Data")
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController())
}
package com.example.tumbuhnyata.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.dashboard.components.KPIItem
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF27361F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.arrow_left),
                                contentDescription = "Back",
                                modifier = Modifier.size(16.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                "KPI (Key Performance Indicator)",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight(800),
                fontSize = 18.sp,
                color = Color(0xFF27361F)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    KPIItem(
                        title = "Carbon Footprint",
                        topIcon = R.drawable.ic_carbonfootprint,
                        statusText = "100% target",
                        statusPercentageValue = "5%",
                        isUp = true,
                        value = "12.300",
                        unit = "kg CO₂e",
                        targetValue = "10.000 kg CO₂e",
                        onClick = { navController.navigate("kpi_detail/carbon_footprint") }
                    )
                }
                item {
                    KPIItem(
                        title = "Konsumsi Energi",
                        topIcon = R.drawable.ic_konsumsienergi,
                        statusText = "94% target",
                        statusPercentageValue = "8%",
                        isUp = false,
                        value = "8.450",
                        unit = "kWh",
                        targetValue = "9.000 kWh",
                        onClick = { navController.navigate("kpi_detail/energy_usage") }
                    )
                }

                item {
                    KPIItem(
                        title = "Penggunaan Air",
                        topIcon = R.drawable.ic_penggunaanair,
                        statusText = "93% target",
                        statusPercentageValue = "4%",
                        isUp = false,
                        value = "56.000",
                        unit = "L",
                        targetValue = "60.000 L",
                        onClick = { navController.navigate("kpi_detail/water_usage") }
                    )
                }
                item {
                    KPIItem(
                        title = "Pohon Tertanam",
                        topIcon = R.drawable.ic_pohontertanam,
                        statusText = "75% target",
                        statusPercentageValue = "25%",
                        isUp = true,
                        value = "4.500",
                        unit = "Pohon",
                        targetValue = "6.000 Pohon",
                        onClick = { navController.navigate("kpi_detail/biodiversity") }
                    )
                }

                item {
                    KPIItem(
                        title = "Pengelolaan Sampah",
                        topIcon = R.drawable.ic_pengelolaansampah,
                        statusText = "78% target",
                        statusPercentageValue = "30%",
                        isUp = true,
                        value = "7.800",
                        unit = "kg",
                        targetValue = "10.000 Kg",
                        onClick = { navController.navigate("kpi_detail/waste") }
                    )
                }
                item {
                    KPIItem(
                        title = "Penerima Manfaat",
                        topIcon = R.drawable.ic_penerimamanfaat,
                        statusText = "80% target",
                        statusPercentageValue = "12%",
                        isUp = true,
                        value = "12.000",
                        unit = "orang",
                        targetValue = "15.000 Orang",
                        onClick = { navController.navigate("kpi_detail/sustainability") }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController())
}
