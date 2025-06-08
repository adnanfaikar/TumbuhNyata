package com.example.tumbuhnyata.ui.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tumbuhnyata.data.model.Notification
import com.example.tumbuhnyata.viewmodel.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    userId: String,
    onBackClick: () -> Unit = {}
) {
    // Menggunakan viewModel dengan factory
    val viewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModel.Factory()
    )
    
    // Menggunakan collectAsState untuk mengamati StateFlow dari ViewModel
    val notifications by viewModel.notifications.collectAsState()
    val errorMessage by viewModel.error.collectAsState()
    
    // Effect untuk mengambil notifikasi saat tampilan dibuat
    LaunchedEffect(key1 = userId) {
        viewModel.getNotifications(userId)
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Notifikasi", fontFamily = PoppinsFontFamily) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (notifications.isEmpty()) {
                EmptyNotifications(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                NotificationList(
                    notifications = notifications,
                    onNotificationClick = { notificationId ->
                        viewModel.markAsRead(notificationId)
                    },
                    onDeleteClick = { notificationId ->
                        viewModel.deleteNotification(notificationId)
                    }
                )
            }
            
            errorMessage?.let {
                ErrorMessage(
                    message = it,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun NotificationList(
    notifications: List<Notification>,
    onNotificationClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(notifications) { notification ->
            NotificationItem(
                notification = notification,
                onClick = { onNotificationClick(notification.id) },
                onDeleteClick = { onDeleteClick(notification.id) }
            )
        }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val backgroundColor = if (notification.isReadBool()) {
        Color(0xFFEEEEEE)
    } else {
        Color(0xFFE1F5FE)
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF263238),
                    fontFamily = PoppinsFontFamily
                )
                
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus notifikasi",
                        tint = Color(0xFFE57373)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color(0xFF37474F),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontFamily = PoppinsFontFamily
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = formatDate(notification.createdAt),
                fontSize = 12.sp,
                color = Color(0xFF78909C),
                fontFamily = PoppinsFontFamily
            )
        }
    }
}

@Composable
fun EmptyNotifications(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tidak ada notifikasi",
            fontSize = 16.sp,
            color = Color(0xFF78909C),
            fontFamily = PoppinsFontFamily
        )
    }
}

@Composable
fun ErrorMessage(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = PoppinsFontFamily
        )
    }
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString)
        
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id"))
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
} 

