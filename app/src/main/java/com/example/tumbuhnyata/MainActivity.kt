package com.example.tumbuhnyata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tumbuhnyata.navigation.AppNavigation
import com.example.tumbuhnyata.ui.theme.TumbuhNyataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TumbuhNyataTheme {
                AppNavigation()
            }
        }
    }
}
