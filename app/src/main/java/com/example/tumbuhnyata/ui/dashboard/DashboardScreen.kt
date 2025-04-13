package com.example.tumbuhnyata.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar // Import Material 3 TopAppBar
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
import androidx.navigation.compose.rememberNavController // Use rememberNavController for preview
import com.example.tumbuhnyata.R // Make sure this matches your project's R file package
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily // Assuming you have this font defined
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Info
import com.example.tumbuhnyata.ui.dashboard.components.KPIItem
import androidx.compose.material3.Scaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,// Use Scaffold to apply theme background and structure
        topBar = {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard",
//                    modifier = Modifier.padding(start = 20.dp),
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
                    // Make TopAppBar background transparent to show Scaffold surface color
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues -> // Content lambda provides padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
                // .verticalScroll(rememberScrollState()) // Optional: Add if content exceeds screen
        ) {
            // Remove the TopAppBar from here, it's now in Scaffold's topBar
            // Spacer(modifier = Modifier.height(8.dp)) // Adjust or remove spacing as needed after moving TopAppBar

            // KPI Section Title
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
                // Carbon Footprint KPI
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

                // Water Usage KPI
                item {
                    KPIItem(
                        title = "Water Usage",
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

                // Energy Consumption KPI
                item {
                    KPIItem(
                        title = "Energy Usage",
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

                // Waste Management KPI
                item {
                    KPIItem(
                        title = "Waste",
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

                // Biodiversity Score KPI
                item {
                    KPIItem(
                        title = "Biodiversity",
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

                // Sustainability Index KPI
                item {
                    KPIItem(
                        title = "Sustainability",
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

// Removed the custom TopAppBar function definition as we are using the Material 3 one

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF) // Set preview background to white
@Composable
fun DashboardScreenPreview() {
    // Use rememberNavController() for previewing Composables that need a NavController
    // In a real app, this NavController would be provided by your NavHost
    DashboardScreen(navController = rememberNavController())
}
