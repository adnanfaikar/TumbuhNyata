package com.example.tumbuhnyata.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import com.example.tumbuhnyata.ui.dashboard.components.KPIItem
import androidx.compose.material3.Scaffold

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
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight(800),
                fontSize = 18.sp,
                color = Color(0xFF27361F)
            )

            Spacer(modifier = Modifier.height(0.dp))

            // KPI Cards Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Carbon Footprint
                item {
                    KPIItem(
                        title = "Carbon Footprint",
                        topIcon = R.drawable.ic_carbonfootprint,
                        statusText = "100% target",
                        statusPercentage = "▲ 5%",
                        statusIcon = Icons.Filled.ArrowUpward,
                        value = "12.300",
                        unit = "kg CO₂e",
                        targetValue = "10.000 kg CO₂e",
                        onClick = { navController.navigate("kpi_detail/carbon_footprint") }
                    )
                }

                // Penggunaan Air
                item {
                    KPIItem(
                        title = "Penggunaan Air",
                        topIcon = R.drawable.ic_penggunaanair,
                        statusText = "85% target",
                        statusPercentage = "▼ 3%",
                        statusIcon = Icons.Filled.ArrowUpward,
                        value = "534",
                        unit = "m³",
                        targetValue = "450 m³",
                        onClick = { navController.navigate("kpi_detail/water_usage") }
                    )
                }

                // Konsumsi Energi
                item {
                    KPIItem(
                        title = "Konsumsi Energi",
                        topIcon = R.drawable.ic_konsumsienergi,
                        statusText = "92% target",
                        statusPercentage = "▲ 2%",
                        statusIcon = Icons.Filled.ArrowUpward,
                        value = "2.450",
                        unit = "kWh",
                        targetValue = "2.250 kWh",
                        onClick = { navController.navigate("kpi_detail/energy_usage") }
                    )
                }

                // Pengelolaan Sampah
                item {
                    KPIItem(
                        title = "Pengelolaan Sampah",
                        topIcon = R.drawable.ic_pengelolaansampah,
                        statusText = "78% target",
                        statusPercentage = "▼ 7%",
                        statusIcon = Icons.Filled.ArrowUpward,
                        value = "342",
                        unit = "kg",
                        targetValue = "265 kg",
                        onClick = { navController.navigate("kpi_detail/waste") }
                    )
                }

                // Pohon Tertanam
                item {
                    KPIItem(
                        title = "Pohon Tertanam",
                        topIcon = R.drawable.ic_pohontertanam,
                        statusText = "95% target",
                        statusPercentage = "▲ 8%",
                        statusIcon = Icons.Filled.ArrowUpward,
                        value = "76",
                        unit = "score",
                        targetValue = "80 score",
                        onClick = { navController.navigate("kpi_detail/biodiversity") }
                    )
                }

                // Penerima Manfaat
                item {
                    KPIItem(
                        title = "Penerima Manfaat",
                        topIcon = R.drawable.ic_penerimamanfaat,
                        statusText = "90% target",
                        statusPercentage = "▲ 4%",
                        statusIcon = Icons.Filled.ArrowUpward,
                        value = "8.7",
                        unit = "index",
                        targetValue = "9.5 index",
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
