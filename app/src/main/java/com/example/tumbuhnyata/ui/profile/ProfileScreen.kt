package com.example.tumbuhnyata.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.BottomNavBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun ProfileScreen(navController: NavController) {
    var selectedIndex by remember { mutableStateOf(3) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_circle_top),
            contentDescription = "background",
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopStart)
                .offset(x = (-30).dp, y = (-10).dp)
        )
        Image(
            painter = painterResource(id = R.drawable.bg_circle_bottom),
            contentDescription = "background",
            modifier = Modifier
                .size(310.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (40).dp, y = (0).dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ){
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 95.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color(0xFF4B4B4B), CircleShape)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(end = 35.dp),
                ) {
                    Text(
                        text = "PT Paragon Corp",
                        fontSize = 26.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Kampung Baru, No 1 Jakarta",
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF4B4B4B)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.arrow_profile),
                    contentDescription = "Arrow",
                    modifier = Modifier
                        .size(26.dp),
                )
            }
            Spacer(modifier = Modifier.height(60.dp))
            ProfileOption("Verifikasi Akun", R.drawable.ic_verification_account, R.drawable.arrow_option, onClick = { navController.navigate("verification_one") })
            ProfileOption("Ganti Password", R.drawable.ic_change_password, R.drawable.arrow_option, onClick = {navController.navigate("change_password")})
            ProfileOption("Ganti Profile", R.drawable.ic_person, R.drawable.arrow_option, onClick = {navController.navigate("update_profile")})
            ProfileOption("Bantuan dan Dukungan", R.drawable.ic_help_support, R.drawable.arrow_option, onClick ={})
            ProfileOption("Bahasa", R.drawable.ic_langauge, R.drawable.arrow_option, onClick = {})
            ProfileOption("Tentang Aplikasi", R.drawable.ic_about, R.drawable.arrow_option, onClick = { navController.navigate("about") })

            Spacer(modifier = Modifier.height(20.dp))

            LogoutButton(navController = navController)
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f))
        BottomNavBarProfile(selectedIndex) { selectedIndex = it }
    }
}
@Composable
fun ProfileOption(title: String, iconStart: Int, iconEnd: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .height(60.dp)
            .border(0.5.dp, Color(0xFFB0B0B0), RoundedCornerShape(10.dp))
            .clickable (onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconStart),
                contentDescription = "Icon Option",
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(13.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 14.sp,
                    letterSpacing = 0.sp,
                    modifier = Modifier.width(243.dp)
                )
                Image(
                    painter = painterResource(id = iconEnd),
                    contentDescription = "Icon Arrow",
                    modifier = Modifier
                        .size(16.dp)
                        .align(alignment = Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun LogoutButton(navController : NavController) {
    Button(
        onClick = { navController.navigate("option") },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27361F)),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logout),
                contentDescription = "Icon Logout",
                modifier = Modifier
                    .size(32.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "Logout",
                color = Color(0xFFF8F8F8),
                fontSize = 18.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.arrow_option),
                contentDescription = "Icon Arrow",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewProfileScreen() {
    ProfileScreen(navController = rememberNavController())
}