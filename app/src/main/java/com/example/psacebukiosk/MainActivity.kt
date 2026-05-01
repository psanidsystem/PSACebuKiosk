package com.example.psacebukiosk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.psacebukiosk.navigation.AppNavigation
import com.example.psacebukiosk.ui.theme.PSACebuKioskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PSACebuKioskTheme {
                AppNavigation()
            }
        }
    }
}
