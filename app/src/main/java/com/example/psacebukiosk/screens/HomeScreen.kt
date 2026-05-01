package com.example.psacebukiosk.screens

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.request.ImageRequest
import coil.compose.AsyncImage
import com.example.psacebukiosk.R
import com.example.psacebukiosk.ui.theme.PsaBlue
import com.example.psacebukiosk.ui.theme.PsaGold
import com.example.psacebukiosk.ui.theme.PsaMist
import com.example.psacebukiosk.ui.theme.PsaNavy
import com.example.psacebukiosk.ui.theme.PsaRed
import com.example.psacebukiosk.ui.theme.PsaSky
import com.example.psacebukiosk.ui.theme.PsaSurface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DEFAULT_ANNOUNCEMENT_DRIVE_FOLDER_URL =
    "https://drive.google.com/drive/folders/1GPI_4AdKZaeVHTOvk-QKQGxIibLx87rm?usp=sharing"

private const val CITIZENS_CHARTER_DRIVE_FOLDER_URL =
    "https://drive.google.com/drive/folders/1tlgpx3ytCCPa8hvIQFunC-1ADFfC6IWT?usp=sharing"

private data class ServiceSection(
    val title: String,
    val items: List<String>
)

private data class DrivePdfDocument(
    val fileId: String,
    val name: String
) {
    val previewUrl: String
        get() = "https://drive.google.com/file/d/$fileId/preview"
}

private data class DriveAnnouncementMedia(
    val url: String,
    val mimeType: String,
    val name: String
)

private data class DashboardService(
    val title: String,
    val subtitle: String,
    val badge: String,
    val icon: ImageVector,
    val accentColor: Color,
    val overview: String,
    val sections: List<ServiceSection>,
    val footerNote: String
)

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val services = listOf(
        DashboardService(
            title = "Civil Registration",
            subtitle = "Birth, marriage, death, and CENOMAR services.",
            badge = "CR",
            icon = Icons.Outlined.Description,
            accentColor = PsaBlue,
            overview = "This service helps visitors review the main civil registration transactions and prepare the basic documentary requirements before proceeding to the correct transaction window.",
            sections = listOf(
                ServiceSection(
                    title = "Available Services",
                    items = listOf(
                        "Birth Certificate",
                        "Marriage Certificate",
                        "Death Certificate",
                        "CENOMAR request"
                    )
                ),
                ServiceSection(
                    title = "Before You Proceed",
                    items = listOf(
                        "Prepare valid identification",
                        "Confirm the exact record or certificate needed",
                        "Review required supporting documents"
                    )
                )
            ),
            footerNote = "Use this module for certificate and registry-related concerns."
        ),
        DashboardService(
            title = "National ID System",
            subtitle = "PhilSys appointments, registration guidance, and reminders.",
            badge = "NID",
            icon = Icons.Outlined.Badge,
            accentColor = PsaGold,
            overview = "This service helps visitors understand National ID registration steps, appointment guidance, and the basic reminders needed before visiting the assigned assistance point.",
            sections = listOf(
                ServiceSection(
                    title = "Available Information",
                    items = listOf(
                        "Appointment booking guidance",
                        "Appointment checking process",
                        "Basic registration requirements",
                        "Preparation reminders before visiting the office"
                    )
                ),
                ServiceSection(
                    title = "What To Prepare",
                    items = listOf(
                        "Bring the required supporting documents",
                        "Review the schedule before arriving",
                        "Check any updated advisories or temporary service changes"
                    )
                ),
                ServiceSection(
                    title = "Suggested Next Step",
                    items = listOf(
                        "Proceed to the National ID help desk if you need staff assistance"
                    )
                )
            ),
            footerNote = "Use this module as the main kiosk reference for PhilSys concerns."
        ),
        DashboardService(
            title = "Statistical",
            subtitle = "Data highlights, office facts, and reference information.",
            badge = "STAT",
            icon = Icons.Outlined.Insights,
            accentColor = PsaRed,
            overview = "This service provides quick statistical highlights, office information, and simple public references that can be viewed while waiting.",
            sections = listOf(
                ServiceSection(
                    title = "Possible Content",
                    items = listOf(
                        "Key statistical highlights",
                        "Infographic summaries",
                        "Office quick facts",
                        "Public information cards"
                    )
                ),
                ServiceSection(
                    title = "Display Style",
                    items = listOf(
                        "Short public summaries",
                        "Visual statistics cards",
                        "Easy-to-read waiting area information"
                    )
                )
            ),
            footerNote = "This module works well for educational and passive visitor content."
        ),
        DashboardService(
            title = "Admin Support",
            subtitle = "Front desk guidance and internal visitor assistance references.",
            badge = "ADM",
            icon = Icons.Outlined.SupportAgent,
            accentColor = PsaSky,
            overview = "This service is designed for support-related concerns such as visitor assistance, queue direction, help desk escalation, and basic administrative guidance.",
            sections = listOf(
                ServiceSection(
                    title = "Support Coverage",
                    items = listOf(
                        "Front desk assistance guidance",
                        "Queue direction support",
                        "Referral to proper units",
                        "Basic administrative reminders"
                    )
                ),
                ServiceSection(
                    title = "Best Use",
                    items = listOf(
                        "Visitors needing help before choosing a service",
                        "Clients needing direction from screening to transaction windows",
                        "Support staff assistance references"
                    )
                )
            ),
            footerNote = "Use this module as a support layer for general kiosk assistance."
        ),
        DashboardService(
            title = "Bulletin",
            subtitle = "Latest advisories, notices, and office announcements.",
            badge = "BLT",
            icon = Icons.Outlined.Campaign,
            accentColor = PsaSky,
            overview = "This service serves as a quick bulletin board for notices that affect visitors, including schedule advisories, system interruptions, and office updates.",
            sections = listOf(
                ServiceSection(
                    title = "Typical Posts",
                    items = listOf(
                        "Holiday schedules",
                        "System downtime notices",
                        "Service advisories",
                        "Latest PSA Cebu updates"
                    )
                ),
                ServiceSection(
                    title = "Best Use",
                    items = listOf(
                        "Check this screen before taking a queue number",
                        "Look here for same-day operational updates",
                        "Use this area for time-sensitive announcements"
                    )
                )
            ),
            footerNote = "This module is best positioned as a live advisory wall for kiosk visitors."
        ),
        DashboardService(
            title = "Office Guide",
            subtitle = "Find the right window, lane, and process flow quickly.",
            badge = "OG",
            icon = Icons.Outlined.Map,
            accentColor = PsaBlue,
            overview = "This service helps visitors move around the office more confidently by showing where to go, who can use priority lanes, and how the general process flows inside the site.",
            sections = listOf(
                ServiceSection(
                    title = "Guide Content",
                    items = listOf(
                        "Assigned windows or counters",
                        "Priority lane guidance",
                        "Step-by-step process flow",
                        "Where to ask for assistance"
                    )
                ),
                ServiceSection(
                    title = "Helpful For",
                    items = listOf(
                        "First-time visitors",
                        "Visitors unsure which service point to approach",
                        "Clients needing direction after screening"
                    )
                )
            ),
            footerNote = "This module can later connect to an illustrated office map or floor layout."
        ),
        DashboardService(
            title = "Citizens Charter",
            subtitle = "PDF service standards, requirements, fees, and timelines.",
            badge = "CC",
            icon = Icons.Outlined.Gavel,
            accentColor = PsaRed,
            overview = "This service automatically loads Citizens Charter PDF files from the official shared folder so visitors can browse requirements, fees, timelines, and service standards directly in the kiosk.",
            sections = listOf(
                ServiceSection(
                    title = "What You Can Review",
                    items = listOf(
                        "PDF Citizens Charter files",
                        "Processing time and fees",
                        "Documentary requirements",
                        "Responsible office or service point"
                    )
                ),
                ServiceSection(
                    title = "How It Works",
                    items = listOf(
                        "Files are detected from the linked public Google Drive folder",
                        "Clients select a PDF from the list",
                        "The selected document opens in the in-app preview area"
                    )
                )
            ),
            footerNote = "This module is connected to the Citizens Charter PDF folder."
        ),
        DashboardService(
            title = "Organizational Structure",
            subtitle = "Office units, reporting structure, and assigned sections.",
            badge = "ORG",
            icon = Icons.Outlined.Groups,
            accentColor = PsaBlue,
            overview = "This service presents the office structure in a simple public-facing format so visitors can understand the main divisions, units, and reporting arrangement of the office.",
            sections = listOf(
                ServiceSection(
                    title = "Suggested Content",
                    items = listOf(
                        "Main office divisions",
                        "Assigned sections and focal persons",
                        "Organizational chart",
                        "Unit responsibilities"
                    )
                ),
                ServiceSection(
                    title = "Best Use",
                    items = listOf(
                        "Visitors asking which unit handles a concern",
                        "Orientation and public information",
                        "Internal office structure display"
                    )
                )
            ),
            footerNote = "This module can later connect to a full organizational chart graphic."
        ),
        DashboardService(
            title = "Employee Locator Chart",
            subtitle = "Find assigned personnel, sections, and office points.",
            badge = "ELC",
            icon = Icons.Outlined.Badge,
            accentColor = PsaSky,
            overview = "This service helps visitors locate employees, assigned sections, focal persons, and office service points in a kiosk-friendly format.",
            sections = listOf(
                ServiceSection(
                    title = "Suggested Content",
                    items = listOf(
                        "Employee locator chart",
                        "Assigned office or section",
                        "Service focal person",
                        "Where to proceed for assistance"
                    )
                ),
                ServiceSection(
                    title = "Best Use",
                    items = listOf(
                        "Visitors looking for the right personnel",
                        "Clients asking where a section is located",
                        "Front desk support and internal guidance"
                    )
                )
            ),
            footerNote = "This module can later connect to a staff directory or locator chart image."
        ),
        DashboardService(
            title = "About Us",
            subtitle = "Office profile, mission, and public-facing background.",
            badge = "INFO",
            icon = Icons.Outlined.Info,
            accentColor = PsaGold,
            overview = "This section presents the Philippine Statistics Authority mandate, core functions, vision, mission, logo meaning, quality policy, and organizational values in a more formal public-facing format.",
            sections = listOf(
                ServiceSection(
                    title = "Profile Highlights",
                    items = listOf(
                        "Mandate and legal basis",
                        "Core functions and official responsibilities",
                        "Vision, mission, and quality policy",
                        "Core values and corporate personality"
                    )
                ),
                ServiceSection(
                    title = "Display Use",
                    items = listOf(
                        "General public orientation",
                        "Visitors learning about the office",
                        "Institutional and identity-focused kiosk information"
                    )
                )
            ),
            footerNote = "This module is designed as a formal PSA profile and public information showcase."
        )
    )
    val primaryServices = services.take(4)
    val secondaryServices = services.drop(4)
    var selectedService by remember { mutableStateOf<DashboardService?>(null) }
    var bannerMediaUri by rememberSaveable { mutableStateOf<String?>(null) }
    var bannerDriveUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var folderBannerUri by rememberSaveable { mutableStateOf<String?>(null) }
    var driveFolderMediaItems by remember { mutableStateOf<List<DriveAnnouncementMedia>>(emptyList()) }
    var showDriveDialog by rememberSaveable { mutableStateOf(false) }
    var showPrivacySealDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        folderBannerUri = loadAnnouncementBannerFromFolder(context)
        if (folderBannerUri == null) {
            driveFolderMediaItems =
                loadAnnouncementMediaFromDriveFolder(DEFAULT_ANNOUNCEMENT_DRIVE_FOLDER_URL)
        }
    }

    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        bannerMediaUri = uri?.toString()
        if (uri != null) {
            bannerDriveUrl = null
        }
    }

    Scaffold(containerColor = Color.Transparent) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0B2F59),
                            PsaNavy,
                            Color(0xFF071E37)
                        )
                    )
                )
        ) {
            DashboardBackdrop()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(16.dp)
            ) {
                DashboardReveal(delayMillis = 0) {
                    KioskHeader()
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1.6f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        primaryServices.chunked(2).forEachIndexed { rowIndex, row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                row.forEachIndexed { columnIndex, service ->
                                    DashboardReveal(
                                        delayMillis = 90 * (rowIndex * 2 + columnIndex + 1),
                                        modifier = Modifier.weight(1f),
                                    ) {
                                        KioskPrimaryCard(
                                            service = service,
                                            modifier = Modifier.fillMaxWidth(),
                                            onClick = { selectedService = service }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    DashboardReveal(
                        delayMillis = 520,
                        modifier = Modifier
                            .weight(1f)
                            .height(270.dp)
                    ) {
                        DataPrivacySealPanel(
                            modifier = Modifier.fillMaxSize(),
                            onOpenSeal = { showPrivacySealDialog = true }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    secondaryServices.chunked(3).forEachIndexed { rowIndex, row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            row.forEachIndexed { columnIndex, service ->
                                DashboardReveal(
                                    delayMillis = 620 + 70 * (rowIndex * 3 + columnIndex),
                                    modifier = Modifier.weight(1f),
                                ) {
                                    KioskSecondaryCard(
                                        service = service,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { selectedService = service }
                                    )
                                }
                            }
                            repeat(3 - row.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                DashboardReveal(delayMillis = 980) {
                    MediaBannerSection(
                        bannerMediaUri = bannerMediaUri,
                        bannerDriveUrl = bannerDriveUrl,
                        folderBannerUri = folderBannerUri,
                        driveFolderMediaItems = driveFolderMediaItems,
                        onLoadMediaClick = {
                            mediaPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onGoogleDriveClick = { showDriveDialog = true },
                        onRefreshFolderClick = {
                            folderBannerUri = loadAnnouncementBannerFromFolder(context)
                        },
                        onSyncDriveClick = {
                            scope.launch {
                                driveFolderMediaItems =
                                    loadAnnouncementMediaFromDriveFolder(DEFAULT_ANNOUNCEMENT_DRIVE_FOLDER_URL)
                            }
                        }
                    )
                }
            }
        }
    }

    selectedService?.let { service ->
        ServiceInfoDialog(
            service = service,
            onDismiss = { selectedService = null }
        )
    }

    if (showPrivacySealDialog) {
        DataPrivacySealDialog(
            onDismiss = { showPrivacySealDialog = false }
        )
    }

    if (showDriveDialog) {
        GoogleDriveBannerDialog(
            initialValue = bannerDriveUrl.orEmpty(),
            onDismiss = { showDriveDialog = false },
            onConfirm = { rawUrl ->
                bannerDriveUrl = normalizeDriveBannerUrl(rawUrl)
                bannerMediaUri = null
                showDriveDialog = false
            }
        )
    }
}

@Composable
private fun DashboardReveal(
    delayMillis: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        visible = true
    }

    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 520)) +
                slideInVertically(animationSpec = tween(durationMillis = 520)) { distance ->
                    distance / 5
                }
        ) {
            content()
        }
    }
}

@Composable
private fun KioskHeader() {
    val now by produceState(initialValue = Date()) {
        while (true) {
            value = Date()
            delay(1000)
        }
    }
    val timeText = remember(now) {
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(now)
    }
    val dateText = remember(now) {
        SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(now)
    }

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = Color.White.copy(alpha = 0.10f),
        tonalElevation = 0.dp,
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(96.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.92f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.psa_logo),
                    contentDescription = "Philippine Statistics Authority logo",
                    modifier = Modifier.padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp, end = 12.dp)
            ) {
                Text(
                    text = "Welcome to PSA Cebu Provincial Statistical Office",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.82f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun KioskPrimaryCard(
    service: DashboardService,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.06f),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White.copy(alpha = 0.32f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(service.accentColor.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = service.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.White.copy(alpha = 0.10f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = service.badge,
                        color = Color.White.copy(alpha = 0.92f),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = service.title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun KioskSecondaryCard(
    service: DashboardService,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFFF7FAFE),
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(132.dp)
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(service.accentColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = service.icon,
                        contentDescription = null,
                        tint = service.accentColor,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(service.accentColor.copy(alpha = 0.10f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = service.badge,
                        color = service.accentColor,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column {
                Text(
                    text = service.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = PsaNavy,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DataPrivacySealPanel(
    modifier: Modifier = Modifier,
    onOpenSeal: () -> Unit
) {
    Surface(
        modifier = modifier.combinedClickable(
            onClick = onOpenSeal,
            onLongClick = onOpenSeal
        ),
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.06f),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = PsaSky.copy(alpha = 0.26f),
                    shape = RoundedCornerShape(24.dp)
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.10f),
                            PsaBlue.copy(alpha = 0.08f),
                            PsaNavy.copy(alpha = 0.12f)
                        )
                    )
                )
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color.White.copy(alpha = 0.13f)
            ) {
                Text(
                    text = "Data Privacy",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.82f),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.data_privacy_seal),
                    contentDescription = "Data Privacy Seal",
                    modifier = Modifier
                        .fillMaxWidth(0.64f)
                        .fillMaxHeight(0.88f),
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                text = "NPC Registered",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.68f),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DataPrivacySealDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.62f))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .fillMaxHeight(0.92f),
                shape = RoundedCornerShape(32.dp),
                color = Color(0xFFF8FBFF),
                shadowElevation = 18.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 1.dp,
                            color = PsaSky.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(32.dp)
                        )
                        .padding(18.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Data Privacy Seal",
                            style = MaterialTheme.typography.titleLarge,
                            color = PsaNavy,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Image(
                            painter = painterResource(id = R.drawable.data_privacy_seal),
                            contentDescription = "Data Privacy Seal enlarged view",
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = "Close",
                            color = PsaBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaBannerSection(
    bannerMediaUri: String?,
    bannerDriveUrl: String?,
    folderBannerUri: String?,
    driveFolderMediaItems: List<DriveAnnouncementMedia>,
    onLoadMediaClick: () -> Unit,
    onGoogleDriveClick: () -> Unit,
    onRefreshFolderClick: () -> Unit,
    onSyncDriveClick: () -> Unit
) {
    val announcementItems = remember(
        bannerMediaUri,
        bannerDriveUrl,
        folderBannerUri,
        driveFolderMediaItems
    ) {
        when {
            bannerMediaUri != null -> listOf(
                DriveAnnouncementMedia(
                    url = bannerMediaUri,
                    mimeType = "image/*",
                    name = "Selected media"
                )
            )
            bannerDriveUrl != null -> listOf(
                DriveAnnouncementMedia(
                    url = bannerDriveUrl,
                    mimeType = "image/*",
                    name = "Google Drive banner"
                )
            )
            folderBannerUri != null -> listOf(
                DriveAnnouncementMedia(
                    url = folderBannerUri,
                    mimeType = "image/*",
                    name = "Folder banner"
                )
            )
            driveFolderMediaItems.isNotEmpty() -> driveFolderMediaItems
            else -> listOf(
                DriveAnnouncementMedia(
                    url = "android.resource://com.example.psacebukiosk/${R.drawable.sample_banner}",
                    mimeType = "image/*",
                    name = "Default banner"
                )
            )
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.08f),
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.05f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Color.White.copy(alpha = 0.12f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        BannerActionButton(
                            label = "Sync Drive",
                            onClick = onSyncDriveClick
                        )
                        BannerActionButton(
                            label = "Refresh Folder",
                            onClick = onRefreshFolderClick
                        )
                        BannerActionButton(
                            label = "Load Media",
                            onClick = onLoadMediaClick
                        )
                        BannerActionButton(
                            label = "Google Drive",
                            onClick = onGoogleDriveClick
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                shape = RoundedCornerShape(20.dp),
                color = Color.Transparent,
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    PsaGold.copy(alpha = 0.65f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0B3A6B))
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF0B3A6B),
                                        PsaBlue,
                                        Color(0xFF1769B7)
                                    )
                                )
                            )
                    )

                    AnnouncementCarousel(
                        mediaItems = announcementItems,
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.02f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.05f)
                                    )
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(6.dp)
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(16.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun AnnouncementCarousel(
    mediaItems: List<DriveAnnouncementMedia>,
    modifier: Modifier = Modifier
) {
    val safeItems = if (mediaItems.isEmpty()) {
        listOf(
            DriveAnnouncementMedia(
                url = "android.resource://com.example.psacebukiosk/${R.drawable.sample_banner}",
                mimeType = "image/*",
                name = "Default banner"
            )
        )
    } else {
        mediaItems
    }
    var currentIndex by remember(safeItems) { mutableStateOf(0) }
    val currentItem = safeItems[currentIndex]

    LaunchedEffect(safeItems, currentIndex) {
        if (safeItems.size > 1) {
            val displayDurationMs = if (currentItem.mimeType.startsWith("video/")) 18000L else 9000L
            delay(displayDurationMs)
            currentIndex = (currentIndex + 1) % safeItems.size
        }
    }

    Crossfade(
        targetState = currentItem,
        animationSpec = tween(durationMillis = 950),
        modifier = modifier,
        label = "announcement-carousel"
    ) { item ->
        if (item.mimeType.startsWith("video/")) {
            AnnouncementVideoPlayer(
                url = item.url,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.url)
                    .crossfade(450)
                    .build(),
                contentDescription = item.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
private fun BannerActionButton(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = Color.White.copy(alpha = 0.16f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.24f))
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Color.White
        )
    }
}

@Composable
private fun GoogleDriveBannerDialog(
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var value by rememberSaveable { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Google Drive Banner") },
        text = {
            Column {
                Text(
                    text = "Paste a public Google Drive image link or a direct image URL.",
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    singleLine = true,
                    label = { Text("Drive or image URL") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(value.trim()) }) {
                Text("Use Banner")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun normalizeDriveBannerUrl(input: String): String {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return trimmed

    val filePattern = Regex("""/file/d/([a-zA-Z0-9_-]+)""")
    val openIdPattern = Regex("""[?&]id=([a-zA-Z0-9_-]+)""")

    val fileId = filePattern.find(trimmed)?.groupValues?.get(1)
        ?: openIdPattern.find(trimmed)?.groupValues?.get(1)

    return if (fileId != null) {
        "https://drive.google.com/uc?export=download&id=$fileId"
    } else {
        trimmed
    }
}

private fun announcementStorageFolder(context: Context): File {
    val root = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir
    return File(root, "announcements").apply {
        if (!exists()) {
            mkdirs()
        }
    }
}

private fun loadAnnouncementBannerFromFolder(context: Context): String? {
    return announcementStorageFolder(context)
        .listFiles()
        ?.asSequence()
        ?.filter { file ->
            file.isFile && file.extension.lowercase(Locale.ROOT) in setOf("png", "jpg", "jpeg", "webp")
        }
        ?.sortedByDescending { it.lastModified() }
        ?.firstOrNull()
        ?.let { Uri.fromFile(it).toString() }
}

private suspend fun loadAnnouncementMediaFromDriveFolder(folderUrl: String): List<DriveAnnouncementMedia> {
    return withContext(Dispatchers.IO) {
        runCatching {
            val folderId = extractDriveFolderId(folderUrl) ?: return@runCatching null
            val html = java.net.URL(folderUrl).readText()
            val itemPattern = Regex(
                """\\x22([A-Za-z0-9_-]{20,})\\x22,\\x5b\\x22$folderId\\x22\\x5d,\\x22(.+?)\\x22,\\x22(.+?)\\x22"""
            )

            itemPattern.findAll(html)
                .mapNotNull { match ->
                    val fileId = match.groupValues[1]
                    val name = decodeDriveEscapes(match.groupValues[2])
                    val mimeType = decodeDriveEscapes(match.groupValues[3])
                    if (!mimeType.startsWith("image/") && !mimeType.startsWith("video/")) {
                        null
                    } else {
                        DriveAnnouncementMedia(
                            url = "https://drive.google.com/uc?export=download&id=$fileId",
                            mimeType = mimeType,
                            name = name
                        )
                    }
                }
                .distinctBy { it.url }
                .toList()
        }.getOrNull().orEmpty()
    }
}

private suspend fun loadPdfDocumentsFromDriveFolder(folderUrl: String): List<DrivePdfDocument> {
    return withContext(Dispatchers.IO) {
        runCatching {
            val folderId = extractDriveFolderId(folderUrl) ?: return@runCatching null
            val html = java.net.URL(folderUrl).readText()
            val itemPattern = Regex(
                """\\x22([A-Za-z0-9_-]{20,})\\x22,\\x5b\\x22$folderId\\x22\\x5d,\\x22(.+?)\\x22,\\x22(.+?)\\x22"""
            )

            itemPattern.findAll(html)
                .mapNotNull { match ->
                    val fileId = match.groupValues[1]
                    val name = decodeDriveEscapes(match.groupValues[2])
                    val mimeType = decodeDriveEscapes(match.groupValues[3])
                    if (mimeType == "application/pdf" || name.endsWith(".pdf", ignoreCase = true)) {
                        DrivePdfDocument(
                            fileId = fileId,
                            name = name
                        )
                    } else {
                        null
                    }
                }
                .distinctBy { it.fileId }
                .sortedBy { it.name.lowercase(Locale.ROOT) }
                .toList()
        }.getOrNull().orEmpty()
    }
}

private fun extractDriveFolderId(url: String): String? {
    val folderPattern = Regex("""/folders/([a-zA-Z0-9_-]+)""")
    val idPattern = Regex("""[?&]id=([a-zA-Z0-9_-]+)""")
    return folderPattern.find(url)?.groupValues?.get(1)
        ?: idPattern.find(url)?.groupValues?.get(1)
}

private fun decodeDriveEscapes(value: String): String {
    return value
        .replace("\\/", "/")
        .replace("\\u003d", "=")
        .replace("\\u0026", "&")
}

@Composable
private fun AnnouncementVideoPlayer(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val player = remember(url) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            prepare()
        }
    }

    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { viewContext ->
            PlayerView(viewContext).apply {
                useController = false
                this.player = player
            }
        },
        update = { playerView ->
            playerView.player = player
        }
    )
}

@Composable
private fun CitizensCharterPdfContent(accentColor: Color) {
    var documents by remember { mutableStateOf<List<DrivePdfDocument>>(emptyList()) }
    var selectedDocument by remember { mutableStateOf<DrivePdfDocument?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = null
        val loadedDocuments = loadPdfDocumentsFromDriveFolder(CITIZENS_CHARTER_DRIVE_FOLDER_URL)
        documents = loadedDocuments
        selectedDocument = loadedDocuments.firstOrNull()
        errorMessage = if (loadedDocuments.isEmpty()) {
            "No PDF files were detected in the Citizens Charter folder."
        } else {
            null
        }
        isLoading = false
    }

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = PsaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Citizens Charter PDF Library",
                        style = MaterialTheme.typography.titleLarge,
                        color = PsaBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Auto-detected from the shared Google Drive folder.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PsaNavy.copy(alpha = 0.78f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = accentColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = if (documents.size == 1) "1 PDF" else "${documents.size} PDFs",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    CitizensCharterStatusCard(
                        title = "Loading PDF files",
                        message = "Please wait while the kiosk checks the Citizens Charter Drive folder.",
                        accentColor = accentColor
                    )
                }

                errorMessage != null -> {
                    CitizensCharterStatusCard(
                        title = "No PDF available",
                        message = errorMessage.orEmpty(),
                        accentColor = PsaRed
                    )
                }

                else -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(0.42f)
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            documents.forEach { document ->
                                CitizensCharterPdfItem(
                                    document = document,
                                    isSelected = document.fileId == selectedDocument?.fileId,
                                    accentColor = accentColor,
                                    onClick = { selectedDocument = document }
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(24.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = PsaBlue.copy(alpha = 0.18f)
                            ),
                            shadowElevation = 6.dp
                        ) {
                            selectedDocument?.let { document ->
                                PdfPreviewWebView(
                                    document = document,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CitizensCharterStatusCard(
    title: String,
    message: String,
    accentColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = accentColor.copy(alpha = 0.10f)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = accentColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = PsaNavy.copy(alpha = 0.86f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun CitizensCharterPdfItem(
    document: DrivePdfDocument,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = if (isSelected) PsaBlue else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isSelected) PsaBlue else PsaBlue.copy(alpha = 0.14f)
        ),
        shadowElevation = if (isSelected) 8.dp else 2.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSelected) PsaGold else accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PDF",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) PsaNavy else accentColor,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = document.name.removeSuffix(".pdf").removeSuffix(".PDF"),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isSelected) Color.White else PsaNavy,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isSelected) "Currently displayed" else "Tap to preview",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) Color.White.copy(alpha = 0.78f) else PsaNavy.copy(alpha = 0.62f),
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}

@Composable
private fun PdfPreviewWebView(
    document: DrivePdfDocument,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                isVerticalScrollBarEnabled = true
                isHorizontalScrollBarEnabled = true
                overScrollMode = WebView.OVER_SCROLL_IF_CONTENT_SCROLLS
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                loadUrl(document.previewUrl)
            }
        },
        update = { webView ->
            if (webView.url != document.previewUrl) {
                webView.loadUrl(document.previewUrl)
            }
        }
    )
}

@Composable
private fun ServiceInfoDialog(
    service: DashboardService,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.32f))
                .padding(horizontal = 22.dp, vertical = 26.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f),
                shape = RoundedCornerShape(34.dp),
                color = Color.White,
                tonalElevation = 12.dp,
                shadowElevation = 24.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (service.title == "About Us") PsaSurface else Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        PsaNavy,
                                        service.accentColor.copy(alpha = 0.88f)
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(Color.White.copy(alpha = 0.18f))
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "${service.badge} Service",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.White
                                    )
                                }
                                TextButton(onClick = onDismiss) {
                                    Text("Close", color = Color.White)
                                }
                            }

                            Text(
                                text = service.title,
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                modifier = Modifier.padding(top = 18.dp)
                            )
                            Text(
                                text = service.subtitle,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.88f),
                                modifier = Modifier.padding(top = 10.dp)
                            )

                            Row(
                                modifier = Modifier.padding(top = 18.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                ModalStatPill(
                                    label = "${service.sections.size} sections",
                                    background = Color.White.copy(alpha = 0.14f)
                                )
                                ModalStatPill(
                                    label = "Full details",
                                    background = service.accentColor.copy(alpha = 0.22f)
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .then(
                                if (service.title == "Citizens Charter") {
                                    Modifier
                                } else {
                                    Modifier.verticalScroll(scrollState)
                                }
                            )
                            .padding(22.dp)
                    ) {
                        when (service.title) {
                            "About Us" -> {
                                AboutUsContent()
                            }

                            "Citizens Charter" -> {
                                CitizensCharterPdfContent(accentColor = service.accentColor)
                            }

                            else -> {
                                ModalOverviewCard(
                                    title = "Overview",
                                    content = service.overview,
                                    accentColor = service.accentColor
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                service.sections.forEach { section ->
                                    ModalSectionCard(
                                        section = section,
                                        accentColor = service.accentColor
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                }
                            }
                        }

                        if (service.title != "Citizens Charter") {
                            Spacer(modifier = Modifier.height(14.dp))

                            Card(
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = PsaMist.copy(alpha = 0.95f)
                                )
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Text(
                                        text = "Visitor Note",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = PsaNavy
                                    )
                                    Text(
                                        text = service.footerNote,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = PsaNavy.copy(alpha = 0.82f),
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = PsaMist)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Back to dashboard")
                        }
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Text("Done")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutUsContent() {
    val functions = listOf(
        "Serve as the central statistical authority of the Philippine government on primary data collection.",
        "Prepare and conduct periodic censuses on population, housing, agriculture, fisheries, business, industry, and other sectors of the economy.",
        "Collect, compile, analyze, abstract, and publish statistical information on the country's economic, social, demographic, and general conditions.",
        "Prepare and conduct statistical sample surveys covering the major aspects of socioeconomic life for government and public use.",
        "Carry out, enforce, and administer civil registration functions in the country as provided for in Act 3753.",
        "Collaborate with national government departments, GOCCs, and subsidiaries in the collection, maintenance, and publication of statistical information.",
        "Promote and develop integrated social and economic statistics, including coordination of national accounts.",
        "Develop and maintain frameworks and standards for data collection, processing, analysis, and dissemination.",
        "Coordinate with government departments and LGUs on standards, methodologies, concepts, definitions, and classifications while avoiding duplication of data collection.",
        "Conduct continuing methodological, analytical, and development activities with the PSRTI to improve censuses, surveys, and other data collection work.",
        "Recommend executive and legislative measures that enhance statistical programs and activities of government.",
        "Prepare the Philippine Statistical Development Program in consultation with the PSA Board.",
        "Implement policies on statistical matters and perform other functions assigned by the PSA Board under RA 10625."
    )

    Spacer(modifier = Modifier.height(2.dp))

    AboutHighlightCard(
        eyebrow = "Mandate",
        title = "Official mandate under RA 10625, RA 11055, and RA 11315",
        body = "The Philippine Statistics Authority is primarily responsible for implementing the objectives and provisions of RA 10625, RA 11055, and RA 11315. It plans, develops, prescribes, disseminates, and enforces policies, rules, regulations, and government-wide programs on official statistics, civil registration services, and the inclusive identification system."
    )

    Spacer(modifier = Modifier.height(14.dp))

    AboutHighlightCard(
        eyebrow = "Institutional role",
        title = "National censuses, surveys, statistics, civil registration, and identification",
        body = "The PSA is responsible for all national censuses and surveys, sectoral statistics, community-based statistics, consolidation of selected administrative recording systems, and the compilation of national accounts."
    )

    Spacer(modifier = Modifier.height(16.dp))

    AboutListCard(
        title = "Core functions",
        items = functions
    )

    Spacer(modifier = Modifier.height(16.dp))

    AboutLogoMeaningCard()

    Spacer(modifier = Modifier.height(16.dp))

    AboutDualStatementCard(
        leftTitle = "Vision",
        leftText = "Global leader of dynamic and transformative statistics, civil registration, and identification services.\n\nNangunguna sa pandaigdigang dinamiko at makapagpabagong estadistika, mga serbisyo ukol sa talaang sibil at pagkakakilanlan.",
        rightTitle = "Mission",
        rightText = "We provide statistics, civil registration, and identification services for sustainable, innovative, and inclusive development.\n\nMakapagbigay ng estadistika, mga serbisyo ukol sa talaang sibil at pagkakakilanlan para sa napapanatili, makabago, at inklusibong pag-unlad.",
    )

    Spacer(modifier = Modifier.height(16.dp))

    AboutPolicyCard(
        title = "Quality Policy",
        paragraphs = listOf(
            "We, the Philippine Statistics Authority, commit to deliver relevant and reliable statistics, efficient civil registration services, and an inclusive identification system to clients and stakeholders.",
            "We adhere to the UN Fundamental Principles of Official Statistics in the production of quality general-purpose statistics.",
            "We commit to deliver efficient civil registration services and an inclusive identification system in accordance with laws, rules, regulations, and other statutory requirements.",
            "We endeavor to live by the established core values and corporate personality of the PSA, while adopting appropriate technology in the development of products and delivery of services to ensure customer satisfaction.",
            "We commit to continually improve the effectiveness of our Quality Management System toward equitable development and improved quality of life for all."
        )
    )

    Spacer(modifier = Modifier.height(16.dp))

    AboutValuesCard(
        values = listOf(
            AboutValue(
                title = "Integrity",
                description = "We observe the highest standards of professional behavior, uphold impartiality and independence, and practice transparency in all interactions and transactions."
            ),
            AboutValue(
                title = "Service Excellence",
                description = "We deliver world-class statistical products, civil registration services, and inclusive identification systems that meet and exceed stakeholder expectations."
            ),
            AboutValue(
                title = "Adaptability",
                description = "We respond positively to change, embrace innovation and technology, and treat challenges as opportunities to improve service to the public."
            )
        )
    )

    Spacer(modifier = Modifier.height(16.dp))

    AboutPrismCard(
        traits = listOf(
            PrismTrait("P", "Professional", "ethical, expert, competent, dependable, committed to excellence"),
            PrismTrait("R", "Responsive", "customer-focused, active listener, proactive communicator"),
            PrismTrait("I", "Innovative", "modern, technologically adept, systems view, thinking out of the box"),
            PrismTrait("S", "Strategic Thinker", "long-term view, working smart, game changer, futurity of present decisions"),
            PrismTrait("M", "Motivated", "values people, promotes welfare, promotes professional development, sound HR policy")
        )
    )
}

private data class AboutValue(
    val title: String,
    val description: String
)

private data class PrismTrait(
    val letter: String,
    val title: String,
    val description: String
)

@Composable
private fun AboutHighlightCard(
    eyebrow: String,
    title: String,
    body: String
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = PsaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(PsaNavy)
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = eyebrow,
                    style = MaterialTheme.typography.labelLarge,
                    color = PsaGold
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = PsaBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 14.dp)
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                color = PsaNavy.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
private fun AboutListCard(
    title: String,
    items: List<String>
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = PsaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = PsaBlue,
                fontWeight = FontWeight.Bold
            )
            items.forEachIndexed { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (index % 2 == 0) {
                            PsaMist.copy(alpha = 0.92f)
                        } else {
                            PsaGold.copy(alpha = 0.18f)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(if (index % 3 == 0) PsaBlue else if (index % 3 == 1) PsaRed else PsaGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (index % 3 == 2) PsaNavy else Color.White
                            )
                        }
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyLarge,
                            color = PsaNavy,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutLogoMeaningCard() {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = PsaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = "The PSA Logo and Its Meaning",
                style = MaterialTheme.typography.titleLarge,
                color = PsaBlue,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "The logo signifies the PSA's commitment to provide timely and quality statistics necessary for decision-making in all aspects of Filipino life. It reflects the vision of being solid, responsive, and world-class.",
                style = MaterialTheme.typography.bodyLarge,
                color = PsaNavy.copy(alpha = 0.82f),
                modifier = Modifier.padding(top = 10.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    modifier = Modifier.size(116.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.psa_logo),
                        contentDescription = "PSA logo",
                        modifier = Modifier.padding(14.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AboutMiniInfoCard(
                        accentColor = PsaBlue,
                        title = "Three curved arrows",
                        body = "These symbolize the PSA pillars: solid, responsive, and world-class. The red, yellow, and blue colors reflect the Philippine flag and the national role of the PSA."
                    )
                    AboutMiniInfoCard(
                        accentColor = PsaRed,
                        title = "Sphere and grid",
                        body = "These represent adherence to the UN Fundamental Principles of Official Statistics and the PSA's commitment to international standards and the global statistical community."
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutMiniInfoCard(
    accentColor: Color,
    title: String,
    body: String
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.10f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = PsaBlue,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = PsaNavy.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun AboutDualStatementCard(
    leftTitle: String,
    leftText: String,
    rightTitle: String,
    rightText: String
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = PsaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                AboutStatementPanel(
                    modifier = Modifier.weight(1f),
                    title = leftTitle,
                    text = leftText
                )
                AboutStatementPanel(
                    modifier = Modifier.weight(1f),
                    title = rightTitle,
                    text = rightText
                )
            }
        }
    }
}

@Composable
private fun AboutStatementPanel(
    modifier: Modifier = Modifier,
    title: String,
    text: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (title == "Vision") PsaBlue.copy(alpha = 0.10f) else PsaGold.copy(alpha = 0.20f)
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = if (title == "Vision") PsaBlue else PsaRed,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = PsaNavy.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
private fun AboutPolicyCard(
    title: String,
    paragraphs: List<String>
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = PsaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = PsaBlue,
                fontWeight = FontWeight.Bold
            )
            paragraphs.forEachIndexed { index, paragraph ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (index % 3) {
                            0 -> PsaBlue.copy(alpha = 0.08f)
                            1 -> PsaGold.copy(alpha = 0.18f)
                            else -> PsaRed.copy(alpha = 0.08f)
                        }
                    )
                ) {
                    Text(
                        text = paragraph,
                        style = MaterialTheme.typography.bodyLarge,
                        color = PsaNavy.copy(alpha = 0.9f),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutValuesCard(
    values: List<AboutValue>
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = PsaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = "Core Values",
                style = MaterialTheme.typography.titleLarge,
                color = PsaBlue,
                fontWeight = FontWeight.Bold
            )
            values.forEachIndexed { index, value ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (index) {
                            0 -> PsaBlue.copy(alpha = 0.10f)
                            1 -> PsaGold.copy(alpha = 0.22f)
                            else -> PsaRed.copy(alpha = 0.10f)
                        }
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = value.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = when (index) {
                                0 -> PsaBlue
                                1 -> PsaNavy
                                else -> PsaRed
                            },
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = value.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = PsaNavy.copy(alpha = 0.9f),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutPrismCard(
    traits: List<PrismTrait>
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = PsaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = "Corporate Personality",
                style = MaterialTheme.typography.titleLarge,
                color = PsaBlue,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "PRISM",
                style = MaterialTheme.typography.headlineMedium,
                color = PsaGold,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 6.dp)
            )
            traits.forEachIndexed { index, trait ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                when (index % 4) {
                                    0 -> PsaBlue
                                    1 -> PsaRed
                                    2 -> PsaGold
                                    else -> PsaNavy
                                },
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = trait.letter,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (index % 4 == 2) PsaNavy else Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = trait.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (index % 2 == 0) PsaBlue else PsaRed,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = trait.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = PsaNavy.copy(alpha = 0.9f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModalStatPill(label: String, background: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White
        )
    }
}

@Composable
private fun ModalOverviewCard(
    title: String,
    content: String,
    accentColor: Color
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(accentColor, CircleShape)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = PsaNavy,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = PsaNavy.copy(alpha = 0.82f),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
private fun ModalSectionCard(
    section: ServiceSection,
    accentColor: Color
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(accentColor.copy(alpha = 0.14f))
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.labelLarge,
                        color = accentColor
                    )
                }
            }

            section.items.forEachIndexed { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = PsaMist.copy(alpha = 0.7f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(accentColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        }
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyLarge,
                            color = PsaNavy,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardBackdrop() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .size(280.dp)
                .padding(top = 60.dp, start = 120.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            PsaSky.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(top = 120.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.04f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}
