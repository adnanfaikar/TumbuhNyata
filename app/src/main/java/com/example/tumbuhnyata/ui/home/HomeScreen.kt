package com.example.tumbuhnyata.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.ui.components.BottomNavigationBar

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(bottom = paddingValues.calculateBottomPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainCard(navController)
            Text(
                text = "Kelola Program CSR Anda",
                fontSize = 21.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            )
            MenuButtons(navController)
            ActivitySection(navController)
        }
    }
}

@Composable
fun MainCard(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp
            ),
        shape = RoundedCornerShape(16.dp)
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
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                CompanyHeader(hasNotifications = true, navController)
                CSRStatusSection()
                CSRFundSection()
                BadgesSection()
                Button(
                    onClick = { navController.navigate("dashboard") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A613C)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "Detail Dashboard  >", 
                        color = Color.White,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CompanyHeader(hasNotifications: Boolean, navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.pt_profile),
                contentDescription = "Company Logo",
                modifier = Modifier
                    .size(53.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "PT Paragon Corp",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    "Kampung Baru, No.1 Jakarta",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
        Button(
            onClick = { },
            modifier = Modifier
                .width(38.dp)
                .height(38.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A613C)),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(id = if (hasNotifications) R.drawable.ic_notif_ping else R.drawable.ic_notif),
                contentDescription = "Notifications",
                modifier = Modifier
                    .size(if (hasNotifications) 22.dp else 22.dp, if (hasNotifications) 26.dp else 24.dp)
            )
        }
    }
}

@Composable
fun CSRStatusSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 21.dp, bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x331E1E1E))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                "Status CSR",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusItem("Selesai ", "12")
                StatusItem("    Progres ", "4")
                StatusItem("    Mendatang ", "7")
            }
        }
    }
}

@Composable
fun StatusItem(label: String, value: String) {
    Row {
        Text(
            "$label: ",
            color = Color.White
        )
        Text(
            value,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CSRFundSection() {
    Column(modifier = Modifier.padding(top = 8.dp, bottom = 20.dp, start = 14.dp, end = 14.dp)) {
        Text(
            "Riwayat Dana CSR",
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_money),
                contentDescription = "Money",

                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Rp 2.987.450.725",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp
            )
        }
        Text(
            "*terhitung dari dana CSR yang telah selesai",
            color = Color.White.copy(alpha = 0.7f),
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp
        )
    }
}

@Composable
fun BadgesSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Level Badge
        Card(
            modifier = Modifier
                .width(148.dp)
                .height(153.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0x331E1E1E))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(9.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    "Level Badge", 
                    color = Color.White, 
                    fontSize = 14.sp, 
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .size(width = 115.dp, height = 70.dp)
                        .background(
                            color = Color(0x331E1E1E),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.superstar_badge),
                        contentDescription = "Badge",
                        modifier = Modifier
                            .width(82.dp)
                            .height(52.dp)
                    )
                }
                Text(
                    "Super Star", 
                    color = Color.White, 
                    fontSize = 14.sp, 
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Emisi Hilang
        Card(
            modifier = Modifier
                .width(148.dp)
                .height(153.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0x331E1E1E))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(9.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    "Emisi Hilang", 
                    color = Color.White, 
                    fontSize = 14.sp, 
                    fontWeight = FontWeight.SemiBold
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_emission),
                    contentDescription = "Leaf",
                    modifier = Modifier.size(60.dp)
                )
                Text(
                    "4.250 kg CO₂e", 
                    color = Color.White, 
                    fontSize = 14.sp, 
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun MenuButtons(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MenuButton("Ajukan CSR", R.drawable.ic_ajukan) { navController.navigate("csrsubmission") }
        MenuButton("Riwayat", R.drawable.ic_history) { navController.navigate("riwayat") }
        MenuButton("Keuangan", R.drawable.ic_finance)
    }
}

@Composable
fun MenuButton(text: String, iconRes: Int, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .width(110.dp)
                .height(92.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF5A7C47),
                                Color(0xFF415A33),
                                Color(0xFF27361F)
                            )
                        )
                    )
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = text,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text, 
            fontSize = 14.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ActivitySection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Aktivitas Terbaru",
                fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold
            )
            TextButton(onClick = { navController.navigate("riwayat") }) {
                Text(
                    "Lihat Semua   >",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF4C8C4A)
                )
            }
        }
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 0.dp)
        ) {
            item {
                ActivityItem(
                    title = "Penanaman 1000 Pohon",
                    community = "Komunitas Jaya Hijau",
                    status = "Program Selesai",
                    kategori = "Lingkungan",
                    lokasi = "Jakarta Timur",
                    periode = "12 Mar - 20 Jun 24",
                    statusType = StatusType.COMPLETED,
                    navController = navController
                )
            }
            item {
                ActivityItem(
                    title = "Penghijauan Hutan Kaltim",
                    community = "PT Hijau Sejati",
                    status = "Mendatang",
                    kategori = "Lingkungan",
                    lokasi = "Kalimantan",
                    periode = "12 Mar - 20 Mar 25",
                    statusType = StatusType.UPCOMING,
                    navController = navController
                )
            }
            item {
                ActivityItem(
                    title = "Beasiswa Yatim Jabar",
                    community = "Pemerintah Prov. Jabar",
                    status = "Progress",
                    kategori = "Sosial",
                    lokasi = "Jawa Barat",
                    periode = "6 Mar - 15 Jun 25",
                    statusType = StatusType.IN_PROGRESS,
                    navController = navController
                )
            }
            item {
                ActivityItem(
                    title = "Donor Darah Paragon 2025",
                    community = "RS Bunda Mulia",
                    status = "Progress",
                    kategori = "Sosial",
                    lokasi = "Jakarta Raya",
                    periode = "12 Jan - 2 Apr 25",
                    statusType = StatusType.IN_PROGRESS,
                    navController = navController
                )
            }
            item {
                ActivityItem(
                    title = "Penanaman Mangrove",
                    community = "Pemkot Kota Lombok",
                    status = "Program Selesai",
                    kategori = "Lingkungan",
                    lokasi = "Pantai Barat, Lombok",
                    periode = "12 Mar - 20 Jun 24",
                    statusType = StatusType.COMPLETED,
                    navController = navController
                )
            }
        }
    }
}

enum class StatusType {
    COMPLETED,
    IN_PROGRESS,
    UPCOMING
}

@Composable
fun ActivityItem(
    title: String,
    community: String,
    status: String,
    kategori: String,
    lokasi: String,
    periode: String,
    statusType: StatusType,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .padding(vertical = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Status indicator on the left
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        when (statusType) {
                            StatusType.COMPLETED -> Color(0xFF4C8C4A)
                            StatusType.IN_PROGRESS -> Color(0xFFFFC107)
                            StatusType.UPCOMING -> Color(0xFF2196F3)
                        }
                    )
            )
            
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = community,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status : ",
                        fontSize = 10.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = status,
                        fontSize = 10.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (statusType == StatusType.COMPLETED) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = "Check",
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(16.dp)
                        )
                    }
                }
                
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color(0xFFEEEEEE)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Kategori",
                            fontSize = 10.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Text(
                            text = kategori,
                            fontSize = 8.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Column {
                        Text(
                            text = "Lokasi",
                            fontSize = 10.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Text(
                            text = lokasi,
                            fontSize = 8.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Column {
                        Text(
                            text = "Periode",
                            fontSize = 10.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Text(
                            text = periode,
                            fontSize = 8.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}

