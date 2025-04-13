package com.example.tumbuhnyata.ui.dashboard.upload.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.InsertDriveFile // Icon for selected file
import androidx.compose.material.icons.filled.UploadFile // Icon for upload action
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DocumentUploadItem(
    modifier: Modifier = Modifier,
    label: String,
    placeholderText: String,
    selectedFileName: String?, // Null if no file selected, filename otherwise
    onSelectClick: () -> Unit, // Action to trigger file selection
    onRemoveClick: () -> Unit // Action to remove selected file
) {
    val shape = RoundedCornerShape(8.dp)
    val hasFileSelected = selectedFileName != null

    Column(modifier = modifier) {
        // Label Text (e.g., "Laporan CSR")
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Clickable Upload Area Box
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Fixed height for the box
                .clip(shape) // Apply clipping before border and clickable
                .border( // Apply border
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = shape
                )
                .clickable(
                    enabled = !hasFileSelected, // Only clickable if no file is selected
                    onClick = onSelectClick
                ),
            shape = shape,
            color = Color.Transparent // Make surface transparent, border provides outline
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(), // Fill the Surface
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween // Space out content
            ) {
                if (hasFileSelected) {
                    // --- State when file IS selected ---
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f) // Allow text to take space and ellipsize
                    ) {
                        Icon(
                            imageVector = Icons.Filled.InsertDriveFile,
                            contentDescription = null,
                            tint = Color(0xFF686868),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = selectedFileName ?: "", // Should not be null here, but safe call
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    // Remove Button
                    IconButton(
                        onClick = onRemoveClick,
                        modifier = Modifier.size(24.dp) // Smaller touch target is okay here
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Remove File",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    // --- State when NO file is selected ---
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.UploadFile,
                            contentDescription = null,
                            tint = Color(0xFFB9B9B9), // Use primary color for icon
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = placeholderText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant // Dimmed placeholder color
                        )
                    }
                    // No remove button needed here
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun DocumentUploadItemPreview_Empty() {
    Column(Modifier.padding(16.dp)) {
        DocumentUploadItem(
            label = "Laporan CSR",
            placeholderText = "Laporan",
            selectedFileName = null,
            onSelectClick = {},
            onRemoveClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun DocumentUploadItemPreview_Selected() {
    Column(Modifier.padding(16.dp)) {
        DocumentUploadItem(
            label = "Sertifikasi CSR",
            placeholderText = "Sertifikasi",
            selectedFileName = "Sertifikasi ISO 9001-2024 Rev B Long Name.pdf",
            onSelectClick = {},
            onRemoveClick = {}
        )
    }
}
