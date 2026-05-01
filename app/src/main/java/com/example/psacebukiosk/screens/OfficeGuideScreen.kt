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
import com.example.psacebukiosk.components.InfoCard
import com.example.psacebukiosk.components.SectionHeader
import com.example.psacebukiosk.ui.theme.BackgroundGray

@Composable
fun OfficeGuideScreen(onBackClick: () -> Unit) {
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
            SectionHeader("Office Guide")
            Spacer(modifier = Modifier.height(16.dp))
            InfoCard("Where to Go", "Civil Registry to Window 1-4, National ID to Area B, Releasing to Window 6.")
            Spacer(modifier = Modifier.height(12.dp))
            InfoCard("Priority Lane", "Senior citizens, PWDs, and pregnant clients may use the priority lane.")
            Spacer(modifier = Modifier.height(12.dp))
            InfoCard("Process Flow", "Get your queue number, prepare documents, then proceed to the assigned window.")
        }
    }
}
