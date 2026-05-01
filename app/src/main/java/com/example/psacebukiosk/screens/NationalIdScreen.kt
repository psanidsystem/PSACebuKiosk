package com.example.psacebukiosk.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.psacebukiosk.components.FooterBar
import com.example.psacebukiosk.components.HighlightButton
import com.example.psacebukiosk.components.InfoCard
import com.example.psacebukiosk.components.SectionHeader
import com.example.psacebukiosk.components.MenuButton
import com.example.psacebukiosk.ui.theme.BackgroundGray

@Composable
fun NationalIdScreen(onBackClick: () -> Unit) {
    Scaffold(bottomBar = { FooterBar() }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(20.dp)
        ) {
            TextButton(onClick = onBackClick) { Text("Back") }
            SectionHeader("National ID Services")
            Spacer(modifier = Modifier.height(16.dp))
            HighlightButton("Book Appointment") {}
            Spacer(modifier = Modifier.height(12.dp))
            MenuButton("Check Appointment") {}
            Spacer(modifier = Modifier.height(12.dp))
            MenuButton("Requirements") {}
            Spacer(modifier = Modifier.height(12.dp))
            InfoCard("Preparation Guide", "Bring valid ID and required supporting documents before your scheduled visit.")
        }
    }
}
