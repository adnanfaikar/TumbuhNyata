package com.example.tumbuhnyata.ui.components

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
        BottomNavItem("Profil", "profile", R.drawable.ic_unprofile, R.drawable.ic_profile)
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
