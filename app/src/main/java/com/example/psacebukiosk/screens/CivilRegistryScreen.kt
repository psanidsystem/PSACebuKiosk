package com.example.psacebukiosk.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.example.psacebukiosk.components.InfoCard
import com.example.psacebukiosk.components.SectionHeader
import com.example.psacebukiosk.ui.theme.BackgroundGray

@Composable
fun CivilRegistryScreen(onBackClick: () -> Unit) {
    Scaffold(bottomBar = { FooterBar() }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            TextButton(onClick = onBackClick) { Text("Back") }
            SectionHeader("Civil Registry Services")
            Spacer(modifier = Modifier.height(16.dp))
            InfoCard("Birth Certificate", "Requirements, fees, and steps for requesting a birth certificate.")
            Spacer(modifier = Modifier.height(12.dp))
            InfoCard("Marriage Certificate", "Guidance for marriage certificate requests and who may request.")
            Spacer(modifier = Modifier.height(12.dp))
            InfoCard("Death Certificate", "Steps and requirements for death certificate transactions.")
            Spacer(modifier = Modifier.height(12.dp))
            InfoCard("CENOMAR", "Transaction guide for Certificate of No Marriage Record.")
        }
    }
}
