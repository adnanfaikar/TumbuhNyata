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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun UpdateProfile(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 70.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.img_profile), // Ganti dengan resource logo kamu
                    contentDescription = "Company Logo",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color(0xFF4B4B4B), CircleShape)
                )

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        text = "Nama Perusahaan",
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF4B4B4B),
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                    )
                    Text(
                        text = "PT Paragon Corp",
                        fontSize = 17.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B4B4B)
                    )
                    Divider()
                }
            }

            Text(
                text = "Ganti Foto",
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4B4B4B),
                modifier = Modifier
                    .padding(top = 9.dp)
                    .clickable { /* TODO: Open image picker */ }
            )

            Spacer(modifier = Modifier.height(46.dp))

            // Email
            Text(
                text = "Email*",
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4B4B4B)
            )
            Text(
                text = "csr@paragon.co.id",
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF989898),
                modifier = Modifier.padding(top = 5.dp)
            )
            Divider()

            Spacer(modifier = Modifier.height(35.dp))

            // Nomor Telepon
            Text(
                text = "Nomor Telepon*",
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4B4B4B)
            )
            Text(
                text = "2345 88907",
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4B4B4B),
                modifier = Modifier.padding(top = 5.dp)
            )
            Divider()
        }

        Spacer(modifier = Modifier.weight(1f))

        // Tombol Simpan
        Button(
            onClick = { navController.navigate("profile") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 1.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF27361F)
            )
        ) {
            Text(
                text = "Simpan",
                color = Color.White,
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
@Preview
@Composable
fun PreviewUpdateProfile() {
    UpdateProfile(navController = rememberNavController())
}