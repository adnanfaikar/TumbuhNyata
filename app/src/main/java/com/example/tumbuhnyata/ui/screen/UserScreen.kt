package com.example.tumbuhnyata.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tumbuhnyata.ui.viewmodel.UserViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun UserScreen() {
    val userViewModel: UserViewModel = viewModel()
    val users by userViewModel.users.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.fetchUsers()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "User List", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(10.dp))

        if (users.isEmpty()) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(users) { user ->
                    Text(text = "${user.id}: ${user.name}")
                }
            }
        }
    }
}