package com.yourapp.ui.components

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
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.register.RegisterScreen
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
                icon = R.drawable.ic_home,
                iconSelected = R.drawable.ic_home_fill,
                label = "Beranda",
                route = "home",
                navController = navController
            )
            BottomNavItem(
                icon = R.drawable.ic_certificate,
                iconSelected = R.drawable.ic_certificate_fill,
                label = "Sertifikasi",
                route = "sertifikasi",
                navController = navController
            )
            BottomNavItem(
                icon = R.drawable.ic_workshop,
                iconSelected = R.drawable.ic_workshop_fill,
                label = "Workshop",
                route = "workshop",
                navController = navController
            )
            BottomNavItem(
                icon = R.drawable.ic_profile,
                iconSelected = R.drawable.ic_profile_fill,
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
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val isSelected = currentRoute == route
    val selectedColor = Color(0xFF4C8C4A)
    val unselectedColor = Color.Gray

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
}package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.model.BottomNavItem
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Beranda", "home", R.drawable.ic_unhome, R.drawable.ic_home),
        BottomNavItem("Sertifikasi", "sertifikasi", R.drawable.ic_unsertif, R.drawable.ic_sertif),
        BottomNavItem("Workshop", "workshop", R.drawable.ic_unwork, R.drawable.ic_work),
        BottomNavItem("Profil", "profil", R.drawable.ic_unprofile, R.drawable.ic_profile)
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(id = if (isSelected) item.selectedIconRes else item.unselectedIconRes),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(item.label, fontFamily = PoppinsFontFamily, color = if (isSelected) Color(0xFF27361F) else Color(0xFF888888)) },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                }
            )
        }
    }
}
