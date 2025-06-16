package com.example.tumbuhnyata.ui.component

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ErrorSnackbar(
    error: String?,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    error?.let {
        LaunchedEffect(error) {
            val result = snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "Coba Lagi",
                duration = SnackbarDuration.Long
            )
            when (result) {
                SnackbarResult.ActionPerformed -> onRetry()
                SnackbarResult.Dismissed -> onDismiss()
            }
        }
    }
    
    SnackbarHost(hostState = snackbarHostState)
}

fun showErrorSnackbar(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String = "Coba Lagi",
    onAction: () -> Unit = {}
) {
    scope.launch {
        val result = snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = SnackbarDuration.Long
        )
        if (result == SnackbarResult.ActionPerformed) {
            onAction()
        }
    }
} 