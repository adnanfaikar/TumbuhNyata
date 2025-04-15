package com.example.tumbuhnyata.ui.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun OptionScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Image(
            painter = painterResource(id = R.drawable.illustration),
            contentDescription = "Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .height(264.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_hitam),
                contentDescription = "Logo Hitam",
                modifier = Modifier.size(98.dp, 30.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Selamat Datang di Tumbuh Nyata!",
            fontSize = 30.sp,
            fontFamily = PoppinsFontFamily,
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1E1E1E)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Masuk ke akun Anda dan kelola program CSR dengan mudah, transparan, dan terukur serta wujudkan dampak sosial yang nyata",
            fontSize = 14.sp,
            fontFamily = PoppinsFontFamily,
            textAlign = TextAlign.Justify,
            color = Color(0xFF4B4B4B)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27361F))
        ) {
            Text(
                text = "Masuk",
                color = Color.White,
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Daftar",
                color = Color.Black,
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(110.dp))

        Text(
            text = "Dengan melakukan login atau registrasi, Anda menyetujui ",
            fontSize = 12.sp,
            fontFamily = PoppinsFontFamily,
            textAlign = TextAlign.Justify,
            color = Color(0xFF4B4B4B)
        )
        Row {
            Text(
                text = "Syarat & Ketentuan",
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4B4B4B)
            )
            Text(text = " serta ", fontSize = 12.sp, fontFamily = PoppinsFontFamily, color = Color(0xFF4B4B4B))
            Text(
                text = "Kebijakan Privasi",
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4B4B4B)
            )
        }
    }
}
