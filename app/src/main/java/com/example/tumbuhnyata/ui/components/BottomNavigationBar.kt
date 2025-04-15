package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun BottomNavigationBar(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = R.drawable.ic_unhome,
                iconSelected = R.drawable.ic_home,
                label = "Beranda",
                route = "home",
                navController = navController
            )
            BottomNavItem(
                icon = R.drawable.ic_unsertif,
                iconSelected = R.drawable.ic_sertif,
                label = "Sertifikasi",
                route = "sertifikasi",
                navController = navController
            )
            BottomNavItem(
                icon = R.drawable.ic_unwork,
                iconSelected = R.drawable.ic_work,
                label = "Workshop",
                route = "workshop",
                navController = navController
            )
            BottomNavItem(
                icon = R.drawable.ic_unprofile,
                iconSelected = R.drawable.ic_profile,
                label = "Profil",
                route = "profile",
                navController = navController
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: Int,
    iconSelected: Int,
    label: String,
    route: String,
    navController: NavController
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    val isSelected = currentRoute == route
    val selectedColor = Color(0xFF4C8C4A)
    val unselectedColor = Color(0xFF888888)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable {
                if (currentRoute != route) {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
    ) {
        Image(
            painter = painterResource(id = if (isSelected) iconSelected else icon),
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(if (isSelected) selectedColor else unselectedColor)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontFamily = PoppinsFontFamily,
            color = if (isSelected) selectedColor else unselectedColor
        )
    }
}

@Preview
@Composable
fun PreviewBottomNavigation() {
    val navController = rememberNavController()
    BottomNavigationBar(navController = navController)
}
