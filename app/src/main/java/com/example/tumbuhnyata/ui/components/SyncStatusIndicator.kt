package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SyncStatusIndicator(
    unsyncedCount: Int,
    isSyncing: Boolean,
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (unsyncedCount > 0 || isSyncing) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSyncing) Color(0xFFFFF3CD) else Color(0xFFE3F2FD)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isSyncing) Icons.Default.Sync else Icons.Default.CloudOff,
                        contentDescription = null,
                        tint = if (isSyncing) Color(0xFF856404) else Color(0xFF1976D2),
                        modifier = Modifier.size(20.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Column {
                        Text(
                            text = if (isSyncing) "Menyinkronkan..." else "Data Belum Tersinkron",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSyncing) Color(0xFF856404) else Color(0xFF1976D2)
                        )
                        
                        if (!isSyncing && unsyncedCount > 0) {
                            Text(
                                text = "$unsyncedCount item belum tersinkron",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
                
                if (!isSyncing) {
                    TextButton(
                        onClick = onSyncClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF1976D2)
                        )
                    ) {
                        Text(
                            text = "SINKRON",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFF856404)
                    )
                }
            }
        }
    }
}

@Composable
fun OfflineModeIndicator(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8D7DA)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = null,
                tint = Color(0xFF721C24),
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "Mode Offline - Menampilkan data lokal",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF721C24)
            )
        }
    }
} 