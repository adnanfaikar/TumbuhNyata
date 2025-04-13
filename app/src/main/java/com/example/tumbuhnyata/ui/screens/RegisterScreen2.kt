package com.example.tumbuhnyata.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.LocationSelector
import com.example.tumbuhnyata.ui.components.StepIndicator

@Composable
fun RegisterScreen2(navController: NavController) {
    var locationSelected by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_register),
            contentDescription = "Background Register",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(300.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepIndicator(
                    step = "1", color = Color(0xFFA5C295), strokeColor = Color.Black, textColor = Color.Black,
                    modifier = Modifier.clickable { navController.navigate("register") }
                )
                Divider(modifier = Modifier.width(79.dp).height(3.dp).background(Color(0xFF525E4C)))
                StepIndicator(step = "2", color = Color(0xFFA5C295), strokeColor = Color.Black, textColor = Color.Black)
                Divider(modifier = Modifier.width(79.dp).height(3.dp).background(Color(0xFF525E4C)))
                StepIndicator(step = "3", color = Color.White, strokeColor = Color.Black, textColor = Color.Black)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Lokasi Kantor",
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF27361F)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (locationSelected) {
                Image(
                    painter = painterResource(id = R.drawable.dummy_loc),
                    contentDescription = "Selected Location",
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                LocationSelector(onLocationSelected = { locationSelected = true })
            }

            Spacer(modifier = Modifier.height(33.dp))

            Button(
                onClick = { navController.navigate("register3") },
                enabled = locationSelected,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (locationSelected) Color(0xFF27361F) else Color.Gray
                )
            ) {
                Text(
                    text = "Selanjutnya",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(13.dp))

            Image(
                painter = painterResource(id = R.drawable.btn_google),
                contentDescription = "Masuk dengan Google",
                modifier = Modifier.fillMaxWidth().clickable {  }
            )

            Spacer(modifier = Modifier.height(33.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sudah mempunyai akun? ", fontSize = 14.sp, color = Color(0xFF27361F))
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF27361F),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewRegisterScreen2() {
    RegisterScreen2(navController = rememberNavController())
}
