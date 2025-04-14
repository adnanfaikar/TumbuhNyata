package com.example.tumbuhnyata.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun AboutScreen(navController: NavController) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Image (
            painter = painterResource(id = R.drawable.bg_onboarding),
            contentDescription = "Background Onboarding",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end =15.dp, top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Image(
                        painter = painterResource(id = R.drawable.btn_back_white),
                        contentDescription = "Back",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Tentang Aplikasi",
                    fontSize = 24.sp,
                    fontFamily = PoppinsFontFamily,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_splash),
                contentDescription = "Logo TumbuhNyata",
                modifier = Modifier
                    .width(218.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Tumbuh Nyata adalah platform digital yang dirancang untuk membantu perusahaan dalam mengelola program Corporate Social Responsibility (CSR) secara lebih transparan, efektif, dan berdampak nyata. Dengan berbagai fitur inovatif, aplikasi ini memudahkan perusahaan dalam merancang, mengajukan, memantau, serta mengevaluas pelaksanaan CSR agar selaras dengan visi keberlanjutan dan tanggung jawab sosial.",
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.White,
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Melalui kolaborasi dengan lembaga kredensial dan mitra CSR, Tumbuh Nyata memastikan bahwa setiap program yang dijalankan memiliki standar tinggi serta dapat diukur dampaknya. Dengan sistem yang terintegrasi, perusahaan dapat mengoptimalkan kontribusi sosial mereka, meningkatkan kredibilitas, dan memperkuat hubungan dengan pemangku kepentingan demi masa depan yang lebih baik",
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.White,
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(90.dp))

            Text(
                text = buildAnnotatedString {
                    append("Versi Aplikasi: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                        append("Tumbuh Nyata 1.0.0")
                    }
                },
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAboutScreen() {
    AboutScreen(navController = rememberNavController())
}