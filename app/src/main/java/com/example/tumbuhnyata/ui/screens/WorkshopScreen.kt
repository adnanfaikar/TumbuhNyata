package com.example.tumbuhnyata.ui.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.tumbuhnyata.ui.components.BottomNavigationBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tumbuhnyata.ui.components.SectionWithTitle
import com.example.tumbuhnyata.ui.components.WorkshopCard
import com.example.tumbuhnyata.ui.components.WorkshopListItem
import com.example.tumbuhnyata.ui.viewmodel.WorkshopViewModel

@Composable
fun WorkshopScreen(
    navController: NavController,
    viewModel: WorkshopViewModel = viewModel()
) {
    val recommended by viewModel.recommended
    val recent by viewModel.recent

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SectionWithTitle(
                title = "Rekomendasi",
                onSeeAllClick = { navController.navigate("rekomendasiworkshop") }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recommended) { workshop ->
                    WorkshopCard(workshop = workshop) {
                        navController.navigate("deskripsiworkshop/${workshop.id}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionWithTitle(
                title = "Terbaru",
                onSeeAllClick = { navController.navigate("workshopterbaru") }
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                recent.forEach { workshop ->
                    WorkshopListItem(
                        workshop = workshop,
                        onClick = { navController.navigate("deskripsiworkshop/${workshop.id}") }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewWorkshopScreen() {
    WorkshopScreen(navController = rememberNavController())
}