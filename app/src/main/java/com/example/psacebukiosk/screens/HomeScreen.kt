package com.example.psacebukiosk.screens

import android.content.Context
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.MotionEvent
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.JavascriptInterface
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.Thunderstorm
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbCloudy
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

private const val DEFAULT_ANNOUNCEMENT_DRIVE_FOLDER_URL =
    "https://drive.google.com/drive/folders/1GPI_4AdKZaeVHTOvk-QKQGxIibLx87rm?usp=sharing"

private const val CITIZENS_CHARTER_DRIVE_FOLDER_URL =
    "https://drive.google.com/drive/folders/1tlgpx3ytCCPa8hvIQFunC-1ADFfC6IWT?usp=sharing"

private const val FEEDBACK_SCRIPT_WEB_APP_URL =
    "https://script.google.com/macros/s/AKfycbx7tPe5E6-d8JwJ6B6I2ygqRQhYEDGY_dQAb7Q-9muGpJQ5e2dQAY0snTV2L6IU07q2/exec"

private const val EMPLOYEE_LOCATOR_SHEET_CSV_URL =
    "https://docs.google.com/spreadsheets/d/1tw30XS2Poy0AOWX7qVs0pL7CnLmV_uopGKJQ7mJ4vTI/gviz/tq?tqx=out:csv&sheet=Personnel"

private const val EMPLOYEE_LOCATOR_PHOTOS_FOLDER_URL =
    "https://drive.google.com/drive/folders/1G5aivL107HWiYnWcwRxkGct87LGpZ9U7?usp=sharing"

private const val EMPLOYEE_PHOTO_CACHE_DIR = "employee_photos"

private const val EMPLOYEE_LOCATOR_CACHE_FILE = "employee_locator_records.csv"

private const val NATIONAL_ID_APPOINTMENT_URL = "https://nationalidappt-psacebu.com/"

private const val NATIONAL_ID_MOBILE_REGISTRATION_SHEET_CSV_URL =
    "https://docs.google.com/spreadsheets/d/1KbnDa9KkVLZ58xjbQaCXTGZ_-Ma2YsyFgZNFpNPqCo8/export?format=csv&gid=2009435957"

private const val NATIONAL_ID_PUBLIC_ADVISORY_URL =
    "https://philsys.gov.ph/category/public-advisories/"

private const val NATIONAL_ID_FAQ_URL =
    "https://philsys.gov.ph/faq-frequently-asked-questions/"

private const val NATIONAL_ID_CONTACT_US_URL =
    "https://philsys.gov.ph/contact-us/"

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

private data class WeatherSnapshot(
    val temperatureC: Int,
    val humidity: Int,
    val windSpeedKmh: Int,
    val condition: String
)

private data class FeedbackResponse(
    val clientName: String,
    val services: List<String>,
    val otherService: String,
    val ratings: List<Int>,
    val comments: String
)

private data class EmployeeLocatorRecord(
    val name: String,
    val designation: String,
    val status: String,
    val backToOffice: String,
    val photoUrl: String? = null
)

private data class DriveEmployeePhoto(
    val fileId: String,
    val name: String,
    val mimeType: String,
    val key: String,
    val fileName: String
)

private data class CachedEmployeePhoto(
    val fileId: String,
    val fileName: String
)

private data class DashboardService(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconResId: Int? = null,
    val accentColor: Color,
    val overview: String,
    val sections: List<ServiceSection>,
    val footerNote: String
)

private sealed class DashboardTile {
    data class Service(val service: DashboardService) : DashboardTile()
    data object Privacy : DashboardTile()
}

private data class DashboardConfettiPiece(
    val xFraction: Float,
    val yFraction: Float,
    val size: Dp,
    val color: Color,
    val drift: Float,
    val rotation: Float,
    val rounded: Boolean
)

private data class NationalIdInfoPanel(
    val title: String,
    val kicker: String,
    val items: List<String>
)

private data class NationalIdMapLocation(
    val title: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

private data class NationalIdMapTile(
    val xOffset: Int,
    val yOffset: Int,
    val url: String
)

private data class NationalIdMobileScheduleDay(
    val weekday: String,
    val date: String,
    val entries: List<NationalIdMobileScheduleEntry>,
    val totalRko: String,
    val totalRa: String
)

private data class NationalIdMobileSchedule(
    val monthLabel: String,
    val days: List<NationalIdMobileScheduleDay>
)

private data class NationalIdMobileScheduleEntry(
    val location: String,
    val rko: String,
    val ra: String,
    val confirmed: Boolean
)

private data class NationalIdMobileScheduleColumnGroup(
    val weekday: String,
    val startColumn: Int,
    val endColumn: Int,
    val displayOrder: Int
)

private data class ParsedNationalIdMobileScheduleDay(
    val day: NationalIdMobileScheduleDay,
    val rowIndex: Int,
    val displayOrder: Int
)

private data class NationalIdFormatCard(
    val title: String,
    val description: String,
    val tag: String
)

private val philsysNationalIdImageUrls = listOf(
    "https://philsys.gov.ph/wp-content/uploads/2025/09/1.png",
    "https://philsys.gov.ph/wp-content/uploads/2025/09/2.5-1536x466.png",
    "https://philsys.gov.ph/wp-content/uploads/2025/09/3.png",
    "https://philsys.gov.ph/wp-content/uploads/2025/09/4.png",
    "https://philsys.gov.ph/wp-content/uploads/2025/09/5.5-768x404.png",
    "https://philsys.gov.ph/wp-content/uploads/2025/09/7.png",
    "https://philsys.gov.ph/wp-content/uploads/2025/09/8.png",
    "https://philsys.gov.ph/wp-content/uploads/2025/09/9.5.png",
    "https://philsys.gov.ph/wp-content/uploads/2025/09/11.png",
    "https://philsys.gov.ph/wp-content/uploads/2025/01/National-ID-PH-hashtag-300x45.png"
)

private val nationalIdRegistrationLocations = listOf(
    NationalIdMapLocation(
        title = "National ID Fixed Registration Center",
        address = "3rd Floor Gaisano Capital South, Corner Colon and Leon Kilat Street, Cebu City, Cebu",
        latitude = 10.296111,
        longitude = 123.896667
    ),
    NationalIdMapLocation(
        title = "Cebu Exchange Tower",
        address = "2/F Cebu Exchange Tower, Salinas Drive, Lahug, Cebu City, across Cebu IT Park",
        latitude = 10.3309,
        longitude = 123.9080
    ),
    NationalIdMapLocation(
        title = "Mandaue City eGovPH Serbisyo Hub",
        address = "Ground Floor, PCO Building, Subangdaku, Mandaue City, Cebu",
        latitude = 10.3194,
        longitude = 123.9258
    )
)

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val weather by produceState<WeatherSnapshot?>(initialValue = null) {
        while (true) {
            val latest = loadCebuWeather()
            if (latest != null) {
                value = latest
            }
            delay(15 * 60 * 1000L)
        }
    }
    val services = listOf(
        DashboardService(
            title = "Civil Registration",
            subtitle = "Birth, marriage, death, and CENOMAR services.",
            icon = Icons.Outlined.Description,
            iconResId = R.drawable.dashboard_civil_registration,
            accentColor = PsaBlue,
            overview = "Quick guide for civil registry certificate requests and record-related concerns before proceeding to the assigned CRS or help desk area.",
            sections = listOf(
                ServiceSection(
                    title = "Available Transactions",
                    items = listOf(
                        "Birth Certificate",
                        "Marriage Certificate",
                        "Death Certificate",
                        "CENOMAR request"
                    )
                ),
                ServiceSection(
                    title = "Requirements Checklist",
                    items = listOf(
                        "Valid ID of the requesting party",
                        "Authorization letter and representative ID if requesting for another person",
                        "Known details: full name, date and place of event, and parents or spouse details when applicable"
                    )
                ),
                ServiceSection(
                    title = "Client Flow",
                    items = listOf(
                        "Confirm the certificate or concern needed",
                        "Prepare ID and supporting documents",
                        "Proceed to the assigned CRS or help desk window",
                        "Review spelling and details before leaving"
                    )
                )
            ),
            footerNote = "For corrections, late registration, or unclear records, ask staff for the correct civil registry guidance."
        ),
        DashboardService(
            title = "National ID System",
            subtitle = "",
            icon = Icons.Outlined.Badge,
            iconResId = R.drawable.dashboard_national_id,
            accentColor = PsaGold,
            overview = "Guidance for National ID and PhilSys concerns including registration reminders, status checking, and where to proceed for assistance.",
            sections = listOf(
                ServiceSection(
                    title = "Available Assistance",
                    items = listOf(
                        "National ID registration guidance",
                        "ePhilID or Digital National ID guidance",
                        "Status checking reminders",
                        "Correction or replacement referral guidance"
                    )
                ),
                ServiceSection(
                    title = "Prepare Before Proceeding",
                    items = listOf(
                        "Bring valid supporting documents",
                        "Prepare transaction or reference slip if available",
                        "Confirm contact details and demographic information",
                        "Follow appointment or queue instructions"
                    )
                ),
                ServiceSection(
                    title = "Where To Go",
                    items = listOf(
                        "Proceed to the National ID assistance point",
                        "Ask staff for status, updating, or document concerns",
                        "Check posted advisories for temporary service changes"
                    )
                )
            ),
            footerNote = "This module should help clients know what to prepare before approaching the PhilSys assistance area."
        ),
        DashboardService(
            title = "Statistical",
            subtitle = "Data highlights, office facts, and reference information.",
            icon = Icons.Outlined.Insights,
            iconResId = R.drawable.dashboard_statistical,
            accentColor = PsaRed,
            overview = "Displays quick public statistics, data highlights, and PSA releases useful for visitors while waiting or looking for official references.",
            sections = listOf(
                ServiceSection(
                    title = "Featured Data",
                    items = listOf(
                        "Cebu population highlights",
                        "Inflation and price statistics",
                        "Labor and employment indicators",
                        "Tourism, business, agriculture, and fisheries quick facts"
                    )
                ),
                ServiceSection(
                    title = "Learning References",
                    items = listOf(
                        "Infographics from PSA releases",
                        "QR links to PSA website or OpenSTAT",
                        "Did-you-know data cards",
                        "Latest survey announcements"
                    )
                ),
                ServiceSection(
                    title = "Best Use",
                    items = listOf(
                        "Simple data browsing while waiting",
                        "Public education and awareness",
                        "Quick reference for commonly requested statistics"
                    )
                )
            ),
            footerNote = "Keep the content short, visual, and based on official PSA releases only."
        ),
        DashboardService(
            title = "Admin Support",
            subtitle = "Front desk guidance and internal visitor assistance references.",
            icon = Icons.Outlined.SupportAgent,
            iconResId = R.drawable.dashboard_admin_support,
            accentColor = PsaSky,
            overview = "General support guide for visitors who need direction, routing, or front desk assistance before choosing a service.",
            sections = listOf(
                ServiceSection(
                    title = "Support Desk",
                    items = listOf(
                        "Front desk assistance",
                        "Concern referral or routing",
                        "Document receiving guidance",
                        "General office contact information"
                    )
                ),
                ServiceSection(
                    title = "Common Requests",
                    items = listOf(
                        "Office hours",
                        "Section directory",
                        "Lost and found or general assistance",
                        "Non-CRS concern referral"
                    )
                ),
                ServiceSection(
                    title = "Client Reminder",
                    items = listOf(
                        "Ask the front desk if unsure which office handles your concern",
                        "Prepare IDs, reference numbers, and supporting documents",
                        "Follow staff instructions for queueing and routing"
                    )
                )
            ),
            footerNote = "This module should reduce confusion before clients join a queue or approach a window."
        ),
        DashboardService(
            title = "Bulletin",
            subtitle = "Latest advisories, notices, and office announcements.",
            icon = Icons.Outlined.Campaign,
            iconResId = R.drawable.dashboard_bulletin,
            accentColor = PsaSky,
            overview = "Live notice board for advisories, schedules, survey notices, and important public announcements.",
            sections = listOf(
                ServiceSection(
                    title = "Current Advisories",
                    items = listOf(
                        "Holiday or no-office schedules",
                        "System downtime",
                        "Temporary service changes",
                        "Emergency notices"
                    )
                ),
                ServiceSection(
                    title = "Public Announcements",
                    items = listOf(
                        "Survey operations notices",
                        "Public-facing hiring or procurement postings",
                        "Upcoming activities",
                        "Latest PSA Cebu updates"
                    )
                ),
                ServiceSection(
                    title = "Recommended Display",
                    items = listOf(
                        "Use short headlines",
                        "Include date and affected service when possible",
                        "Keep urgent announcements at the top"
                    )
                )
            ),
            footerNote = "Use Bulletin for same-day reminders and time-sensitive updates that visitors should see immediately."
        ),
        DashboardService(
            title = "Office Guide",
            subtitle = "Find the right window, lane, and process flow quickly.",
            icon = Icons.Outlined.Map,
            accentColor = PsaBlue,
            overview = "A navigation guide for clients so they know where to go, what lane to use, and how the transaction flow works.",
            sections = listOf(
                ServiceSection(
                    title = "Navigation",
                    items = listOf(
                        "Counter or window guide",
                        "Queueing instructions",
                        "Payment or releasing area directions",
                        "Help desk and front desk location"
                    )
                ),
                ServiceSection(
                    title = "Client Flow",
                    items = listOf(
                        "Start at screening or information point",
                        "Choose the correct service or counter",
                        "Follow priority lane guidance if applicable",
                        "Proceed to releasing or the next instruction"
                    )
                ),
                ServiceSection(
                    title = "Accessibility",
                    items = listOf(
                        "Senior citizen, PWD, and pregnant client assistance",
                        "Comfort room and waiting area locations",
                        "Emergency exit and safety reminders"
                    )
                )
            ),
            footerNote = "This module works best with a simple floor guide or visual map once the final office layout is available."
        ),
        DashboardService(
            title = "Citizens Charter",
            subtitle = "PDF service standards, requirements, fees, and timelines.",
            icon = Icons.Outlined.Gavel,
            iconResId = R.drawable.dashboard_citizens_charter,
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
            icon = Icons.Outlined.Groups,
            iconResId = R.drawable.dashboard_organizational_chart,
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
            icon = Icons.Outlined.Badge,
            iconResId = R.drawable.dashboard_employee_locator,
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
            icon = Icons.Outlined.Info,
            iconResId = R.drawable.dashboard_about_us,
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
    var selectedService by remember { mutableStateOf<DashboardService?>(null) }
    var folderBannerUri by rememberSaveable { mutableStateOf<String?>(null) }
    var driveFolderMediaItems by remember { mutableStateOf<List<DriveAnnouncementMedia>>(emptyList()) }
    var showPrivacySealDialog by rememberSaveable { mutableStateOf(false) }
    var showRateDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        folderBannerUri = loadAnnouncementBannerFromFolder(context)
        if (folderBannerUri == null) {
            driveFolderMediaItems =
                loadAnnouncementMediaFromDriveFolder(DEFAULT_ANNOUNCEMENT_DRIVE_FOLDER_URL)
        }
    }

    Scaffold(containerColor = Color.Transparent) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEAF6FF),
                            Color(0xFFC9E8FF),
                            Color(0xFF9ED2F5),
                            Color(0xFF6DAEDB)
                        )
                    )
                )
        ) {
            DashboardBackdrop()

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val compact = maxWidth < 600.dp
                val wideKiosk = maxWidth >= 840.dp
                val tallPortrait = maxHeight >= 820.dp
                val contentPadding = when {
                    wideKiosk -> 24.dp
                    compact -> 12.dp
                    else -> 18.dp
                }
                val gap = if (compact) 10.dp else 14.dp
                val serviceCardHeight = when {
                    wideKiosk && tallPortrait -> 150.dp
                    wideKiosk -> 136.dp
                    compact && tallPortrait -> 112.dp
                    compact -> 102.dp
                    tallPortrait -> 124.dp
                    else -> 112.dp
                }
                val gridGap = 0.dp
                val gridTiles = dashboardTiles(services, compact)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                    .padding(contentPadding)
                ) {
                    DashboardReveal(delayMillis = 0) {
                        KioskHeader(weather = weather)
                    }

                    Spacer(modifier = Modifier.height(gap))

                    DashboardReveal(delayMillis = 90) {
                        MediaBannerSection(
                            folderBannerUri = folderBannerUri,
                            driveFolderMediaItems = driveFolderMediaItems
                        )
                    }

                    Spacer(modifier = Modifier.height(gap))

                    ServiceGridPanel {
                        ResponsiveDashboardGrid(
                            tiles = gridTiles,
                            columns = if (compact) 2 else 3,
                            cardHeight = serviceCardHeight,
                            startDelay = 240,
                            rowGap = gridGap,
                            columnGap = gridGap,
                            onServiceClick = { selectedService = it },
                            onPrivacyClick = { showPrivacySealDialog = true }
                        )
                    }
                }
            }

            RateUsFloatingButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(18.dp),
                onClick = { showRateDialog = true }
            )
        }
    }

    selectedService?.let { service ->
        if (service.title == "Employee Locator Chart") {
            EmployeeLocatorDialog(onDismiss = { selectedService = null })
        } else {
            ServiceInfoDialog(
                service = service,
                onDismiss = { selectedService = null }
            )
        }
    }

    if (showPrivacySealDialog) {
        DataPrivacySealDialog(
            onDismiss = { showPrivacySealDialog = false }
        )
    }

    if (showRateDialog) {
        RateUsDialog(onDismiss = { showRateDialog = false })
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
private fun ResponsiveDashboardGrid(
    tiles: List<DashboardTile>,
    columns: Int,
    cardHeight: Dp,
    startDelay: Int,
    rowGap: Dp,
    columnGap: Dp,
    onServiceClick: (DashboardService) -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(rowGap)
    ) {
        tiles.chunked(columns).forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(columnGap)
            ) {
                row.forEachIndexed { columnIndex, tile ->
                    val tileIndex = rowIndex * columns + columnIndex

                    DashboardReveal(
                        delayMillis = startDelay + 70 * tileIndex,
                        modifier = Modifier.weight(1f),
                    ) {
                        when (tile) {
                            is DashboardTile.Service -> KioskServiceCard(
                                service = tile.service,
                                cardHeight = cardHeight,
                                animationIndex = tileIndex,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onServiceClick(tile.service) }
                            )
                            DashboardTile.Privacy -> PrivacyDashboardTile(
                                cardHeight = cardHeight,
                                animationIndex = tileIndex,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onPrivacyClick
                            )
                        }
                    }
                }

                repeat(columns - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

private fun dashboardTiles(
    services: List<DashboardService>,
    compact: Boolean
): List<DashboardTile> {
    val about = services.firstOrNull { it.title == "About Us" }
    val employeeLocator = services.firstOrNull { it.title == "Employee Locator Chart" }

    val orderedServices = if (compact) {
        services.filterNot {
            it.title == "About Us" || it.title == "Employee Locator Chart"
        } + listOfNotNull(about) + listOfNotNull(employeeLocator)
    } else {
        services.filterNot { it.title == "About Us" } + listOfNotNull(about)
    }

    val output = mutableListOf<DashboardTile>()
    orderedServices.forEach { service ->
        output += DashboardTile.Service(service)
        if (service.title == "About Us") {
            output += DashboardTile.Privacy
        }
    }
    return output
}

@Composable
private fun KioskHeader(weather: WeatherSnapshot?) {
    BoxWithConstraints {
        val compact = maxWidth < 430.dp
        val logoSize = if (compact) 58.dp else 72.dp
        val headerPadding = if (compact) 12.dp else 16.dp
        val titleStyle = if (compact) {
            MaterialTheme.typography.titleMedium
        } else {
            MaterialTheme.typography.titleLarge
        }
        val welcomeStyle = if (compact) {
            MaterialTheme.typography.labelLarge
        } else {
            MaterialTheme.typography.bodyLarge
        }
        val timeStyle = if (compact) {
            MaterialTheme.typography.titleLarge
        } else {
            MaterialTheme.typography.headlineSmall
        }
        val dateStyle = if (compact) {
            MaterialTheme.typography.labelLarge
        } else {
            MaterialTheme.typography.bodyMedium
        }

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF53A9E4).copy(alpha = 0.56f),
                            Color(0xFF2385C8).copy(alpha = 0.62f),
                            Color(0xFF0E5B9B).copy(alpha = 0.70f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.42f),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .height(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(PsaBlue.copy(alpha = 0.88f))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(PsaGold.copy(alpha = 0.88f))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(PsaRed.copy(alpha = 0.88f))
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.34f),
                                Color.White.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(
                        start = headerPadding,
                        top = if (compact) 13.dp else 15.dp,
                        end = headerPadding,
                        bottom = if (compact) 11.dp else 13.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.psa_logo),
                    contentDescription = "Philippine Statistics Authority logo",
                    modifier = Modifier
                        .size(logoSize)
                        .padding(3.dp),
                    contentScale = ContentScale.Fit
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = if (compact) 10.dp else 13.dp, end = if (compact) 8.dp else 12.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(
                        text = "Welcome to",
                        style = welcomeStyle,
                        color = Color.White.copy(alpha = 0.82f),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "PSA Cebu Provincial",
                        style = titleStyle,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Statistical Office",
                        style = titleStyle,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = timeText,
                        style = timeStyle,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = dateText,
                        style = dateStyle,
                        color = Color.White.copy(alpha = 0.80f),
                        maxLines = 1
                    )
                    WeatherChip(weather = weather, compact = compact)
                }
            }
        }
    }
}

@Composable
private fun WeatherChip(
    weather: WeatherSnapshot?,
    compact: Boolean
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color.White.copy(alpha = 0.18f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.22f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = if (compact) 7.dp else 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(if (compact) 5.dp else 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = weatherIconVector(weather?.condition),
                contentDescription = weather?.condition ?: "Weather",
                tint = Color(0xFFFFD95C),
                modifier = Modifier.size(if (compact) 15.dp else 17.dp)
            )
            Text(
                text = "Cebu City",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFFFE38A),
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Text(
                text = weather?.let { "${it.temperatureC}C" } ?: "--C",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            if (!compact && weather != null) {
                Text(
                    text = weather.condition,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.70f),
                    maxLines = 1
                )
            }
        }
    }
}

private fun weatherIconVector(condition: String?): ImageVector {
    return when (condition) {
        "Clear" -> Icons.Outlined.WbSunny
        "Partly cloudy", "Cloudy", "Fog" -> Icons.Outlined.WbCloudy
        "Drizzle", "Rain", "Showers" -> Icons.Outlined.WaterDrop
        "Thunderstorm" -> Icons.Outlined.Thunderstorm
        else -> Icons.Outlined.WbCloudy
    }
}

@Composable
private fun ServiceGridPanel(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White.copy(alpha = 0.55f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.82f)),
        shadowElevation = 12.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.70f),
                            Color(0xFFD8EEFF).copy(alpha = 0.52f),
                            Color(0xFFB7DEFA).copy(alpha = 0.38f)
                        )
                    )
                )
                .padding(0.dp)
        ) {
            content()
            DashboardConfettiLayer(modifier = Modifier.matchParentSize())
        }
    }
}

@Composable
private fun DashboardConfettiLayer(modifier: Modifier = Modifier) {
    val pieces = remember {
        listOf(
            DashboardConfettiPiece(0.06f, 0.09f, 7.dp, PsaBlue, 6f, 14f, true),
            DashboardConfettiPiece(0.22f, 0.03f, 12.dp, PsaGold, 8f, -10f, false),
            DashboardConfettiPiece(0.46f, 0.08f, 6.dp, PsaRed, 5f, 12f, true),
            DashboardConfettiPiece(0.74f, 0.04f, 10.dp, PsaBlue, 7f, -16f, false),
            DashboardConfettiPiece(0.91f, 0.12f, 7.dp, PsaGold, 6f, 18f, true),
            DashboardConfettiPiece(0.03f, 0.37f, 9.dp, PsaGold, 8f, -14f, false),
            DashboardConfettiPiece(0.93f, 0.36f, 8.dp, PsaRed, 6f, 16f, true),
            DashboardConfettiPiece(0.08f, 0.66f, 6.dp, PsaBlue, 5f, -12f, true),
            DashboardConfettiPiece(0.86f, 0.69f, 11.dp, PsaGold, 9f, 12f, false),
            DashboardConfettiPiece(0.21f, 0.93f, 8.dp, PsaRed, 6f, -18f, true),
            DashboardConfettiPiece(0.50f, 0.95f, 10.dp, PsaBlue, 8f, 15f, false),
            DashboardConfettiPiece(0.76f, 0.91f, 6.dp, PsaGold, 5f, -12f, true)
        )
    }

    BoxWithConstraints(modifier = modifier) {
        pieces.forEachIndexed { index, piece ->
            val motion by rememberIdleMotion(index + 40)
            Box(
                modifier = Modifier
                    .offset(
                        x = maxWidth * piece.xFraction,
                        y = maxHeight * piece.yFraction
                    )
                    .graphicsLayer {
                        translationY = (motion - 0.5f) * piece.drift
                        translationX = (0.5f - motion) * piece.drift * 0.55f
                        rotationZ = (motion - 0.5f) * piece.rotation
                        alpha = 0.38f
                    }
                    .size(piece.size)
                    .clip(if (piece.rounded) CircleShape else RoundedCornerShape(2.dp))
                    .background(piece.color.copy(alpha = 0.64f))
            )
        }
    }
}

@Composable
private fun KioskServiceCard(
    service: DashboardService,
    cardHeight: Dp,
    animationIndex: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(2.dp)
    val tileColors = serviceButtonColors(service.title)
    val accentColor = serviceButtonAccent(service.title)
    val textColor = serviceButtonTextColor()
    val idleMotion by rememberIdleMotion(animationIndex)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val compactTile = cardHeight <= 110.dp
    val labelStyle = MaterialTheme.typography.labelLarge.copy(
        fontSize = if (compactTile) 11.sp else 12.5.sp,
        lineHeight = if (compactTile) 13.sp else 15.sp
    )

    Surface(
        modifier = modifier
            .graphicsLayer {
                translationY = if (pressed) 1.8f else -1.7f * idleMotion
                scaleX = if (pressed) 0.975f else 1f + 0.004f * idleMotion
                scaleY = if (pressed) 0.975f else 1f + 0.004f * idleMotion
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = cardShape,
        color = tileColors.first(),
        border = androidx.compose.foundation.BorderStroke(0.7.dp, Color.White.copy(alpha = 0.86f)),
        tonalElevation = 4.dp,
        shadowElevation = if (pressed) 1.dp else 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .background(
                    Brush.verticalGradient(
                        colors = tileColors
                    )
                )
                .border(
                    width = 0.7.dp,
                    color = Color.White.copy(alpha = 0.60f),
                    shape = cardShape
                )
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(0.64f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(accentColor.copy(alpha = 0.68f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 7.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .size(if (compactTile) 42.dp else 50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (service.iconResId != null) {
                        Image(
                            painter = painterResource(id = service.iconResId),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(if (compactTile) 2.dp else 3.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Icon(
                            imageVector = service.icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(if (compactTile) 24.dp else 28.dp)
                        )
                    }
                }

                Text(
                    text = dashboardTileLabel(service.title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (compactTile) 40.dp else 44.dp),
                    color = textColor,
                    style = labelStyle,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun PrivacyDashboardTile(
    cardHeight: Dp,
    animationIndex: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(2.dp)
    val idleMotion by rememberIdleMotion(animationIndex)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val compactTile = cardHeight <= 110.dp
    val titleStyle = MaterialTheme.typography.labelLarge.copy(
        fontSize = if (compactTile) 11.sp else 12.5.sp,
        lineHeight = if (compactTile) 13.sp else 15.sp
    )

    Surface(
        modifier = modifier
            .graphicsLayer {
                translationY = if (pressed) 1.8f else -1.7f * idleMotion
                scaleX = if (pressed) 0.975f else 1f + 0.004f * idleMotion
                scaleY = if (pressed) 0.975f else 1f + 0.004f * idleMotion
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = cardShape,
        color = Color(0xFFE7F4FF),
        border = androidx.compose.foundation.BorderStroke(0.7.dp, Color.White.copy(alpha = 0.86f)),
        tonalElevation = 4.dp,
        shadowElevation = if (pressed) 1.dp else 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFCFEFF),
                            Color(0xFFE6F5FF),
                            Color(0xFFD8ECFF)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.50f),
                    shape = cardShape
                )
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(0.64f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(PsaGold.copy(alpha = 0.68f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 7.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .size(if (compactTile) 38.dp else 44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.86f))
                        .border(
                            width = 1.dp,
                            color = PsaGold.copy(alpha = 0.24f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.data_privacy_seal),
                        contentDescription = "Data Privacy Seal",
                        modifier = Modifier
                            .fillMaxWidth(0.72f)
                            .fillMaxHeight(0.78f),
                        contentScale = ContentScale.Fit
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (compactTile) 40.dp else 44.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Data Privacy\nSeal",
                        style = titleStyle,
                        color = PsaNavy,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun dashboardTileLabel(title: String): String {
    return when (title) {
        "Organizational Structure" -> "Organizational\nStructure"
        "Employee Locator Chart" -> "Employee Locator\nChart"
        else -> title
    }
}

@Composable
private fun rememberIdleMotion(animationIndex: Int): androidx.compose.runtime.State<Float> {
    val transition = rememberInfiniteTransition(label = "tile-idle-motion")
    return transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2200 + (animationIndex % 5) * 180,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tile-idle-motion-$animationIndex"
    )
}

private fun serviceButtonColors(title: String): List<Color> {
    return when (title) {
        "Civil Registration" -> listOf(Color(0xFFFAFDFF), Color(0xFFD8ECFF), Color(0xFFB9DCFA))
        "National ID System" -> listOf(Color(0xFFFFFEFA), Color(0xFFE5F2FF), Color(0xFFCFE7FF))
        "Statistical" -> listOf(Color(0xFFFFFCFA), Color(0xFFEAF5FF), Color(0xFFD5EAFF))
        "Admin Support" -> listOf(Color(0xFFF9FDFF), Color(0xFFD7EDFF), Color(0xFFBFE1FA))
        "Bulletin" -> listOf(Color(0xFFF9FEFF), Color(0xFFD2F1FF), Color(0xFFB7E7F8))
        "Office Guide" -> listOf(Color(0xFFFAFDFF), Color(0xFFDDEEFF), Color(0xFFC3E0FB))
        "Citizens Charter" -> listOf(Color(0xFFFFFCFA), Color(0xFFEAF5FF), Color(0xFFD6EBFF))
        "Organizational Structure" -> listOf(Color(0xFFFAFDFF), Color(0xFFD9ECFF), Color(0xFFC0DDF7))
        "Employee Locator Chart" -> listOf(Color(0xFFF8FEFF), Color(0xFFD1F0FF), Color(0xFFB5E3F3))
        "About Us" -> listOf(Color(0xFFFFFEFA), Color(0xFFE7F4FF), Color(0xFFD3EAFE))
        else -> listOf(Color(0xFFFAFDFF), Color(0xFFE8F3FF), Color(0xFFD3EAFE))
    }
}

private fun serviceButtonAccent(title: String): Color {
    return when (title) {
        "Civil Registration" -> Color(0xFF2367A2)
        "National ID System" -> Color(0xFFD6A319)
        "Statistical" -> Color(0xFFE26D50)
        "Admin Support" -> Color(0xFF3C9876)
        "Bulletin" -> Color(0xFF18A7B7)
        "Office Guide" -> Color(0xFF2F72BD)
        "Citizens Charter" -> Color(0xFFD84C3A)
        "Organizational Structure" -> Color(0xFF3869B5)
        "Employee Locator Chart" -> Color(0xFF2C9CA2)
        "About Us" -> Color(0xFFD3A018)
        else -> PsaBlue
    }
}

private fun serviceButtonTextColor(): Color {
    return PsaNavy
}

@Composable
private fun RateUsFloatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val motion by rememberIdleMotion(12)

    Surface(
        modifier = modifier
            .graphicsLayer {
                translationY = -5f * motion
                scaleX = 1f + 0.012f * motion
                scaleY = 1f + 0.012f * motion
            }
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = PsaGold,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.46f)),
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.dashboard_rate_us),
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Rate Us",
                style = MaterialTheme.typography.labelLarge,
                color = PsaNavy,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RateUsDialog(onDismiss: () -> Unit) {
    val scope = rememberCoroutineScope()
    val serviceTypes = listOf("CRASM", "Electronic Endorsement", "National ID")
    val surveyItems = listOf(
        "The level of courtesy and professionalism of our staff.",
        "The waiting time of service.",
        "Overall services provided.",
        "The information provided by our staff.",
        "Resolution of your concern / complaints.",
        "Cleanliness of the Rest Room areas.",
        "Cleanliness of waiting area.",
        "Coolness of Service Area."
    )
    var clientName by rememberSaveable { mutableStateOf("") }
    var otherService by rememberSaveable { mutableStateOf("") }
    var comments by rememberSaveable { mutableStateOf("") }
    var selectedServices by remember { mutableStateOf(setOf<String>()) }
    var ratings by remember { mutableStateOf(List(surveyItems.size) { 0 }) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }

    fun clearForm() {
        clientName = ""
        otherService = ""
        comments = ""
        selectedServices = emptySet()
        ratings = List(surveyItems.size) { 0 }
        errorMessage = null
        isSubmitting = false
        submitted = false
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.48f))
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.94f),
                shape = RoundedCornerShape(30.dp),
                color = Color(0xFFF4F8FB),
                shadowElevation = 24.dp
            ) {
                if (submitted) {
                    FeedbackThankYouCard(
                        onMainScreen = onDismiss,
                        onAnotherResponse = { clearForm() }
                    )
                } else {
                    Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FeedbackHeaderCard()

                    FeedbackInfoCard()

                    FeedbackSectionTitle("Client Information")
                    FeedbackInputCard {
                        OutlinedTextField(
                            value = clientName,
                            onValueChange = { clientName = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text("Client Name (Optional)") }
                        )
                    }

                    FeedbackSectionTitle("Type of Services")
                    FeedbackInputCard {
                        serviceTypes.forEach { service ->
                            FeedbackCheckboxRow(
                                label = service,
                                checked = service in selectedServices,
                                onCheckedChange = { checked: Boolean ->
                                    selectedServices = if (checked) {
                                        selectedServices + service
                                    } else {
                                        selectedServices - service
                                    }
                                }
                            )
                        }
                        OutlinedTextField(
                            value = otherService,
                            onValueChange = { otherService = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            singleLine = true,
                            label = { Text("Others (Specify)") }
                        )
                    }

                    FeedbackSectionTitle("Customer Satisfaction Survey")
                    FeedbackInputCard {
                        Text(
                            text = "Please rate each item by tapping the stars below.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PsaNavy.copy(alpha = 0.74f)
                        )
                        surveyItems.forEachIndexed { index, item ->
                            SurveyRatingRow(
                                item = item,
                                rating = ratings[index],
                                onRatingChange = { rating: Int ->
                                    ratings = ratings.toMutableList().also { it[index] = rating }
                                }
                            )
                            if (index != surveyItems.lastIndex) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                            }
                        }
                    }

                    FeedbackSectionTitle("Comments / Suggestions")
                    FeedbackInputCard {
                        OutlinedTextField(
                            value = comments,
                            onValueChange = { comments = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            label = { Text("Enter your comments or suggestions") }
                        )
                    }

                    errorMessage?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = PsaRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                val hasService = selectedServices.isNotEmpty() || otherService.isNotBlank()
                                val hasRating = ratings.any { it > 0 }
                                errorMessage = when {
                                    !hasService -> "Please select at least one service type."
                                    !hasRating -> "Please rate at least one survey item."
                                    else -> null
                                }
                                if (errorMessage == null) {
                                    val response = FeedbackResponse(
                                        clientName = clientName.trim(),
                                        services = selectedServices.toList(),
                                        otherService = otherService.trim(),
                                        ratings = ratings,
                                        comments = comments.trim()
                                    )
                                    scope.launch {
                                        isSubmitting = true
                                        val result = submitFeedbackResponse(response, surveyItems)
                                        isSubmitting = false
                                        if (result.isSuccess) {
                                            submitted = true
                                        } else {
                                            errorMessage = result.exceptionOrNull()?.message
                                                ?: "Unable to submit feedback. Please try again."
                                        }
                                    }
                                }
                            },
                            enabled = !isSubmitting,
                            modifier = Modifier
                                .weight(1f)
                                .height(58.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PsaBlue)
                        ) {
                            Text(
                                text = if (isSubmitting) "Submitting..." else "Submit Survey",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Button(
                            onClick = { clearForm() },
                            enabled = !isSubmitting,
                            modifier = Modifier
                                .weight(1f)
                                .height(58.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = PsaBlue
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PsaBlue)
                        ) {
                            Text("Clear Form", fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = PsaNavy
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PsaNavy.copy(alpha = 0.20f))
                    ) {
                        Text("Back to Main Screen", fontWeight = FontWeight.Bold)
                    }
                }
                }
            }
        }
    }
}

@Composable
private fun FeedbackHeaderCard() {
    val now by produceState(initialValue = Date()) {
        while (true) {
            value = Date()
            delay(1000)
        }
    }
    val dateText = remember(now) {
        SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(now)
    }
    val timeText = remember(now) {
        SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(now)
    }

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.psa_logo),
                contentDescription = "Philippine Statistics Authority logo",
                modifier = Modifier.size(92.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = dateText,
                style = MaterialTheme.typography.bodyMedium,
                color = PsaNavy.copy(alpha = 0.62f),
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = timeText,
                style = MaterialTheme.typography.titleMedium,
                color = PsaNavy,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "REPUBLIC OF THE PHILIPPINES",
                style = MaterialTheme.typography.labelMedium,
                color = PsaNavy.copy(alpha = 0.54f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 18.dp)
            )
            Text(
                text = "Philippine Statistics Authority",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF151B2A),
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Cebu Provincial Statistical Office",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF151B2A),
                fontWeight = FontWeight.SemiBold
            )
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = PsaBlue,
                modifier = Modifier.padding(top = 18.dp)
            ) {
                Text(
                    text = "CONFIDENTIAL PACD FORM",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FeedbackInfoCard() {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = PsaMist.copy(alpha = 0.92f),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaSky.copy(alpha = 0.18f))
    ) {
        Text(
            text = "The Philippine Statistics Authority strives to provide quality service to our clients. We believe that client feedback can help us improve our services in the future. Please fill in the details below.",
            modifier = Modifier.padding(18.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = PsaNavy.copy(alpha = 0.86f)
        )
    }
}

@Composable
private fun FeedbackSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = Color(0xFF151B2A),
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
private fun FeedbackInputCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            content = content
        )
    }
}

@Composable
private fun FeedbackCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = PsaBlue,
                uncheckedColor = PsaNavy.copy(alpha = 0.36f)
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF151B2A),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun SurveyRatingRow(
    item: String,
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    val activeColor = ratingColor(rating)

    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            text = item,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF151B2A),
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = if (rating == 0) "Tap to rate" else ratingLabel(rating),
            style = MaterialTheme.typography.labelLarge,
            color = if (rating == 0) PsaNavy.copy(alpha = 0.45f) else activeColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp)
        )
        Row(
            modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(5) { index ->
                val starNumber = index + 1
                val selected = starNumber <= rating
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onRatingChange(starNumber) },
                    shape = CircleShape,
                    color = if (selected) activeColor.copy(alpha = 0.16f) else Color(0xFFF1F4F7)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Rate $starNumber",
                            tint = if (selected) activeColor else PsaNavy.copy(alpha = 0.24f),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun ratingLabel(rating: Int): String {
    return when (rating) {
        1 -> "Poor"
        2 -> "Fair"
        3 -> "Neutral"
        4 -> "Satisfied"
        5 -> "Very Satisfied"
        else -> "Tap to rate"
    }
}

private fun ratingColor(rating: Int): Color {
    return when (rating) {
        1, 2 -> PsaRed
        3 -> Color(0xFFF5B51B)
        4, 5 -> Color(0xFF5DAD3B)
        else -> PsaNavy.copy(alpha = 0.35f)
    }
}

@Composable
private fun FeedbackThankYouCard(
    onMainScreen: () -> Unit,
    onAnotherResponse: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 7.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(112.dp),
                    shape = CircleShape,
                    color = Color(0xFFE7F6E9)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "OK",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color(0xFF168A36),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "Thank You",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF151B2A),
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(top = 26.dp)
                )
                Text(
                    text = "Your feedback has been submitted successfully. Your response will help us improve our services.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = PsaNavy.copy(alpha = 0.72f),
                    modifier = Modifier.padding(top = 12.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onMainScreen,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PsaBlue)
                ) {
                    Text("Back to Main Screen", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onAnotherResponse,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = PsaBlue
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PsaBlue)
                ) {
                    Text("Submit Another Response", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun EmployeeLocatorDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var records by remember { mutableStateOf<List<EmployeeLocatorRecord>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var statusFilter by rememberSaveable { mutableStateOf("ALL") }
    var selectedEmployeeName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val cachedRecords = loadCachedEmployeeLocatorRecords(context)
        if (cachedRecords.isNotEmpty()) {
            records = cachedRecords
            isLoading = false
        }

        if (cachedRecords.isEmpty()) {
            isLoading = true
        }
        val result = loadEmployeeLocatorRecords(context)
        val fetchedRecords = result.getOrNull()
        if (fetchedRecords != null) {
            records = fetchedRecords
            errorMessage = null
        } else if (records.isEmpty()) {
            errorMessage = result.exceptionOrNull()?.message
        }
        isLoading = false
        val latestRecords = fetchedRecords ?: records

        val syncedPhotos = withContext(Dispatchers.IO) {
            syncEmployeePhotosFromDrive(
                context = context,
                employeeKeys = latestRecords.map { employeePhotoKeyFromName(it.name) }.toSet()
            )
        }
        if (syncedPhotos.isNotEmpty()) {
            records = records.map { record ->
                val key = employeePhotoKeyFromName(record.name)
                record.copy(photoUrl = syncedPhotos[key] ?: record.photoUrl)
            }
        }
    }

    val filteredRecords = remember(records, statusFilter) {
        records.filter { record ->
            val statusGroup = employeeStatusGroup(record.status)
            statusFilter == "ALL" || statusFilter == statusGroup
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.56f))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.94f),
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFFEAF6FF),
                shadowElevation = 24.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    EmployeeLocatorHeader(
                        total = records.size,
                        inCount = records.count { employeeStatusGroup(it.status) == "IN" },
                        outCount = records.count { employeeStatusGroup(it.status) == "OUT" },
                        onDismiss = onDismiss
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFDDF2FF),
                                        Color(0xFFEAF7FF),
                                        Color(0xFFD8EEFF)
                                    )
                                )
                            )
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            EmployeeFilterChip("ALL", statusFilter) { statusFilter = "ALL" }
                            EmployeeFilterChip("IN", statusFilter) { statusFilter = "IN" }
                            EmployeeFilterChip("OUT", statusFilter) { statusFilter = "OUT" }
                        }

                        Text(
                            text = "${filteredRecords.size} shown",
                            style = MaterialTheme.typography.labelLarge,
                            color = PsaNavy.copy(alpha = 0.62f),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    when {
                        isLoading -> Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Loading personnel locator...",
                                style = MaterialTheme.typography.titleMedium,
                                color = PsaNavy
                            )
                        }
                        errorMessage != null -> Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage ?: "Unable to load personnel locator.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = PsaRed
                            )
                        }
                        else -> LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFFEAF6FF),
                                            Color(0xFFDDF0FF),
                                            Color(0xFFD5ECFF)
                                        )
                                    )
                                )
                                .padding(horizontal = 18.dp)
                        ) {
                            item { Spacer(modifier = Modifier.height(4.dp)) }
                            items(filteredRecords) { record ->
                                EmployeeRecordRow(
                                    record = record,
                                    onPhotoClick = { selectedEmployeeName = record.name }
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    val selectedEmployeeIndex = selectedEmployeeName?.let { selectedName ->
        filteredRecords.indexOfFirst { it.name == selectedName }
    } ?: -1

    if (selectedEmployeeIndex >= 0) {
        EmployeeDetailDialog(
            records = filteredRecords,
            initialPage = selectedEmployeeIndex,
            onDismiss = { selectedEmployeeName = null }
        )
    }
}

@Composable
private fun EmployeeLocatorHeader(
    total: Int,
    inCount: Int,
    outCount: Int,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF0E5B9B), Color(0xFF167DB7), Color(0xFF3AB3C5))
                )
            )
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EmployeeStatPill("Total", total.toString(), PsaGold)
                    EmployeeStatPill("In", inCount.toString(), Color(0xFF82D47B))
                    EmployeeStatPill("Out", outCount.toString(), PsaRed)
                }

                Surface(
                    modifier = Modifier.clickable(onClick = onDismiss),
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White.copy(alpha = 0.18f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.24f))
                ) {
                    Text(
                        text = "Close",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
private fun EmployeeStatPill(
    label: String,
    value: String,
    accentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color.White.copy(alpha = 0.16f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.18f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.72f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = accentColor,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun EmployeeFilterChip(
    label: String,
    selected: String,
    onClick: () -> Unit
) {
    val active = selected == label

    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = if (active) PsaBlue else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (active) PsaBlue else PsaNavy.copy(alpha = 0.12f)
        )
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (active) Color.White else PsaNavy,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmployeeRecordRow(
    record: EmployeeLocatorRecord,
    onPhotoClick: () -> Unit
) {
    val statusGroup = employeeStatusGroup(record.status)
    val statusColor = if (statusGroup == "IN") Color(0xFF3C9D5A) else Color(0xFFD65B4A)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onPhotoClick)
            .background(Color(0xFFEAF6FF).copy(alpha = 0.72f))
            .padding(horizontal = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EmployeePhotoAvatar(
                record = record,
                statusColor = statusColor,
                size = 62.dp,
                onClick = onPhotoClick
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF151B2A),
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = record.designation,
                    style = MaterialTheme.typography.bodySmall,
                    color = PsaNavy.copy(alpha = 0.64f),
                    modifier = Modifier.padding(top = 2.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (statusGroup == "OUT" && record.backToOffice.isNotBlank()) {
                    Text(
                        text = "Back: ${record.backToOffice}",
                        style = MaterialTheme.typography.labelSmall,
                        color = PsaGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(999.dp),
                color = statusColor.copy(alpha = 0.12f)
            ) {
                Text(
                    text = record.status,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = statusColor,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(start = 84.dp),
            color = PsaNavy.copy(alpha = 0.08f)
        )
    }
}

@Composable
private fun EmployeePhotoAvatar(
    record: EmployeeLocatorRecord,
    statusColor: Color,
    size: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (record.photoUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(record.photoUrl)
                    .crossfade(250)
                    .build(),
                contentDescription = "${record.name} photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                color = statusColor.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.24f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = employeeInitials(record.name),
                        style = MaterialTheme.typography.titleLarge,
                        color = statusColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun EmployeeDetailDialog(
    records: List<EmployeeLocatorRecord>,
    initialPage: Int,
    onDismiss: () -> Unit
) {
    if (records.isEmpty()) {
        return
    }

    val safeInitialPage = initialPage.coerceIn(0, records.lastIndex)
    val loopCycles = 80
    val loopingPageCount = if (records.size > 1) records.size * loopCycles else 1
    val loopingInitialPage = if (records.size > 1) {
        val midpoint = loopingPageCount / 2
        midpoint - (midpoint % records.size) + safeInitialPage
    } else {
        0
    }
    val pagerState = rememberPagerState(initialPage = loopingInitialPage) { loopingPageCount }

    fun employeeIndexForPage(page: Int): Int = if (records.isEmpty()) 0 else page % records.size

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.64f))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    pageSpacing = 14.dp
                ) { page ->
                    val pageOffset = (
                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                    ).absoluteValue.coerceIn(0f, 1f)
                    val scale = 0.92f + ((1f - pageOffset) * 0.08f)
                    val alpha = 0.58f + ((1f - pageOffset) * 0.42f)
                    val rotation = if (page < pagerState.currentPage) {
                        pageOffset * 6f
                    } else {
                        -pageOffset * 6f
                    }

                    EmployeeDetailPage(
                        record = records[employeeIndexForPage(page)],
                        onDismiss = onDismiss,
                        modifier = Modifier.graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                            rotationY = rotation
                            cameraDistance = 18 * density
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val currentEmployeeIndex = employeeIndexForPage(pagerState.currentPage)
                    repeat(records.size.coerceAtMost(7)) { index ->
                        val active = when {
                            records.size <= 7 -> currentEmployeeIndex == index
                            index == 0 -> currentEmployeeIndex == 0
                            index == 6 -> currentEmployeeIndex == records.lastIndex
                            else -> {
                                val scaledIndex = ((currentEmployeeIndex.toFloat() / records.lastIndex) * 4f).roundToInt() + 1
                                scaledIndex == index
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(if (active) 10.dp else 7.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = if (active) 0.95f else 0.34f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmployeeDetailPage(
    record: EmployeeLocatorRecord,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusGroup = employeeStatusGroup(record.status)
    val statusColor = if (statusGroup == "IN") Color(0xFF3C9D5A) else Color(0xFFD65B4A)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(34.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        statusColor.copy(alpha = 0.16f),
                                        Color(0xFFF5F9FC)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        EmployeePhotoAvatar(
                            record = record,
                            statusColor = statusColor,
                            size = 230.dp,
                            onClick = {}
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = record.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF151B2A),
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = record.designation,
                            style = MaterialTheme.typography.titleMedium,
                            color = PsaNavy.copy(alpha = 0.72f),
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp),
                        shape = RoundedCornerShape(22.dp),
                        color = Color(0xFFF7FAFD),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PsaNavy.copy(alpha = 0.08f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Current Status",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = PsaNavy.copy(alpha = 0.62f),
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = statusColor.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = record.status,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = statusColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            if (statusGroup == "OUT" && record.backToOffice.isNotBlank()) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Back to Office",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = PsaNavy.copy(alpha = 0.62f),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Expected return date",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = PsaNavy.copy(alpha = 0.48f)
                                        )
                                    }
                                    Text(
                                        text = record.backToOffice,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = PsaNavy,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .clickable(onClick = onDismiss),
                        shape = RoundedCornerShape(16.dp),
                        color = PsaBlue,
                        shadowElevation = 6.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Close Details",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
}

private fun employeeInitials(name: String): String {
    val parts = name
        .replace(",", " ")
        .split(" ")
        .filter { it.isNotBlank() }
    return parts
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar()?.toString() }
        .joinToString("")
        .ifBlank { "PS" }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DataPrivacySealPanel(
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    onOpenSeal: () -> Unit
) {
    Surface(
        modifier = modifier.combinedClickable(
            onClick = onOpenSeal,
            onLongClick = onOpenSeal
        ),
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.10f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.16f)),
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.10f),
                            PsaBlue.copy(alpha = 0.08f),
                            PsaNavy.copy(alpha = 0.12f)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(if (compact) 74.dp else 96.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.data_privacy_seal),
                    contentDescription = "Data Privacy Seal",
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .fillMaxHeight(0.82f),
                    contentScale = ContentScale.Fit
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Data Privacy Seal",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DataPrivacySealDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.62f))
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            val portraitLayout = maxHeight > maxWidth
            val dialogShape = RoundedCornerShape(if (portraitLayout) 24.dp else 32.dp)

            Surface(
                modifier = Modifier
                    .fillMaxWidth(if (portraitLayout) 0.94f else 0.72f)
                    .fillMaxHeight(if (portraitLayout) 0.94f else 0.92f),
                shape = dialogShape,
                color = Color(0xFFF8FBFF),
                shadowElevation = 18.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 1.dp,
                            color = PsaSky.copy(alpha = 0.35f),
                            shape = dialogShape
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
    folderBannerUri: String?,
    driveFolderMediaItems: List<DriveAnnouncementMedia>
) {
    val announcementItems = remember(
        folderBannerUri,
        driveFolderMediaItems
    ) {
        when {
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
        shape = RoundedCornerShape(10.dp),
        color = Color.White.copy(alpha = 0.30f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.46f)),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFEAF6FF).copy(alpha = 0.62f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Color.White.copy(alpha = 0.70f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFDFF2FF).copy(alpha = 0.55f))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFEAF7FF),
                                        Color(0xFFD0ECFF),
                                        Color(0xFFB8DFF8)
                                    )
                                )
                            )
                    )

                    AnnouncementCarousel(
                        mediaItems = announcementItems,
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(6.dp))
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.04f),
                                        Color.Transparent,
                                        Color(0xFF0E5B9B).copy(alpha = 0.04f)
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
                                color = Color.White.copy(alpha = 0.32f),
                                shape = RoundedCornerShape(6.dp)
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
                contentScale = ContentScale.Crop
            )
        }
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

private suspend fun loadCebuWeather(): WeatherSnapshot? = withContext(Dispatchers.IO) {
    runCatching {
        val endpoint =
            "https://api.open-meteo.com/v1/forecast" +
                "?latitude=10.3157" +
                "&longitude=123.8854" +
                "&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m" +
                "&timezone=Asia%2FManila"
        val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 6000
            readTimeout = 6000
        }

        connection.inputStream.bufferedReader().use { reader ->
            val current = JSONObject(reader.readText()).getJSONObject("current")
            WeatherSnapshot(
                temperatureC = current.getDouble("temperature_2m").roundToInt(),
                humidity = current.getInt("relative_humidity_2m"),
                windSpeedKmh = current.getDouble("wind_speed_10m").roundToInt(),
                condition = weatherConditionLabel(current.getInt("weather_code"))
            )
        }
    }.getOrNull()
}

private suspend fun submitFeedbackResponse(
    response: FeedbackResponse,
    surveyItems: List<String>
): Result<Unit> = withContext(Dispatchers.IO) {
    runCatching {
        if (FEEDBACK_SCRIPT_WEB_APP_URL.isBlank()) {
            error("Feedback endpoint is not configured yet.")
        }

        val ratingsJson = JSONObject()
        surveyItems.forEachIndexed { index, item ->
            ratingsJson.put(item, response.ratings[index])
        }

        val payload = JSONObject()
            .put("clientName", response.clientName)
            .put("services", JSONArray(response.services))
            .put("otherService", response.otherService)
            .put("ratings", ratingsJson)
            .put("comments", response.comments)
            .put("submittedAt", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))

        val connection = (URL(FEEDBACK_SCRIPT_WEB_APP_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            instanceFollowRedirects = true
            connectTimeout = 10000
            readTimeout = 10000
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
            setRequestProperty("Accept", "application/json")
        }

        connection.outputStream.use { output ->
            output.write(payload.toString().toByteArray(Charsets.UTF_8))
        }

        val responseCode = connection.responseCode
        if (responseCode !in 200..299) {
            error("Unable to submit feedback. Server returned $responseCode.")
        }
        connection.inputStream.close()
    }
}

private suspend fun loadEmployeeLocatorRecords(context: Context): Result<List<EmployeeLocatorRecord>> = withContext(Dispatchers.IO) {
    runCatching {
        val connection = (URL(EMPLOYEE_LOCATOR_SHEET_CSV_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10000
            readTimeout = 10000
        }

        val csv = connection.inputStream.bufferedReader().use { it.readText() }
        employeeLocatorCacheFile(context).writeText(csv)
        parseEmployeeLocatorRecords(context, csv)
    }
}

private suspend fun loadCachedEmployeeLocatorRecords(context: Context): List<EmployeeLocatorRecord> = withContext(Dispatchers.IO) {
    runCatching {
        val cacheFile = employeeLocatorCacheFile(context)
        if (!cacheFile.exists()) {
            emptyList()
        } else {
            parseEmployeeLocatorRecords(context, cacheFile.readText())
        }
    }.getOrDefault(emptyList())
}

private fun parseEmployeeLocatorRecords(
    context: Context,
    csv: String
): List<EmployeeLocatorRecord> {
    val photoUrlByKey = loadEmployeePhotoUrlMap(context)
        val rows = parseCsvRows(csv)
    return rows
        .drop(1)
        .mapNotNull { row ->
            val name = row.getOrNull(0)?.trim().orEmpty()
            val designation = row.getOrNull(1)?.trim().orEmpty()
            val status = row.getOrNull(2)?.trim().orEmpty()
            val backToOffice = row.getOrNull(3)?.trim().orEmpty()

            if (name.isBlank()) {
                null
            } else {
                EmployeeLocatorRecord(
                    name = name,
                    designation = designation.ifBlank { "Not specified" },
                    status = status.ifBlank { "IN" },
                    backToOffice = backToOffice
                )
            }
        }
        .map { record ->
            record.copy(photoUrl = photoUrlByKey[employeePhotoKeyFromName(record.name)])
        }
}

private fun employeeLocatorCacheFile(context: Context): File {
    return File(context.filesDir, EMPLOYEE_LOCATOR_CACHE_FILE)
}

private fun loadEmployeePhotoUrlMap(context: Context): MutableMap<String, String> {
    val photoUrlByKey = runCatching {
        context.assets.open("employee_photos/manifest.csv")
            .bufferedReader()
            .useLines { lines ->
                lines
                    .drop(1)
                    .mapNotNull { line ->
                        val columns = line.split(",", limit = 2)
                        val key = columns.getOrNull(0)?.trim().orEmpty()
                        val fileName = columns.getOrNull(1)?.trim().orEmpty()
                        if (key.isBlank() || fileName.isBlank()) {
                            null
                        } else {
                            key to "file:///android_asset/employee_photos/$fileName"
                        }
                    }
                    .toMap()
            }
    }.getOrDefault(emptyMap()).toMutableMap()

    loadCachedEmployeePhotoMap(context).forEach { (key, cachedPhoto) ->
        val file = employeePhotoCacheFile(context, cachedPhoto.fileName)
        if (file.exists()) {
            photoUrlByKey[key] = Uri.fromFile(file).toString()
        }
    }

    return photoUrlByKey
}

private fun syncEmployeePhotosFromDrive(
    context: Context,
    employeeKeys: Set<String>
): Map<String, String> {
    if (employeeKeys.isEmpty()) {
        return emptyMap()
    }

    return runCatching {
        val remotePhotos = loadEmployeePhotosFromDriveFolder()
            .filter { it.key in employeeKeys }
            .distinctBy { it.key }

        if (remotePhotos.isEmpty()) {
            return@runCatching emptyMap()
        }

        val cachedPhotos = loadCachedEmployeePhotoMap(context).toMutableMap()
        val syncedPhotoUrlByKey = mutableMapOf<String, String>()

        remotePhotos.forEach { photo ->
            val targetFile = employeePhotoCacheFile(context, photo.fileName)
            val cachedPhoto = cachedPhotos[photo.key]

            if (cachedPhoto?.fileId != photo.fileId || !targetFile.exists()) {
                downloadDriveFile(photo.fileId, targetFile)
                cachedPhotos[photo.key] = CachedEmployeePhoto(
                    fileId = photo.fileId,
                    fileName = photo.fileName
                )
            }

            if (targetFile.exists()) {
                syncedPhotoUrlByKey[photo.key] = Uri.fromFile(targetFile).toString()
            }
        }

        saveCachedEmployeePhotoMap(context, cachedPhotos)
        syncedPhotoUrlByKey
    }.getOrDefault(emptyMap())
}

private fun loadEmployeePhotosFromDriveFolder(): List<DriveEmployeePhoto> {
    val folderId = extractDriveFolderId(EMPLOYEE_LOCATOR_PHOTOS_FOLDER_URL) ?: return emptyList()
    val html = URL(EMPLOYEE_LOCATOR_PHOTOS_FOLDER_URL).readText()
    val photos = mutableListOf<DriveEmployeePhoto>()
    val escapedPattern = Regex(
        """\\x22([A-Za-z0-9_-]{20,})\\x22,\\x5b\\x22$folderId\\x22\\x5d,\\x22(.+?)\\x22,\\x22(.+?)\\x22"""
    )
    val htmlEntityPattern = Regex(
        """&quot;([A-Za-z0-9_-]{20,})&quot;\],null,null,null,&quot;(image/[^&]+)&quot;.*?&quot;([^&]+?\.(?:png|jpe?g|webp))&quot;""",
        setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
    )

    escapedPattern.findAll(html).forEach { match ->
        val fileId = match.groupValues[1]
        val name = decodeDriveEscapes(match.groupValues[2])
        val mimeType = decodeDriveEscapes(match.groupValues[3])
        createDriveEmployeePhoto(fileId, name, mimeType)?.let { photos += it }
    }

    htmlEntityPattern.findAll(html).forEach { match ->
        val fileId = match.groupValues[1]
        val mimeType = decodeDriveHtmlEntities(match.groupValues[2])
        val name = decodeDriveHtmlEntities(match.groupValues[3])
        createDriveEmployeePhoto(fileId, name, mimeType)?.let { photos += it }
    }

    return photos.distinctBy { it.key }
}

private fun createDriveEmployeePhoto(
    fileId: String,
    name: String,
    mimeType: String
): DriveEmployeePhoto? {
    if (!mimeType.startsWith("image/")) {
        return null
    }

    val key = employeePhotoKeyFromFileName(name)
    if (key.isBlank() || key.endsWith("|")) {
        return null
    }

    return DriveEmployeePhoto(
        fileId = fileId,
        name = name,
        mimeType = mimeType,
        key = key,
        fileName = employeePhotoCacheFileName(key, name, mimeType)
    )
}

private fun loadCachedEmployeePhotoMap(context: Context): Map<String, CachedEmployeePhoto> {
    val manifestFile = employeePhotoCacheFile(context, "manifest.csv")
    if (!manifestFile.exists()) {
        return emptyMap()
    }

    return runCatching {
        parseCsvRows(manifestFile.readText())
            .drop(1)
            .mapNotNull { row ->
                val key = row.getOrNull(0)?.trim().orEmpty()
                val fileId = row.getOrNull(1)?.trim().orEmpty()
                val fileName = row.getOrNull(2)?.trim().orEmpty()
                if (key.isBlank() || fileId.isBlank() || fileName.isBlank()) {
                    null
                } else {
                    key to CachedEmployeePhoto(fileId, fileName)
                }
            }
            .toMap()
    }.getOrDefault(emptyMap())
}

private fun saveCachedEmployeePhotoMap(
    context: Context,
    cachedPhotos: Map<String, CachedEmployeePhoto>
) {
    val manifestFile = employeePhotoCacheFile(context, "manifest.csv")
    manifestFile.parentFile?.mkdirs()
    val manifest = buildString {
        appendLine("key,fileId,filename")
        cachedPhotos.toSortedMap().forEach { (key, photo) ->
            appendLine("$key,${photo.fileId},${photo.fileName}")
        }
    }
    manifestFile.writeText(manifest)
}

private fun downloadDriveFile(fileId: String, targetFile: File) {
    targetFile.parentFile?.mkdirs()
    val connection = (URL("https://drive.google.com/uc?export=download&id=$fileId").openConnection() as HttpURLConnection).apply {
        requestMethod = "GET"
        connectTimeout = 15000
        readTimeout = 20000
    }

    connection.inputStream.use { input ->
        targetFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
}

private fun employeePhotoCacheFile(context: Context, fileName: String): File {
    return File(File(context.filesDir, EMPLOYEE_PHOTO_CACHE_DIR), fileName)
}

private fun employeePhotoCacheFileName(
    key: String,
    originalName: String,
    mimeType: String
): String {
    val extension = when {
        originalName.endsWith(".png", ignoreCase = true) || mimeType == "image/png" -> "png"
        originalName.endsWith(".webp", ignoreCase = true) || mimeType == "image/webp" -> "webp"
        else -> "jpg"
    }
    val safeKey = key
        .lowercase(Locale.ROOT)
        .replace("|", "_")
        .filter { it.isLetterOrDigit() || it == '_' }
    return "$safeKey.$extension"
}

private fun parseCsvRows(csv: String): List<List<String>> {
    val rows = mutableListOf<List<String>>()
    val currentRow = mutableListOf<String>()
    val currentValue = StringBuilder()
    var inQuotes = false
    var index = 0

    while (index < csv.length) {
        val char = csv[index]
        when {
            char == '"' && inQuotes && index + 1 < csv.length && csv[index + 1] == '"' -> {
                currentValue.append('"')
                index++
            }
            char == '"' -> inQuotes = !inQuotes
            char == ',' && !inQuotes -> {
                currentRow += currentValue.toString()
                currentValue.clear()
            }
            (char == '\n' || char == '\r') && !inQuotes -> {
                if (char == '\r' && index + 1 < csv.length && csv[index + 1] == '\n') {
                    index++
                }
                currentRow += currentValue.toString()
                currentValue.clear()
                if (currentRow.any { it.isNotBlank() }) {
                    rows += currentRow.toList()
                }
                currentRow.clear()
            }
            else -> currentValue.append(char)
        }
        index++
    }

    currentRow += currentValue.toString()
    if (currentRow.any { it.isNotBlank() }) {
        rows += currentRow.toList()
    }

    return rows
}

private fun employeeStatusGroup(status: String): String {
    return if (status.trim().uppercase(Locale.getDefault()).startsWith("IN")) {
        "IN"
    } else {
        "OUT"
    }
}

private fun employeePhotoKeyFromName(name: String): String {
    val parts = name.split(",", limit = 2)
    val surname = parts.getOrNull(0).orEmpty().trim()
    val givenNames = parts.getOrNull(1).orEmpty().trim()
    val initial = givenNames.firstOrNull { it.isLetter() }?.uppercaseChar()?.toString().orEmpty()
    return "${normalizeEmployeeNamePart(surname)}|$initial"
}

private fun employeePhotoKeyFromFileName(fileName: String): String {
    val nameWithoutExtension = fileName.substringBeforeLast('.', fileName)
    val parts = nameWithoutExtension.split(",", limit = 2)
    val surname = parts.getOrNull(0).orEmpty().trim()
    val initial = parts.getOrNull(1).orEmpty().firstOrNull { it.isLetter() }?.uppercaseChar()?.toString().orEmpty()
    return "${normalizeEmployeeNamePart(surname)}|$initial"
}

private fun normalizeEmployeeNamePart(value: String): String {
    val withoutDiacritics = Normalizer
        .normalize(value, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")

    return withoutDiacritics
        .uppercase(Locale.getDefault())
        .filter { it.isLetterOrDigit() }
}

private fun weatherConditionLabel(code: Int): String {
    return when (code) {
        0 -> "Clear"
        1, 2 -> "Partly cloudy"
        3 -> "Cloudy"
        45, 48 -> "Fog"
        51, 53, 55, 56, 57 -> "Drizzle"
        61, 63, 65, 66, 67 -> "Rain"
        71, 73, 75, 77 -> "Snow"
        80, 81, 82 -> "Showers"
        85, 86 -> "Snow showers"
        95, 96, 99 -> "Thunderstorm"
        else -> "Weather"
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
        .replace(Regex("""\\x([0-9A-Fa-f]{2})""")) { match ->
            match.groupValues[1].toInt(16).toChar().toString()
        }
        .replace("\\/", "/")
        .replace("\\u003d", "=")
        .replace("\\u0026", "&")
}

private fun decodeDriveHtmlEntities(value: String): String {
    return value
        .replace("&quot;", "\"")
        .replace("&#39;", "'")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
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
    val isDocumentModule = service.title == "Citizens Charter"
    val isAboutModule = service.title == "About Us"
    val isNationalIdModule = service.title == "National ID System"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.32f))
                .padding(horizontal = 14.dp, vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            val compactPortrait = maxWidth < 520.dp
            val portraitLayout = maxHeight > maxWidth
            val modalWidthFraction = when {
                compactPortrait -> 1f
                portraitLayout -> 0.94f
                else -> 0.86f
            }
            val modalHeightFraction = if (maxHeight < 760.dp) 0.97f else 0.94f
            val contentHorizontalPadding = when {
                compactPortrait -> 12.dp
                portraitLayout -> 16.dp
                isNationalIdModule -> 18.dp
                else -> 18.dp
            }
            val contentVerticalPadding = if (compactPortrait) 14.dp else 18.dp

            Surface(
                modifier = Modifier
                    .fillMaxWidth(modalWidthFraction)
                    .fillMaxHeight(modalHeightFraction),
                shape = RoundedCornerShape(if (compactPortrait) 24.dp else 30.dp),
                color = Color.Transparent,
                tonalElevation = 12.dp,
                shadowElevation = 24.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFEAF7FF),
                                    Color(0xFFDFF2FF),
                                    Color(0xFFF7FBFF)
                                )
                            )
                        )
                ) {
                    ServiceModalHeader(
                        service = service,
                        onDismiss = onDismiss
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .then(if (isDocumentModule) Modifier else Modifier.verticalScroll(scrollState))
                            .padding(
                                horizontal = contentHorizontalPadding,
                                vertical = contentVerticalPadding
                            )
                    ) {
                        when {
                            isAboutModule -> {
                                AboutUsContent()
                            }

                            isDocumentModule -> {
                                CitizensCharterPdfContent(accentColor = service.accentColor)
                            }

                            isNationalIdModule -> {
                                NationalIdCebuContent(accentColor = service.accentColor)
                            }

                            else -> {
                                ServiceModalOverview(
                                    content = service.overview,
                                    accentColor = service.accentColor
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                service.sections.forEach { section ->
                                    ServiceModalSection(
                                        section = section,
                                        accentColor = service.accentColor
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }

                        if (!isDocumentModule && !isNationalIdModule) {
                            ServiceModalFooter(
                                note = service.footerNote,
                                accentColor = service.accentColor
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.78f),
                                        Color(0xFFF7FBFF).copy(alpha = 0.96f)
                                    )
                                )
                            )
                            .padding(horizontal = 18.dp, vertical = if (isNationalIdModule) 10.dp else 14.dp),
                        horizontalArrangement = if (isNationalIdModule) Arrangement.Center else Arrangement.End
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .height(if (isNationalIdModule) 44.dp else 48.dp)
                                .then(
                                    if (isNationalIdModule) {
                                        Modifier.fillMaxWidth(if (compactPortrait) 0.74f else 0.52f)
                                    } else {
                                        Modifier
                                    }
                                ),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isNationalIdModule) Color(0xFF0D5DA8) else PsaBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Back to Dashboard",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceModalHeader(
    service: DashboardService,
    onDismiss: () -> Unit
) {
    val isNationalIdModule = service.title == "National ID System"
    val headerBackground = if (isNationalIdModule) {
        Brush.linearGradient(listOf(Color(0xFF0F6EA4), Color(0xFF0F6EA4)))
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF0A4A89),
                Color(0xFF1377B9),
                service.accentColor.copy(alpha = 0.84f)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerBackground)
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ServiceModalIcon(service = service)
                Column(modifier = Modifier.padding(start = 13.dp)) {
                    Text(
                        text = service.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 28.sp
                    )
                    if (service.title != "National ID System") {
                        Text(
                            text = service.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.86f),
                            modifier = Modifier.padding(top = 4.dp),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .height(if (isNationalIdModule) 44.dp else 40.dp)
                    .clickable(onClick = onDismiss),
                shape = RoundedCornerShape(999.dp),
                color = if (isNationalIdModule) Color.White.copy(alpha = 0.92f) else Color.White.copy(alpha = 0.18f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isNationalIdModule) Color.White.copy(alpha = 0.86f) else Color.White.copy(alpha = 0.28f)
                )
            ) {
                Text(
                    text = "Close",
                    modifier = Modifier.padding(horizontal = if (isNationalIdModule) 18.dp else 16.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isNationalIdModule) Color(0xFF0D4268) else Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
private fun ServiceModalIcon(service: DashboardService) {
    Box(
        modifier = Modifier.size(66.dp),
        contentAlignment = Alignment.Center
    ) {
        if (service.iconResId != null) {
            Image(
                painter = painterResource(id = service.iconResId),
                contentDescription = service.title,
                modifier = Modifier.size(58.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            Icon(
                imageVector = service.icon,
                contentDescription = service.title,
                tint = Color.White,
                modifier = Modifier.size(42.dp)
            )
        }
    }
}

@Composable
private fun ServiceModalOverview(
    content: String,
    accentColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.78f),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaBlue.copy(alpha = 0.10f)),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(58.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(accentColor)
            )
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.titleMedium,
                    color = PsaNavy,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = PsaNavy.copy(alpha = 0.82f),
                    modifier = Modifier.padding(top = 6.dp),
                    lineHeight = 23.sp
                )
            }
        }
    }
}

@Composable
private fun ServiceModalSection(
    section: ServiceSection,
    accentColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.70f),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaBlue.copy(alpha = 0.08f))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(accentColor.copy(alpha = 0.10f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(accentColor, CircleShape)
                )
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = PsaNavy,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            section.items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.labelLarge,
                        color = accentColor,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.width(26.dp)
                    )
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        color = PsaNavy.copy(alpha = 0.88f),
                        lineHeight = 22.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (index != section.items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = PsaBlue.copy(alpha = 0.08f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ServiceModalFooter(
    note: String,
    accentColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        shape = RoundedCornerShape(18.dp),
        color = accentColor.copy(alpha = 0.11f)
    ) {
        Text(
            text = note,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = PsaNavy.copy(alpha = 0.82f),
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun NationalIdCebuContent(accentColor: Color) {
    val panels = remember {
        listOf(
            NationalIdInfoPanel(
                title = "Supporting Documents",
                kicker = "Prepare first",
                items = emptyList()
            ),
            NationalIdInfoPanel(
                title = "How to Register to the National ID",
                kicker = "Step-by-step guide",
                items = emptyList()
            ),
            NationalIdInfoPanel(
                title = "Registration Centers",
                kicker = "Where to proceed",
                items = listOf(
                    "National ID Fixed Registration Center: 3rd Floor Gaisano Capital South, Corner Colon and Leon Kilat Street, Cebu City. Appointment booking is available through nationalidappt-psacebu.com. Office hours: Monday to Friday, 8:00 AM - 5:00 PM.",
                    "Cebu Exchange Tower: 2/F Cebu Exchange Tower, Salinas Drive, Lahug, Cebu City, across Cebu IT Park. Walk-ins are accepted on a first come, first served basis. Office hours: Monday to Sunday, 8:00 PM - 12:00 Midnight.",
                    "Mandaue City eGovPH Serbisyo Hub: Ground Floor, PCO Building, Subangdaku, Mandaue City. Booking or access is through the eGovPH App. Office hours: Monday to Thursday, 7:00 AM - 6:00 PM."
                )
            ),
            NationalIdInfoPanel(
                title = "Book an Appointment",
                kicker = "Reserve a slot",
                items = listOf(
                    "National ID Fixed Registration Center: scan the QR below or open nationalidappt-psacebu.com. This online appointment site is only for the Gaisano Capital South fixed registration center.",
                    "Mandaue City eGovPH Serbisyo Hub: use the eGovPH App for booking or service access before proceeding to the hub.",
                    "Cebu Exchange Tower: no online appointment needed; proceed as walk-in and follow first come, first served queueing."
                )
            ),
            NationalIdInfoPanel(
                title = "Public Advisory",
                kicker = "PhilSys updates",
                items = emptyList()
            ),
            NationalIdInfoPanel(
                title = "Frequently Asked Questions",
                kicker = "PhilSys FAQ",
                items = emptyList()
            ),
            NationalIdInfoPanel(
                title = "Schedule of Mobile Registrations",
                kicker = "Off-site schedule",
                items = emptyList()
            ),
            NationalIdInfoPanel(
                title = "Already Registered",
                kicker = "Status and updates",
                items = listOf(
                    "Do not register again if you already completed Step 2 registration.",
                    "Use the Digital National ID or ePhilID if available while waiting for the physical card.",
                    "For corrections, updating, or replacement concerns, ask the registration center staff for the proper current process."
                )
            ),
            NationalIdInfoPanel(
                title = "Contact Us",
                kicker = "PhilSys support",
                items = emptyList()
            )
        )
    }
    var expandedPanels by rememberSaveable {
        mutableStateOf(emptyList<String>())
    }
    var nationalIdExpanded by rememberSaveable { mutableStateOf(false) }

    PhilSysNationalIdAccordion(
        expanded = nationalIdExpanded,
        onClick = { nationalIdExpanded = !nationalIdExpanded }
    )

    Spacer(modifier = Modifier.height(10.dp))

    panels.forEachIndexed { index, panel ->
        NationalIdExpandablePanel(
            panel = panel,
            expanded = panel.title in expandedPanels,
            accentColor = accentColor,
            onClick = {
                expandedPanels = if (panel.title in expandedPanels) {
                    expandedPanels - panel.title
                } else {
                    expandedPanels + panel.title
                }
            }
        )
        if (index == panels.lastIndex) {
            Spacer(modifier = Modifier.height(10.dp))
        } else {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

    ServiceModalFooter(
        note = "Registration center schedules and appointment rules may change. Please follow the latest PSA Cebu and PhilSys advisories posted in the office.",
        accentColor = accentColor
    )
}

@Composable
private fun PhilSysNationalIdAccordion(
    expanded: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.92f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD4E7F4)),
        shadowElevation = 5.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFD65B),
                                Color(0xFFF7C844),
                                Color(0xFFFFE38A)
                            )
                        )
                    )
                    .clickable(onClick = onClick)
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "The National ID",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF0D4D82),
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.36f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (expanded) "-" else "+",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF0D4D82),
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {
                PhilSysNationalIdWebContent()
            }
        }
    }
}

@Composable
@SuppressLint("SetJavaScriptEnabled")
private fun PhilSysNationalIdWebContent() {
    var previewImageUrl by remember { mutableStateOf<String?>(null) }
    var contentHeight by remember { mutableStateOf(2100.dp) }

    previewImageUrl?.let { imageUrl ->
        PhilSysImagePreviewDialog(
            imageUrl = imageUrl,
            onDismiss = { previewImageUrl = null }
        )
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(contentHeight),
        factory = { context ->
            WebView(context).apply {
                addJavascriptInterface(
                    PhilSysImageBridge { imageIndex ->
                        previewImageUrl = philsysNationalIdImageUrls.getOrNull(imageIndex)
                    },
                    "PhilSysImageBridge"
                )
                addJavascriptInterface(
                    PhilSysHeightBridge { measuredHeight ->
                        contentHeight = measuredHeight.coerceAtLeast(1).dp
                    },
                    "PhilSysHeightBridge"
                )
                webViewClient = WebViewClient()
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false
                overScrollMode = WebView.OVER_SCROLL_NEVER
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                settings.loadsImagesAutomatically = true
                settings.blockNetworkImage = false
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                loadDataWithBaseURL(
                    "https://philsys.gov.ph/the-national-id/",
                    philsysNationalIdHtml(),
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        update = { webView ->
            if (webView.url == null) {
                webView.loadDataWithBaseURL(
                    "https://philsys.gov.ph/the-national-id/",
                    philsysNationalIdHtml(),
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        }
    )
}

private class PhilSysImageBridge(
    private val onImageClick: (Int) -> Unit
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun openImage(imageIndex: Int) {
        mainHandler.post {
            onImageClick(imageIndex)
        }
    }
}

private class PhilSysHeightBridge(
    private val onHeightMeasured: (Int) -> Unit
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun updateHeight(height: Int) {
        mainHandler.post {
            onHeightMeasured(height)
        }
    }
}

@Composable
@SuppressLint("SetJavaScriptEnabled")
private fun PhilSysImagePreviewDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.72f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDismiss
                )
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .aspectRatio(philsysImagePreviewAspectRatio(imageUrl))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Transparent)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {}
                    )
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
                    factory = { context ->
                        WebView(context).apply {
                            tag = imageUrl
                            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                            webViewClient = WebViewClient()
                            isVerticalScrollBarEnabled = false
                            isHorizontalScrollBarEnabled = false
                            overScrollMode = WebView.OVER_SCROLL_NEVER
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            settings.javaScriptEnabled = false
                            settings.domStorageEnabled = true
                            settings.cacheMode = WebSettings.LOAD_DEFAULT
                            settings.loadsImagesAutomatically = true
                            settings.blockNetworkImage = false
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false
                            loadUrl(imageUrl)
                        }
                    },
                    update = { webView ->
                        if (webView.tag != imageUrl) {
                            webView.tag = imageUrl
                            webView.loadUrl(imageUrl)
                        }
                    }
                )
            }
        }
    }
}

private fun philsysImagePreviewAspectRatio(imageUrl: String): Float {
    return when {
        imageUrl.contains("2.5-1536x466", ignoreCase = true) -> 1536f / 466f
        imageUrl.contains("National-ID-PH-hashtag", ignoreCase = true) -> 300f / 45f
        imageUrl.endsWith("/1.png", ignoreCase = true) -> 1.82f
        imageUrl.endsWith("/3.png", ignoreCase = true) -> 1.35f
        imageUrl.endsWith("/4.png", ignoreCase = true) -> 1.35f
        imageUrl.endsWith("/5.5-768x404.png", ignoreCase = true) -> 768f / 404f
        imageUrl.endsWith("/7.png", ignoreCase = true) -> 1.35f
        imageUrl.endsWith("/8.png", ignoreCase = true) -> 1.35f
        imageUrl.endsWith("/9.5.png", ignoreCase = true) -> 1.90f
        imageUrl.endsWith("/11.png", ignoreCase = true) -> 1.35f
        else -> 1.6f
    }
}

private fun philsysNationalIdHtml(): String {
    return """
        <!doctype html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
            <style>
                * { box-sizing: border-box; }
                html, body {
                    margin: 0;
                    padding: 0;
                    background: transparent;
                    font-family: Arial, Helvetica, sans-serif;
                    color: #123f82;
                }
                .page {
                    width: 100%;
                    padding: 16px 16px 0;
                }
                .title-bar {
                    background: #ffca18;
                    color: #123f82;
                    border-radius: 16px;
                    text-align: center;
                    font-size: 25px;
                    font-weight: 800;
                    line-height: 1.15;
                    padding: 14px 12px;
                    margin: 8px 0 16px;
                    letter-spacing: 0;
                }
                .image-card {
                    width: 100%;
                    background: transparent;
                    border-radius: 4px;
                    margin: 0 0 14px;
                    padding: 0;
                    overflow: hidden;
                    cursor: pointer;
                    border: 0;
                    line-height: 0;
                }
                .image-card.white {
                    background: transparent;
                    border: 0;
                }
                img {
                    display: block;
                    width: 100%;
                    height: auto;
                    margin: 0 auto;
                }
                .paragraph {
                    font-size: 19px;
                    line-height: 1.58;
                    color: #123f82;
                    margin: 12px 0 18px;
                    letter-spacing: 0;
                    text-align: justify;
                    text-justify: inter-word;
                }
                .paragraph b {
                    font-weight: 800;
                }
                .hashtag {
                    display: block;
                    width: min(260px, 62%);
                    margin: 16px auto 4px;
                }
            </style>
            <script>
                document.addEventListener('DOMContentLoaded', function() {
                    function reportHeight() {
                        if (window.PhilSysHeightBridge) {
                            var page = document.querySelector('.page');
                            var rect = page ? page.getBoundingClientRect() : null;
                            var height = Math.ceil(rect ? rect.height : (document.body.getBoundingClientRect().height || 1));
                            window.PhilSysHeightBridge.updateHeight(height);
                        }
                    }

                    document.querySelectorAll('.image-card img, img.hashtag').forEach(function(img) {
                        img.addEventListener('click', function() {
                            if (window.PhilSysImageBridge) {
                                window.PhilSysImageBridge.openImage(parseInt(img.dataset.imageIndex, 10));
                            }
                        });
                        img.addEventListener('load', reportHeight);
                        img.addEventListener('error', reportHeight);
                    });
                    reportHeight();
                    setTimeout(reportHeight, 500);
                    setTimeout(reportHeight, 1200);
                });
            </script>
        </head>
        <body>
            <main class="page">
                <section>
                    <div class="image-card white">
                        <img data-image-index="0" src="https://philsys.gov.ph/wp-content/uploads/2025/09/1.png" alt="Valid and accepted National ID formats">
                    </div>
                </section>

                <section>
                    <div class="title-bar">National ID Card</div>
                    <div class="image-card">
                        <img data-image-index="1" src="https://philsys.gov.ph/wp-content/uploads/2025/09/2.5-1536x466.png" alt="National ID card front and back">
                    </div>
                    <p class="paragraph">The <b>National ID card</b> is made of polycarbonate and contains a quick response (QR) code that may be scanned for authentication.</p>
                    <div class="image-card">
                        <img data-image-index="2" src="https://philsys.gov.ph/wp-content/uploads/2025/09/3.png" alt="National ID card front features">
                    </div>
                    <div class="image-card">
                        <img data-image-index="3" src="https://philsys.gov.ph/wp-content/uploads/2025/09/4.png" alt="National ID card back features">
                    </div>
                </section>

                <section>
                    <div class="title-bar">Digital National ID</div>
                    <div class="image-card">
                        <img data-image-index="4" src="https://philsys.gov.ph/wp-content/uploads/2025/09/5.5-768x404.png" alt="Digital National ID">
                    </div>
                    <p class="paragraph">The digital version of the National ID, the <b>Digital National ID</b>, may be accessed via the eGovPH mobile app.</p>
                    <div class="image-card">
                        <img data-image-index="5" src="https://philsys.gov.ph/wp-content/uploads/2025/09/7.png" alt="Digital National ID front details">
                    </div>
                    <div class="image-card">
                        <img data-image-index="6" src="https://philsys.gov.ph/wp-content/uploads/2025/09/8.png" alt="Digital National ID back details">
                    </div>
                </section>

                <section>
                    <div class="title-bar">National ID in Paper Format</div>
                    <div class="image-card">
                        <img data-image-index="7" src="https://philsys.gov.ph/wp-content/uploads/2025/09/9.5.png" alt="National ID in paper format">
                    </div>
                    <p class="paragraph">The <b>National ID in paper format</b> can be claimed from National ID registration centers free of charge. Like the National ID card, it has a QR code that may be used for authentication.</p>
                    <div class="image-card">
                        <img data-image-index="8" src="https://philsys.gov.ph/wp-content/uploads/2025/09/11.png" alt="National ID paper format details">
                    </div>
                </section>

                <img data-image-index="9" class="hashtag" src="https://philsys.gov.ph/wp-content/uploads/2025/01/National-ID-PH-hashtag-300x45.png" alt="#NationalIDPH">
            </main>
        </body>
        </html>
    """.trimIndent()
}

@Composable
private fun NationalIdWebsiteContent(accentColor: Color) {
    NationalIdWebsiteSectionTitle(title = "The National ID")

    Spacer(modifier = Modifier.height(12.dp))

    NationalIdAcceptedBanner()

    Spacer(modifier = Modifier.height(16.dp))

    NationalIdWebsiteSectionTitle(title = "National ID Card")
    NationalIdWebsiteVisualPanel {
        NationalIdPhysicalCardPreview()
    }
    NationalIdWebsiteParagraph(
        text = "The National ID card is made of polycarbonate and contains a quick response (QR) code that may be scanned for authentication."
    )
    NationalIdFeatureBoard(
        title = "National ID Card - Front",
        features = listOf(
            "Republic of the Philippines Seal",
            "National ID Card Number",
            "Personalized Picture",
            "Ghost Image",
            "Personalized Data and Labels",
            "PhilSys Registry Office Seal",
            "Microprint and Guilloche Design",
            "Color Shifting Print"
        ),
        accentColor = accentColor
    )
    NationalIdFeatureBoard(
        title = "National ID Card - Back",
        features = listOf(
            "QR Code",
            "Personalized Data and Labels",
            "Card Serial Number",
            "Latent Image",
            "UV fluorescent images",
            "National ID Number microprinted"
        ),
        accentColor = accentColor
    )

    Spacer(modifier = Modifier.height(16.dp))

    NationalIdWebsiteSectionTitle(title = "Digital National ID")
    NationalIdWebsiteVisualPanel {
        NationalIdDigitalPreview()
    }
    NationalIdWebsiteParagraph(
        text = "The digital version of the National ID, the Digital National ID, may be accessed via the eGovPH mobile app."
    )
    NationalIdFeatureBoard(
        title = "Digital National ID - Front",
        features = listOf(
            "National ID Card Number",
            "Front-facing Photograph",
            "Present Address",
            "Full Name and Date of Birth",
            "Quick Response (QR) code"
        ),
        accentColor = accentColor
    )
    NationalIdFeatureBoard(
        title = "Digital National ID - Back",
        features = listOf(
            "Sex",
            "Blood Type",
            "Marital Status",
            "Place of Birth",
            "QR code"
        ),
        accentColor = accentColor
    )

    Spacer(modifier = Modifier.height(16.dp))

    NationalIdWebsiteSectionTitle(title = "National ID in Paper Format")
    NationalIdWebsiteVisualPanel {
        NationalIdPaperPreview()
    }
    NationalIdWebsiteParagraph(
        text = "The National ID in paper format can be claimed from National ID registration centers free of charge. Like the National ID card, it has a QR code that may be used for authentication."
    )
    NationalIdFeatureBoard(
        title = "Paper Format Details",
        features = listOf(
            "Demographic Information",
            "Front-facing Photograph",
            "National ID Card Number",
            "QR Code",
            "Reminders",
            "Generation Date",
            "Authentication Instruction",
            "Cut-here Instructions"
        ),
        accentColor = accentColor
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(14.dp),
        color = Color.White.copy(alpha = 0.72f)
    ) {
        Text(
            text = "#NationalIDPH",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            style = MaterialTheme.typography.titleMedium,
            color = PsaBlue,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NationalIdWebsiteSectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5CB58).copy(alpha = 0.46f)),
        shadowElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFDA65),
                            Color(0xFFF4C643),
                            Color(0xFFFFE89A)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF0D4D82),
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun NationalIdWebsiteVisualPanel(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaBlue.copy(alpha = 0.10f))
    ) {
        Box(
            modifier = Modifier.padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
private fun NationalIdWebsiteParagraph(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
        style = MaterialTheme.typography.bodyLarge,
        color = PsaBlue,
        lineHeight = 25.sp
    )
}

@Composable
private fun NationalIdPhysicalCardPreview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NationalIdCardFace(
            title = "Front",
            details = listOf("National ID No.", "Photo", "Name", "Date of Birth", "Address"),
            modifier = Modifier.weight(1f)
        )
        NationalIdCardFace(
            title = "Back",
            details = listOf("QR Code", "Sex", "Blood Type", "Birthplace", "Serial No."),
            modifier = Modifier.weight(1f),
            showQr = true
        )
    }
}

@Composable
private fun NationalIdCardFace(
    title: String,
    details: List<String>,
    modifier: Modifier = Modifier,
    showQr: Boolean = false
) {
    Surface(
        modifier = modifier.aspectRatio(1.56f),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF4FBFF),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaBlue.copy(alpha = 0.22f)),
        shadowElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFE3F5FF),
                            Color(0xFFFFF1F1)
                        )
                    )
                )
                .padding(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = PsaBlue,
                fontWeight = FontWeight.ExtraBold
            )
            if (showQr) {
                NationalIdQrPreview(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(54.dp)
                )
            } else {
                Surface(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = PsaBlue.copy(alpha = 0.10f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("ID", color = PsaBlue, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .align(if (showQr) Alignment.CenterStart else Alignment.CenterEnd)
                    .fillMaxWidth(0.56f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                details.take(4).forEach { detail ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(99.dp))
                            .background(PsaBlue.copy(alpha = 0.18f))
                    )
                    Text(
                        text = detail,
                        style = MaterialTheme.typography.labelSmall,
                        color = PsaNavy.copy(alpha = 0.68f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun NationalIdDigitalPreview() {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.82f)
            .aspectRatio(2.05f),
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF111827),
        shadowElevation = 5.dp
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(18.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NationalIdCardFace(
                        title = "Digital ID",
                        details = listOf("National ID No.", "Photo", "Full Name", "Date of Birth"),
                        modifier = Modifier.weight(1f),
                        showQr = true
                    )
                    Surface(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .width(42.dp)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFEAF3FF)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "QR",
                                style = MaterialTheme.typography.labelMedium,
                                color = PsaBlue,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NationalIdPaperPreview() {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.78f)
            .aspectRatio(1.42f),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaNavy.copy(alpha = 0.18f)),
        shadowElevation = 5.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PsaNavy.copy(alpha = 0.10f))
                    )
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(99.dp))
                                .background(PsaNavy.copy(alpha = 0.16f))
                        )
                    }
                }
                NationalIdQrPreview(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }
            HorizontalDivider(color = PsaNavy.copy(alpha = 0.14f), modifier = Modifier.padding(vertical = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(2) {
                    Column(modifier = Modifier.weight(1f)) {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .padding(bottom = 2.dp)
                                    .clip(RoundedCornerShape(99.dp))
                                    .background(PsaNavy.copy(alpha = 0.13f))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NationalIdFeatureBoard(
    title: String,
    features: List<String>,
    accentColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.78f),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaBlue.copy(alpha = 0.10f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = PsaNavy,
                fontWeight = FontWeight.ExtraBold
            )
            features.forEachIndexed { index, feature ->
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        modifier = Modifier.size(24.dp),
                        shape = CircleShape,
                        color = PsaGold
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = PsaBlue,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                    Text(
                        text = feature,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = PsaNavy.copy(alpha = 0.84f),
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun NationalIdGuidePanel(
    expanded: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    val formats = remember {
        listOf(
            NationalIdFormatCard(
                title = "National ID Card",
                description = "A physical polycarbonate card with a QR code for authentication.",
                tag = "Physical"
            ),
            NationalIdFormatCard(
                title = "Digital National ID",
                description = "A digital version that may be accessed through the eGovPH mobile app.",
                tag = "Mobile"
            ),
            NationalIdFormatCard(
                title = "National ID in Paper Format",
                description = "A paper format that may be claimed from registration centers free of charge and also includes a QR code.",
                tag = "Paper"
            )
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.90f),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.30f)),
        shadowElevation = 5.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                PsaGold,
                                Color(0xFFFFD94D)
                            )
                        )
                    )
                    .padding(horizontal = 18.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "The National ID",
                        style = MaterialTheme.typography.headlineSmall,
                        color = PsaBlue,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "#NationalIDPH",
                        style = MaterialTheme.typography.labelLarge,
                        color = PsaBlue.copy(alpha = 0.78f),
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Text(
                    text = if (expanded) "-" else "+",
                    style = MaterialTheme.typography.headlineMedium,
                    color = PsaBlue,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(38.dp)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(16.dp)) {
                    NationalIdAcceptedBanner()

                    Spacer(modifier = Modifier.height(12.dp))

                    formats.forEach { format ->
                        NationalIdFormatRow(
                            format = format,
                            accentColor = accentColor
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    NationalIdSecurityFeatureStrip(accentColor = accentColor)
                }
            }
        }
    }
}

@Composable
private fun NationalIdAcceptedBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF7FBFF),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaBlue.copy(alpha = 0.20f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ano man ang format,",
                style = MaterialTheme.typography.titleLarge,
                color = PsaBlue,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = PsaGold,
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Text(
                    text = "VALID at ACCEPTED 'yan!",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = PsaBlue,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NationalIdMiniCard(label = "Card", color = PsaBlue)
                NationalIdPhonePreview()
                NationalIdMiniCard(label = "Paper", color = Color(0xFF64748B))
            }
        }
    }
}

@Composable
private fun NationalIdMiniCard(
    label: String,
    color: Color
) {
    Surface(
        modifier = Modifier
            .width(96.dp)
            .height(58.dp),
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.35f)),
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(46.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(color.copy(alpha = 0.24f))
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = color,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 7.dp)
            )
        }
    }
}

@Composable
private fun NationalIdPhonePreview() {
    Surface(
        modifier = Modifier
            .width(92.dp)
            .height(58.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF101827),
        shadowElevation = 4.dp
    ) {
        Box(modifier = Modifier.padding(6.dp)) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(10.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NationalIdQrPreview(modifier = Modifier.size(26.dp))
                    Text(
                        text = "Digital ID",
                        style = MaterialTheme.typography.labelSmall,
                        color = PsaBlue,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NationalIdQrPreview(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .border(1.dp, PsaNavy.copy(alpha = 0.28f), RoundedCornerShape(4.dp))
            .padding(3.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            repeat(4) { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(4) { column ->
                        Box(
                            modifier = Modifier
                                .size(3.dp)
                                .background(
                                    if ((row + column) % 2 == 0 || row == column) PsaNavy else Color.Transparent
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NationalIdFormatRow(
    format: NationalIdFormatCard,
    accentColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF9FCFF),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaBlue.copy(alpha = 0.10f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = RoundedCornerShape(16.dp),
                color = accentColor.copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = format.tag.take(1),
                        style = MaterialTheme.typography.titleLarge,
                        color = accentColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = format.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = PsaNavy,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = format.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PsaNavy.copy(alpha = 0.74f),
                    lineHeight = 19.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = PsaGold.copy(alpha = 0.22f)
            ) {
                Text(
                    text = format.tag,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = PsaBlue,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
private fun NationalIdSecurityFeatureStrip(accentColor: Color) {
    val features = listOf(
        "QR code authentication",
        "Front-facing photograph",
        "National ID number",
        "Demographic information",
        "Security print features"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = accentColor.copy(alpha = 0.10f)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Details commonly shown",
                style = MaterialTheme.typography.titleSmall,
                color = PsaNavy,
                fontWeight = FontWeight.ExtraBold
            )
            features.forEach { feature ->
                Row(
                    modifier = Modifier.padding(top = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(accentColor, CircleShape)
                    )
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PsaNavy.copy(alpha = 0.82f),
                        modifier = Modifier.padding(start = 9.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NationalIdExpandablePanel(
    panel: NationalIdInfoPanel,
    expanded: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    var selectedMapLocation by remember { mutableStateOf<NationalIdMapLocation?>(null) }

    selectedMapLocation?.let { location ->
        NationalIdLocationMapDialog(
            location = location,
            onDismiss = { selectedMapLocation = null }
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (expanded) Color(0xFFDFF1FB).copy(alpha = 0.92f) else Color(0xFFEAF6FC).copy(alpha = 0.76f),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (expanded) accentColor.copy(alpha = 0.30f) else Color(0xFF9EC7DF).copy(alpha = 0.30f)
        ),
        shadowElevation = if (expanded) 2.dp else 0.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFF2FAFF).copy(alpha = if (expanded) 0.86f else 0.62f),
                                Color(0xFFD7ECF8).copy(alpha = if (expanded) 0.82f else 0.52f)
                            )
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(42.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(
                            if (expanded) {
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFFFFD24A),
                                        Color(0xFF62A9D5)
                                    )
                                )
                            } else {
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF9AC9E5),
                                        Color(0xFF6EA9CB)
                                    )
                                )
                            }
                        )
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = panel.kicker.uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (expanded) Color(0xFFC69B22) else Color(0xFF6F98AE),
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = panel.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = PsaNavy,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (expanded) {
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF0D6AA6),
                                        Color(0xFF2A91BD)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFFDCECF5),
                                        Color(0xFFCFE2EE)
                                    )
                                )
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (expanded) "-" else "+",
                        style = MaterialTheme.typography.titleLarge,
                        color = if (expanded) Color.White else Color(0xFF557487),
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.background(Color(0xFFF5FBFF).copy(alpha = 0.68f))
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        color = Color(0xFF8DBFDC).copy(alpha = 0.24f)
                    )
                    if (panel.title == "Registration Centers") {
                        NationalIdRegistrationCentersContent(
                            accentColor = accentColor,
                            onMapClick = { selectedMapLocation = it }
                        )
                    } else if (panel.title == "Public Advisory") {
                        NationalIdPublicAdvisoryContent(accentColor = accentColor)
                    } else if (panel.title == "Frequently Asked Questions") {
                        NationalIdFaqContent(accentColor = accentColor)
                    } else if (panel.title == "Contact Us") {
                        NationalIdContactUsContent(accentColor = accentColor)
                    } else if (panel.title == "Supporting Documents") {
                        NationalIdSupportingDocumentsContent(accentColor = accentColor)
                    } else if (panel.title == "How to Register to the National ID") {
                        NationalIdHowToRegisterContent(accentColor = accentColor)
                    } else if (panel.title == "Schedule of Mobile Registrations") {
                        NationalIdMobileRegistrationScheduleContent(accentColor = accentColor)
                    } else {
                        panel.items.forEachIndexed { index, item ->
                            val isAppointmentQrEntry = panel.title == "Book an Appointment" && index == 0
                            val isEgovAppointmentEntry = panel.title == "Book an Appointment" && index == 1
                            val mapLocation = if (panel.title == "Registration Centers") {
                                nationalIdRegistrationLocations.getOrNull(index)
                            } else {
                                null
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(
                                        if (mapLocation != null) {
                                            Modifier.clickable { selectedMapLocation = mapLocation }
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Surface(
                                    modifier = Modifier.size(23.dp),
                                    shape = CircleShape,
                                    color = if (isAppointmentQrEntry || mapLocation != null) {
                                        accentColor
                                    } else {
                                        accentColor.copy(alpha = 0.16f)
                                    }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${index + 1}",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (isAppointmentQrEntry || mapLocation != null) Color.White else accentColor,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                                Text(
                                    text = item,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PsaNavy.copy(alpha = 0.86f),
                                    lineHeight = 20.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 12.dp)
                                )

                                if (mapLocation != null) {
                                    Surface(
                                        shape = RoundedCornerShape(999.dp),
                                        color = Color(0xFFE3F1FA),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.18f))
                                    ) {
                                        Text(
                                            text = "Map",
                                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = accentColor,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }

                            if (isAppointmentQrEntry) {
                                NationalIdAppointmentQrCard(accentColor = accentColor)
                            }
                            if (isEgovAppointmentEntry) {
                                NationalIdEgovAppointmentGuideCard(accentColor = accentColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NationalIdRegistrationCentersContent(
    accentColor: Color,
    onMapClick: (NationalIdMapLocation) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        nationalIdRegistrationLocations.forEachIndexed { index, location ->
            val details = when (index) {
                0 -> Triple(
                    "Appointment",
                    "Book through nationalidappt-psacebu.com",
                    "Monday to Friday, 8:00 AM - 5:00 PM"
                )

                1 -> Triple(
                    "Walk-in",
                    "First come, first served queueing",
                    "Monday to Sunday, 8:00 PM - 12:00 Midnight"
                )

                else -> Triple(
                    "eGovPH App",
                    "Book or access service through the eGovPH App",
                    "Monday to Thursday, 7:00 AM - 6:00 PM"
                )
            }

            NationalIdRegistrationCenterCard(
                number = index + 1,
                location = location,
                accessType = details.first,
                accessNote = details.second,
                officeHours = details.third,
                accentColor = accentColor,
                onMapClick = { onMapClick(location) }
            )
        }
    }
}

@Composable
private fun NationalIdRegistrationCenterCard(
    number: Int,
    location: NationalIdMapLocation,
    accessType: String,
    accessNote: String,
    officeHours: String,
    accentColor: Color,
    onMapClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.92f),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaNavy.copy(alpha = 0.08f)),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(13.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    modifier = Modifier.size(30.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = accentColor
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = number.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = location.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = PsaNavy,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = location.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = PsaNavy.copy(alpha = 0.68f),
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NationalIdRegistrationInfoChip(
                    label = accessType,
                    value = accessNote,
                    modifier = Modifier.fillMaxWidth()
                )
                NationalIdRegistrationInfoChip(
                    label = "Office Hours",
                    value = officeHours,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clickable(onClick = onMapClick),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE8F4FB),
                border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.18f))
            ) {
                Text(
                    text = "View map location",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF0D5DA8),
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun NationalIdRegistrationInfoChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF4FAFE),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaNavy.copy(alpha = 0.06f))
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF0D5DA8),
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = PsaNavy.copy(alpha = 0.72f),
                lineHeight = 17.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
@SuppressLint("SetJavaScriptEnabled")
private fun NationalIdPublicAdvisoryContent(accentColor: Color) {
    NationalIdEmbeddedPhilSysPage(
        title = "PhilSys Public Advisories",
        url = NATIONAL_ID_PUBLIC_ADVISORY_URL,
        accentColor = accentColor
    )
}

@Composable
@SuppressLint("SetJavaScriptEnabled")
private fun NationalIdFaqContent(accentColor: Color) {
    NationalIdEmbeddedPhilSysPage(
        title = "Frequently Asked Questions",
        url = NATIONAL_ID_FAQ_URL,
        accentColor = accentColor
    )
}

@Composable
@SuppressLint("SetJavaScriptEnabled")
private fun NationalIdContactUsContent(accentColor: Color) {
    NationalIdEmbeddedPhilSysPage(
        title = "PhilSys Contact Us",
        url = NATIONAL_ID_CONTACT_US_URL,
        accentColor = accentColor
    )
}

@Composable
@SuppressLint("SetJavaScriptEnabled")
private fun NationalIdEmbeddedPhilSysPage(
    title: String,
    url: String,
    accentColor: Color
) {
    BoxWithConstraints(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        val advisoryHeight = when {
            maxHeight > 760.dp -> 720.dp
            maxHeight > 620.dp -> 640.dp
            else -> 560.dp
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(advisoryHeight),
            shape = RoundedCornerShape(18.dp),
            color = Color.White.copy(alpha = 0.96f),
            border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.18f)),
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF3FAFE))
                        .padding(horizontal = 13.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            color = PsaNavy,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = url.removePrefix("https://").removeSuffix("/"),
                            style = MaterialTheme.typography.labelSmall,
                            color = PsaNavy.copy(alpha = 0.58f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    factory = { context ->
                        WebView(context).apply {
                            setOnTouchListener { view, event ->
                                when (event.actionMasked) {
                                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                                        view.parent?.requestDisallowInterceptTouchEvent(true)
                                    }
                                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                        view.parent?.requestDisallowInterceptTouchEvent(false)
                                    }
                                }
                                false
                            }
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView, url: String?) {
                                    view.evaluateJavascript(
                                        """
                                        (function() {
                                            var meta = document.querySelector('meta[name="viewport"]');
                                            if (!meta) {
                                                meta = document.createElement('meta');
                                                meta.name = 'viewport';
                                                document.head.appendChild(meta);
                                            }
                                            meta.content = 'width=device-width, initial-scale=1, maximum-scale=3';

                                            if (!document.getElementById('psa-kiosk-advisory-fit')) {
                                                var style = document.createElement('style');
                                                style.id = 'psa-kiosk-advisory-fit';
                                                style.textContent = `
                                                    html, body {
                                                        width: 100% !important;
                                                        max-width: 100% !important;
                                                        overflow-x: hidden !important;
                                                        background: #ffffff !important;
                                                    }
                                                    body {
                                                        margin: 0 !important;
                                                        padding: 12px !important;
                                                        font-size: 17px !important;
                                                        line-height: 1.5 !important;
                                                        box-sizing: border-box !important;
                                                    }
                                                    *, *::before, *::after {
                                                        box-sizing: border-box !important;
                                                        max-width: 100% !important;
                                                    }
                                                    header, footer, nav, aside, .sidebar, .widget-area, #secondary,
                                                    .site-header, .site-footer, .main-header-bar, .ast-breadcrumbs-wrapper {
                                                        display: none !important;
                                                    }
                                                    #page, #content, .site, .site-content, .content-area, .site-main,
                                                    .ast-container, .container, .wrap, main {
                                                        width: 100% !important;
                                                        max-width: 100% !important;
                                                        margin: 0 !important;
                                                        padding: 0 !important;
                                                        display: block !important;
                                                    }
                                                    article, .post, .hentry, .ast-article-post, .blog-layout-1, .blog-layout-2 {
                                                        width: 100% !important;
                                                        max-width: 100% !important;
                                                        float: none !important;
                                                        clear: both !important;
                                                        display: block !important;
                                                        margin: 0 0 20px 0 !important;
                                                        padding: 0 0 18px 0 !important;
                                                        border-bottom: 1px solid #e5edf4 !important;
                                                    }
                                                    article img, .post img, img {
                                                        width: 100% !important;
                                                        max-width: 100% !important;
                                                        height: auto !important;
                                                        display: block !important;
                                                        margin: 8px 0 !important;
                                                    }
                                                    h1, h2, h3, .entry-title {
                                                        color: #0b2640 !important;
                                                        font-size: 21px !important;
                                                        line-height: 1.25 !important;
                                                        margin: 0 0 10px 0 !important;
                                                        word-break: normal !important;
                                                    }
                                                    p, span, a, li, .entry-meta, .read-more, .ast-button {
                                                        font-size: 15px !important;
                                                        line-height: 1.45 !important;
                                                    }
                                                    .read-more, .ast-button, a.more-link {
                                                        display: inline-block !important;
                                                        margin-top: 4px !important;
                                                        padding: 8px 10px !important;
                                                        border-radius: 8px !important;
                                                        background: #e8f4fb !important;
                                                        color: #0f5f90 !important;
                                                        font-weight: 700 !important;
                                                        text-decoration: none !important;
                                                    }
                                                `;
                                                document.head.appendChild(style);
                                            }
                                        })();
                                        """.trimIndent(),
                                        null
                                    )
                                }
                            }
                            isVerticalScrollBarEnabled = true
                            isHorizontalScrollBarEnabled = false
                            overScrollMode = WebView.OVER_SCROLL_IF_CONTENT_SCROLLS
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.cacheMode = WebSettings.LOAD_DEFAULT
                            settings.loadWithOverviewMode = false
                            settings.useWideViewPort = false
                            settings.textZoom = 110
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false
                            setInitialScale(100)
                            loadUrl(url)
                        }
                    },
                    update = { webView ->
                        if (webView.url != url) {
                            webView.loadUrl(url)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NationalIdEgovAppointmentGuideCard(accentColor: Color) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 36.dp, end = 14.dp, bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.22f)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "eGovPH appointment guide",
                style = MaterialTheme.typography.labelLarge,
                color = PsaNavy,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.national_id_egovph_appointment_guide),
                contentDescription = "Book your National ID appointment through eGovPH guide",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun NationalIdHowToRegisterContent(accentColor: Color) {
    Column(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF4FAFE),
            border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.18f))
        ) {
            Text(
                text = "Scroll down to view the full registration guide.",
                modifier = Modifier.padding(horizontal = 13.dp, vertical = 10.dp),
                style = MaterialTheme.typography.labelMedium,
                color = PsaNavy.copy(alpha = 0.72f),
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, PsaNavy.copy(alpha = 0.08f)),
            shadowElevation = 2.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.national_id_how_to_register_english),
                contentDescription = "How to Register to the National ID guide",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(816f / 2048f),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
private fun NationalIdSupportingDocumentsContent(accentColor: Color) {
    val primaryDocuments = remember {
        listOf(
            "PSA-issued Certificate of Live Birth and one government-issued identification document bearing full name, front-facing photograph, and signature or thumb mark.",
            "Philippine Passport or ePassport issued by the Department of Foreign Affairs (DFA).",
            "Unified Multi-purpose Identification (UMID) Card issued by the Government Service Insurance System (GSIS) or Social Security System (SSS).",
            "Student's License Permit or Non-Professional/Professional Driver's License issued by the LTO."
        )
    }
    val secondaryDocuments = remember {
        listOf(
            "PSA-issued Certificate of Live Birth or National Statistics Office (NSO)-issued Certificate of Live Birth with Birth Reference Number (BreN).",
            "Local Civil Registry Office (LCRO)-issued Certificate of Live Birth.",
            "PSA-issued Report of Birth.",
            "PSA-issued Certificate of Foundling.",
            "Integrated Bar of the Philippines (IBP) Identification Card.",
            "Professional Regulatory Commission (PRC) ID.",
            "Seaman's Book or Seafarer's Record Book.",
            "Overseas Workers Welfare Administration (OWWA) ID.",
            "Senior Citizen's ID issued by OSCA and/or Local Government Units (LGU).",
            "Social Security System (SSS) ID.",
            "Pantawid Pamilyang Pilipino Program (4Ps) ID.",
            "License to Own or Possess Firearms (LTOPF).",
            "National Bureau of Investigation (NBI) Clearance.",
            "Police Clearance or Police ID.",
            "Solo Parent's ID.",
            "Person with Disability (PWD) ID issued by NCDA or its regional counterpart, Office of the Mayor, DSWD Office, or participating organizations with a Memorandum of Agreement with the DOH.",
            "Voter's ID issued by the Commission on Elections (COMELEC).",
            "Postal ID issued by the Philippine Postal Corporation (PhlPost).",
            "Taxpayer Identification Number (TIN) ID.",
            "PhilHealth ID.",
            "Special Resident Retiree's Visa (SRRV) issued by the Philippine Retirement Authority (PRA).",
            "National ID from other countries.",
            "Residence ID from other countries.",
            "Professional Identification Card issued by the Maritime Industry Authority (MARINA).",
            "Eligibility Card issued by the Civil Service Commission.",
            "Dependent's ID issued by the Armed Forces of the Philippines (AFP) and Philippine National Police (PNP).",
            "Retiree's ID issued by the PNP, AFP, or Philippine Coast Guard (PCG).",
            "Conductor's License issued by the LTO.",
            "Philippine Veterans Affairs Office (PVAO) Pensioner's ID, Veteran or Dependent.",
            "Seafarer's Identity Document or Seaman's ID.",
            "Tribal Certificate or ID issued by the Tribal Affairs Office under the Office of the Mayor, if applicable.",
            "Certificate of Confirmation issued by the National Commission on Indigenous Peoples (NCIP), or Certificate of Indigenous Cultural Communities (ICCs) / Indigenous Peoples (IPs) Membership (CIPM) issued by the Tribal Leader of ICC/IP.",
            "Certificate of Tribal Membership issued by the National Commission on Muslim Filipinos (NCMF)."
        )
    }
    val conditionalSecondaryDocuments = remember {
        listOf(
            "Employee ID.",
            "School ID. For pre-school and elementary, signature or thumb mark of the ID owner is not required.",
            "City or Municipal ID.",
            "Barangay Clearance or Certificate.",
            "Barangay ID.",
            "Voter's Certification issued by the COMELEC.",
            "Prison Record.",
            "Certificate of Detention."
        )
    }

    Column(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFFFBEC),
            border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.22f))
        ) {
            Text(
                text = "If there are discrepancies between the PSA-issued Certificate of Live Birth and the government-issued ID presented, the entries in the PSA-issued Certificate of Live Birth shall prevail.",
                modifier = Modifier.padding(13.dp),
                style = MaterialTheme.typography.bodySmall,
                color = PsaNavy.copy(alpha = 0.76f),
                lineHeight = 18.sp
            )
        }

        NationalIdDocumentSectionCard(
            title = "Primary Supporting Documents",
            description = "The registrant is encouraged to bring any of the following primary documents:",
            documents = primaryDocuments,
            accentColor = accentColor
        )

        NationalIdDocumentSectionCard(
            title = "Secondary Supporting Documents",
            description = "If the registrant does not have any primary document, he/she may bring any of the following secondary documents:",
            documents = secondaryDocuments,
            accentColor = accentColor
        )

        NationalIdDocumentSectionCard(
            title = "Other Accepted Secondary IDs",
            description = "These shall be accepted if they have a front-facing photograph, signature or thumb mark, full name, permanent address, and date of birth:",
            documents = conditionalSecondaryDocuments,
            accentColor = accentColor
        )
    }
}

@Composable
private fun NationalIdDocumentSectionCard(
    title: String,
    description: String,
    documents: List<String>,
    accentColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.93f),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaNavy.copy(alpha = 0.08f)),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(13.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = PsaNavy,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = PsaNavy.copy(alpha = 0.68f),
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 3.dp, bottom = 8.dp)
            )

            documents.forEachIndexed { index, document ->
                NationalIdDocumentRow(
                    number = index + 1,
                    document = document,
                    accentColor = accentColor
                )
                if (index != documents.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 45.dp, top = 8.dp, bottom = 8.dp),
                        color = PsaNavy.copy(alpha = 0.06f)
                    )
                }
            }
        }
    }
}

@Composable
private fun NationalIdDocumentRow(
    number: Int,
    document: String,
    accentColor: Color
) {
    Row(verticalAlignment = Alignment.Top) {
        Surface(
            modifier = Modifier
                .width(if (number >= 10) 34.dp else 28.dp)
                .height(26.dp),
            shape = RoundedCornerShape(999.dp),
            color = accentColor.copy(alpha = 0.16f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, lineHeight = 14.sp),
                    color = Color(0xFF0D5DA8),
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
        Text(
            text = document,
            modifier = Modifier
                .weight(1f)
                .padding(start = 11.dp, top = 2.dp),
            style = MaterialTheme.typography.bodySmall,
            color = PsaNavy.copy(alpha = 0.82f),
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun NationalIdMobileRegistrationScheduleContent(accentColor: Color) {
    val scheduleResult by produceState<Result<NationalIdMobileSchedule>?>(initialValue = null) {
        value = loadNationalIdMobileRegistrationSchedule()
    }

    when {
        scheduleResult == null -> {
            NationalIdScheduleStatusCard(
                title = "Loading schedule",
                message = "Retrieving the latest mobile registration schedule from Google Sheets.",
                accentColor = accentColor
            )
        }

        scheduleResult?.isFailure == true -> {
            NationalIdScheduleStatusCard(
                title = "Schedule unavailable",
                message = "Make sure the Google Sheet is shared as Anyone with the link can view, then reopen this panel.",
                accentColor = accentColor
            )
        }

        else -> {
            val schedule = scheduleResult?.getOrNull()
            val rows = schedule?.days.orEmpty()
            if (rows.isEmpty()) {
                NationalIdScheduleStatusCard(
                    title = "No schedule posted",
                    message = "No mobile registration schedule is currently listed in the connected sheet.",
                    accentColor = accentColor
                )
            } else {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                    NationalIdScheduleMonthHeader(
                        monthLabel = schedule?.monthLabel.orEmpty(),
                        days = rows,
                        accentColor = accentColor
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    NationalIdScheduleLegend(accentColor = accentColor)
                    Spacer(modifier = Modifier.height(10.dp))
                    rows.forEachIndexed { index, day ->
                        NationalIdMobileScheduleDayCard(
                            day = day,
                            accentColor = accentColor
                        )
                        if (index != rows.lastIndex) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NationalIdScheduleMonthHeader(
    monthLabel: String,
    days: List<NationalIdMobileScheduleDay>,
    accentColor: Color
) {
    val confirmedCount = days.sumOf { day -> day.entries.count { it.confirmed } }
    val tentativeCount = days.sumOf { day -> day.entries.count { !it.confirmed } }
    val totalSchedules = days.sumOf { it.entries.size }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.Transparent,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF0B6FA4),
                            Color(0xFF1395C8),
                            accentColor.copy(alpha = 0.86f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = monthLabel.ifBlank { "Mobile Registration Schedule" },
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Schedule of Mobile Registrations",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.82f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NationalIdScheduleMetric(
                        label = "Days",
                        value = days.size.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    NationalIdScheduleMetric(
                        label = "Schedules",
                        value = totalSchedules.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NationalIdScheduleMetric(
                        label = "Confirmed",
                        value = confirmedCount.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    NationalIdScheduleMetric(
                        label = "Tentative",
                        value = tentativeCount.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun NationalIdScheduleMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.18f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.16f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 9.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.76f),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun NationalIdScheduleStatusCard(
    title: String,
    message: String,
    accentColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.72f),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.20f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = PsaNavy,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = PsaNavy.copy(alpha = 0.72f),
                lineHeight = 19.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun NationalIdScheduleLegend(accentColor: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = accentColor.copy(alpha = 0.10f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NationalIdScheduleStatusPill(confirmed = true)
            NationalIdScheduleStatusPill(confirmed = false)
            Text(
                text = "Checked entries are confirmed. RKO and RA show posted team counts.",
                style = MaterialTheme.typography.labelSmall,
                color = PsaNavy.copy(alpha = 0.64f),
                lineHeight = 15.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NationalIdMobileScheduleDayCard(
    day: NationalIdMobileScheduleDay,
    accentColor: Color
) {
    val confirmedCount = day.entries.count { it.confirmed }
    val tentativeCount = day.entries.size - confirmedCount

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.92f),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaNavy.copy(alpha = 0.07f)),
        shadowElevation = 2.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFF7FBFF),
                                accentColor.copy(alpha = 0.16f)
                            )
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(46.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = PsaNavy
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = day.date,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 11.dp)
                ) {
                    Text(
                        text = day.weekday,
                        style = MaterialTheme.typography.titleMedium,
                        color = PsaNavy,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = buildString {
                            append("${day.entries.size} schedule")
                            if (day.entries.size != 1) append("s")
                            append(" | $confirmedCount confirmed")
                            if (tentativeCount > 0) append(" | $tentativeCount tentative")
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = PsaNavy.copy(alpha = 0.62f),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            if (day.totalRko.isNotBlank() || day.totalRa.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF7FBFF))
                        .padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Daily total",
                        style = MaterialTheme.typography.labelMedium,
                        color = PsaNavy.copy(alpha = 0.62f),
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.weight(1f)
                    )
                    NationalIdScheduleWideCountChip(label = "RKO", value = day.totalRko.ifBlank { "0" })
                    NationalIdScheduleWideCountChip(label = "RA", value = day.totalRa.ifBlank { "0" })
                }
            }

            day.entries.forEachIndexed { index, entry ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 11.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        NationalIdScheduleStatusDot(confirmed = entry.confirmed)
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                text = entry.location,
                                style = MaterialTheme.typography.bodyMedium,
                                color = PsaNavy,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 20.sp
                            )
                            Text(
                                text = if (entry.confirmed) "Confirmed schedule" else "Tentative schedule",
                                style = MaterialTheme.typography.labelSmall,
                                color = PsaNavy.copy(alpha = 0.56f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NationalIdScheduleWideCountChip(
                            label = "RKO",
                            value = entry.rko.ifBlank { "0" },
                            modifier = Modifier.weight(1f)
                        )
                        NationalIdScheduleWideCountChip(
                            label = "RA",
                            value = entry.ra.ifBlank { "0" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                if (index != day.entries.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        color = PsaNavy.copy(alpha = 0.06f)
                    )
                }
            }
        }
    }
}

@Composable
private fun NationalIdScheduleStatusDot(confirmed: Boolean) {
    val color = if (confirmed) Color(0xFF0E8F55) else Color(0xFFE24A3B)

    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.16f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Composable
private fun NationalIdScheduleWideCountChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF3F8FC),
        border = androidx.compose.foundation.BorderStroke(1.dp, PsaNavy.copy(alpha = 0.07f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = PsaNavy.copy(alpha = 0.58f),
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = PsaNavy,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun NationalIdScheduleStatusPill(confirmed: Boolean) {
    val background = if (confirmed) Color(0xFFDBF5E8) else Color(0xFFFFECE9)
    val foreground = if (confirmed) Color(0xFF047844) else Color(0xFFC2362B)

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = background,
        border = androidx.compose.foundation.BorderStroke(1.dp, foreground.copy(alpha = 0.18f))
    ) {
        Text(
            text = if (confirmed) "Confirmed" else "Tentative",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = foreground,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

private suspend fun loadNationalIdMobileRegistrationSchedule(): Result<NationalIdMobileSchedule> = withContext(Dispatchers.IO) {
    runCatching {
        val connection = (URL(NATIONAL_ID_MOBILE_REGISTRATION_SHEET_CSV_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 12000
            readTimeout = 15000
        }

        val csv = connection.inputStream.bufferedReader().use { it.readText() }
        if (csv.trimStart().startsWith("<")) {
            error("Google Sheet CSV export is not publicly accessible.")
        }
        parseNationalIdMobileRegistrationSchedule(csv)
    }
}

private fun parseNationalIdMobileRegistrationSchedule(csv: String): NationalIdMobileSchedule {
    val rows = parseCsvRows(csv).map { row -> row.map { it.trim() } }
    val weekdayHeaderIndex = rows.indexOfFirst { row ->
        row.any { cell -> nationalIdWeekdayFromCell(cell) != null }
    }
    if (weekdayHeaderIndex < 0) {
        return NationalIdMobileSchedule(
            monthLabel = nationalIdCurrentMonthLabel(),
            days = emptyList()
        )
    }

    val maxColumnCount = rows.maxOfOrNull { it.size } ?: 0
    val weekdayGroups = rows[weekdayHeaderIndex]
        .mapIndexedNotNull { columnIndex, cell ->
            nationalIdWeekdayFromCell(cell)?.let { weekday ->
                columnIndex to weekday
            }
        }
        .mapIndexed { order, (columnIndex, weekday) ->
            val nextColumn = rows[weekdayHeaderIndex]
                .drop(columnIndex + 1)
                .indexOfFirst { nationalIdWeekdayFromCell(it) != null }
                .takeIf { it >= 0 }
                ?.let { columnIndex + it + 1 }
                ?: maxColumnCount

            NationalIdMobileScheduleColumnGroup(
                weekday = weekday,
                startColumn = columnIndex,
                endColumn = nextColumn,
                displayOrder = order
            )
        }

    val parsedDays = mutableListOf<ParsedNationalIdMobileScheduleDay>()

    weekdayGroups.forEach { group ->
        var rowIndex = weekdayHeaderIndex + 1
        while (rowIndex < rows.size) {
            if (!isNationalIdScheduleDateBlock(rows, rowIndex, group)) {
                rowIndex++
                continue
            }

            val date = nationalIdScheduleDateFromRow(rows[rowIndex], group).orEmpty()
            val headerRowIndex = nationalIdScheduleHeaderRowIndex(rows, rowIndex, group)
            if (date.isBlank() || headerRowIndex == null) {
                rowIndex++
                continue
            }

            val headerRow = rows[headerRowIndex]
            val locColumn = nationalIdColumnIndex(headerRow, group, "LOC") ?: group.startColumn
            val rkoColumn = nationalIdColumnIndex(headerRow, group, "RKO") ?: (locColumn + 1)
            val raColumn = nationalIdColumnIndex(headerRow, group, "RA") ?: (rkoColumn + 1)
            val statusColumn = (locColumn - 1).coerceAtLeast(group.startColumn)
            val nextDateBlockRow = ((rowIndex + 1) until rows.size)
                .firstOrNull { candidateRow -> isNationalIdScheduleDateBlock(rows, candidateRow, group) }
                ?: rows.size

            var totalRko = ""
            var totalRa = ""
            val entries = mutableListOf<NationalIdMobileScheduleEntry>()

            for (entryRowIndex in (headerRowIndex + 1) until nextDateBlockRow) {
                val entryRow = rows[entryRowIndex]
                if (entryRow.isNationalIdScheduleTotalRow(group)) {
                    totalRko = entryRow.nationalIdCell(rkoColumn)
                    totalRa = entryRow.nationalIdCell(raColumn)
                    continue
                }

                val location = entryRow.nationalIdCell(locColumn)
                    .replace(Regex("\\s+"), " ")
                    .trim()
                if (location.isBlank() ||
                    location.equals("LOC", ignoreCase = true) ||
                    location.startsWith("NOTE", ignoreCase = true)
                ) {
                    continue
                }

                entries += NationalIdMobileScheduleEntry(
                    location = location,
                    rko = entryRow.nationalIdCell(rkoColumn),
                    ra = entryRow.nationalIdCell(raColumn),
                    confirmed = nationalIdScheduleIsConfirmed(entryRow.nationalIdCell(statusColumn))
                )
            }

            if (entries.isNotEmpty()) {
                parsedDays += ParsedNationalIdMobileScheduleDay(
                    day = NationalIdMobileScheduleDay(
                        weekday = group.weekday,
                        date = date,
                        entries = entries,
                        totalRko = totalRko,
                        totalRa = totalRa
                    ),
                    rowIndex = rowIndex,
                    displayOrder = group.displayOrder
                )
            }

            rowIndex = nextDateBlockRow
        }
    }

    val days = parsedDays
        .sortedWith(compareBy<ParsedNationalIdMobileScheduleDay> { it.rowIndex }.thenBy { it.displayOrder })
        .map { it.day }

    return NationalIdMobileSchedule(
        monthLabel = nationalIdScheduleMonthLabel(rows, weekdayHeaderIndex, days),
        days = days
    )
}

private fun nationalIdWeekdayFromCell(cell: String): String? {
    val normalized = cell.trim().uppercase(Locale.ROOT)
    return listOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY")
        .firstOrNull { weekday -> normalized.startsWith(weekday) }
        ?.lowercase(Locale.ROOT)
        ?.replaceFirstChar { it.titlecase(Locale.ROOT) }
}

private fun nationalIdScheduleMonthLabel(
    rows: List<List<String>>,
    weekdayHeaderIndex: Int,
    days: List<NationalIdMobileScheduleDay>
): String {
    val monthFromSheet = rows
        .take(weekdayHeaderIndex)
        .asSequence()
        .flatMap { it.asSequence() }
        .mapNotNull { nationalIdMonthLabelFromCell(it) }
        .firstOrNull()

    return monthFromSheet ?: nationalIdInferMonthLabelFromCalendar(days) ?: nationalIdCurrentMonthLabel()
}

private fun nationalIdMonthLabelFromCell(cell: String): String? {
    val monthNames = listOf(
        "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
        "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
    )
    val normalized = cell.trim().uppercase(Locale.ROOT)
    val month = monthNames.firstOrNull { normalized.contains(it) } ?: return null
    val year = Regex("\\b(20\\d{2})\\b").find(normalized)?.value
        ?: Calendar.getInstance().get(Calendar.YEAR).toString()

    return "${month.lowercase(Locale.ROOT).replaceFirstChar { it.titlecase(Locale.ROOT) }} $year"
}

private fun nationalIdInferMonthLabelFromCalendar(days: List<NationalIdMobileScheduleDay>): String? {
    val firstDay = days.firstOrNull { it.date.toIntOrNull() == 1 } ?: return null
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonthIndex = currentYear * 12 + calendar.get(Calendar.MONTH)
    val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    val bestCandidate = ((currentYear - 1)..(currentYear + 1)).flatMap { year ->
        (Calendar.JANUARY..Calendar.DECEMBER).mapNotNull { month ->
            calendar.set(year, month, 1)
            val weekday = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
            if (weekday.equals(firstDay.weekday, ignoreCase = true)) {
                year to month
            } else {
                null
            }
        }
    }.minByOrNull { (year, month) ->
        ((year * 12 + month) - currentMonthIndex).absoluteValue
    } ?: return null

    calendar.set(bestCandidate.first, bestCandidate.second, 1)
    return formatter.format(calendar.time)
}

private fun nationalIdCurrentMonthLabel(): String {
    return SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())
}

private fun isNationalIdScheduleDateBlock(
    rows: List<List<String>>,
    rowIndex: Int,
    group: NationalIdMobileScheduleColumnGroup
): Boolean {
    val row = rows.getOrNull(rowIndex) ?: return false
    if (nationalIdScheduleDateFromRow(row, group).isNullOrBlank()) {
        return false
    }
    return nationalIdScheduleHeaderRowIndex(rows, rowIndex, group) != null
}

private fun nationalIdScheduleDateFromRow(
    row: List<String>,
    group: NationalIdMobileScheduleColumnGroup
): String? {
    val groupCells = (group.startColumn until group.endColumn)
        .map { row.nationalIdCell(it) }
        .filter { it.isNotBlank() }

    if (groupCells.isEmpty() || groupCells.any { it.toIntOrNull() == null }) {
        return null
    }

    return groupCells.firstOrNull { cell ->
        val value = cell.toIntOrNull()
        value != null && value in 1..31
    }
}

private fun nationalIdScheduleHeaderRowIndex(
    rows: List<List<String>>,
    dateRowIndex: Int,
    group: NationalIdMobileScheduleColumnGroup
): Int? {
    return ((dateRowIndex + 1)..minOf(dateRowIndex + 4, rows.lastIndex))
        .firstOrNull { rowIndex ->
            val row = rows[rowIndex]
            nationalIdColumnIndex(row, group, "LOC") != null &&
                nationalIdColumnIndex(row, group, "RKO") != null &&
                nationalIdColumnIndex(row, group, "RA") != null
        }
}

private fun nationalIdColumnIndex(
    row: List<String>,
    group: NationalIdMobileScheduleColumnGroup,
    header: String
): Int? {
    return (group.startColumn until group.endColumn)
        .firstOrNull { columnIndex -> row.nationalIdCell(columnIndex).equals(header, ignoreCase = true) }
}

private fun List<String>.isNationalIdScheduleTotalRow(group: NationalIdMobileScheduleColumnGroup): Boolean {
    return (group.startColumn until group.endColumn)
        .any { columnIndex -> nationalIdCell(columnIndex).equals("TOTAL", ignoreCase = true) }
}

private fun nationalIdScheduleIsConfirmed(status: String): Boolean {
    val normalized = status.trim().uppercase(Locale.ROOT)
    return normalized in setOf("TRUE", "YES", "Y", "1", "CHECKED", "CONFIRMED") ||
        status.any { it.code in setOf(0x2713, 0x2714, 0x2611) }
}

private fun List<String>.nationalIdCell(index: Int): String {
    return getOrNull(index).orEmpty().trim()
}

@Composable
private fun NationalIdAppointmentQrCard(accentColor: Color) {
    val qrUrl = remember {
        val encodedUrl = URLEncoder.encode(NATIONAL_ID_APPOINTMENT_URL, "UTF-8")
        "https://api.qrserver.com/v1/create-qr-code/?size=260x260&margin=12&data=$encodedUrl"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 52.dp, end = 16.dp, bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF7FBFF),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.24f)),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(108.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, PsaNavy.copy(alpha = 0.10f))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(qrUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "National ID appointment QR code",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Scan to book",
                    style = MaterialTheme.typography.titleSmall,
                    color = PsaNavy,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = NATIONAL_ID_APPOINTMENT_URL.removePrefix("https://").removeSuffix("/"),
                    style = MaterialTheme.typography.bodySmall,
                    color = PsaNavy.copy(alpha = 0.68f),
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}

@Composable
@SuppressLint("SetJavaScriptEnabled")
private fun NationalIdLocationMapDialog(
    location: NationalIdMapLocation,
    onDismiss: () -> Unit
) {
    var mapZoom by remember(location.title) { mutableStateOf(17) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.58f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDismiss
                )
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            val portraitLayout = maxHeight > maxWidth
            val mapWidthFraction = if (portraitLayout) 0.98f else 0.94f
            val mapHeightFraction = if (portraitLayout) 0.80f else 0.72f

            Surface(
                modifier = Modifier
                    .fillMaxWidth(mapWidthFraction)
                    .fillMaxHeight(mapHeightFraction)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {}
                    ),
                shape = RoundedCornerShape(22.dp),
                color = Color(0xFFF7FBFF),
                shadowElevation = 18.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0F6EA4))
                            .padding(horizontal = 16.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = location.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = location.address,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.78f),
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(top = 3.dp)
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .height(38.dp)
                                .clickable(onClick = onDismiss),
                            shape = RoundedCornerShape(999.dp),
                            color = Color.White.copy(alpha = 0.94f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.88f))
                        ) {
                            Text(
                                text = "Close",
                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF0D4268),
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    NationalIdTileMap(
                        location = location,
                        zoom = mapZoom,
                        onZoomIn = { mapZoom = (mapZoom + 1).coerceAtMost(19) },
                        onZoomOut = { mapZoom = (mapZoom - 1).coerceAtLeast(15) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun NationalIdTileMap(
    location: NationalIdMapLocation,
    zoom: Int,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tiles = remember(location.latitude, location.longitude, zoom) {
        nationalIdMapTiles(location = location, zoom = zoom)
    }
    var gestureScale by remember(location.title, zoom) { mutableStateOf(1f) }
    var offsetX by remember(location.title, zoom) { mutableStateOf(0f) }
    var offsetY by remember(location.title, zoom) { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .background(Color(0xFFE5F1F7))
            .clip(RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(location.title, zoom) {
                    detectTransformGestures { _, pan, zoomChange, _ ->
                        gestureScale = (gestureScale * zoomChange).coerceIn(0.80f, 3.20f)
                        offsetX = (offsetX + pan.x).coerceIn(-900f, 900f)
                        offsetY = (offsetY + pan.y).coerceIn(-900f, 900f)
                    }
                }
                .graphicsLayer {
                    scaleX = gestureScale
                    scaleY = gestureScale
                    translationX = offsetX
                    translationY = offsetY
                },
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                (-2..2).forEach { yOffset ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        (-2..2).forEach { xOffset ->
                            val tile = tiles.first { it.xOffset == xOffset && it.yOffset == yOffset }
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(tile.url)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE84855).copy(alpha = 0.96f))
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp),
            shape = RoundedCornerShape(999.dp),
            color = Color.White.copy(alpha = 0.88f)
        ) {
            Text(
                text = "Drag or pinch map",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                style = MaterialTheme.typography.labelSmall,
                color = PsaNavy.copy(alpha = 0.70f),
                fontWeight = FontWeight.Bold
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White.copy(alpha = 0.92f),
            shadowElevation = 4.dp
        ) {
            Column {
                MapZoomButton(
                    label = "+",
                    enabled = zoom < 19,
                    onClick = onZoomIn
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = PsaNavy.copy(alpha = 0.10f)
                )
                MapZoomButton(
                    label = "-",
                    enabled = zoom > 15,
                    onClick = onZoomOut
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp),
            shape = RoundedCornerShape(999.dp),
            color = Color.White.copy(alpha = 0.88f)
        ) {
            Text(
                text = "Zoom $zoom",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                style = MaterialTheme.typography.labelSmall,
                color = PsaNavy.copy(alpha = 0.70f),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MapZoomButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
            color = if (enabled) PsaNavy else PsaNavy.copy(alpha = 0.28f),
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
    }
}

private fun nationalIdMapTiles(
    location: NationalIdMapLocation,
    zoom: Int
): List<NationalIdMapTile> {
    val lat = location.latitude
    val lon = location.longitude
    val tilesPerAxis = 1 shl zoom
    val latRadians = Math.toRadians(lat)
    val centerX = Math.floor(((lon + 180.0) / 360.0) * tilesPerAxis).toInt()
    val centerY = Math.floor(
        ((1.0 - (Math.log(Math.tan(latRadians) + (1.0 / Math.cos(latRadians))) / Math.PI)) / 2.0) * tilesPerAxis
    ).toInt()

    return (-2..2).flatMap { yOffset ->
        (-2..2).map { xOffset ->
            val x = (centerX + xOffset).floorMod(tilesPerAxis)
            val y = (centerY + yOffset).coerceIn(0, tilesPerAxis - 1)
            NationalIdMapTile(
                xOffset = xOffset,
                yOffset = yOffset,
                url = "https://${cartoTileSubdomain(x, y)}.basemaps.cartocdn.com/light_all/$zoom/$x/$y.png"
            )
        }
    }
}

private fun Int.floorMod(modulus: Int): Int = ((this % modulus) + modulus) % modulus

private fun cartoTileSubdomain(x: Int, y: Int): String {
    return when ((x + y).floorMod(4)) {
        0 -> "a"
        1 -> "b"
        2 -> "c"
        else -> "d"
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
        Image(
            painter = painterResource(id = R.drawable.kiosk_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEAF7FF).copy(alpha = 0.76f),
                            Color(0xFFC7E8FF).copy(alpha = 0.66f),
                            Color(0xFF91CCF2).copy(alpha = 0.58f),
                            Color(0xFF4B94CA).copy(alpha = 0.54f)
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.40f),
                            PsaSky.copy(alpha = 0.16f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .padding(top = 110.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.12f),
                            PsaGold.copy(alpha = 0.06f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(780.dp)
                .padding(top = 520.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF0E5B9B).copy(alpha = 0.12f),
                            Color(0xFF0E5B9B).copy(alpha = 0.16f),
                            Color.Transparent
                        )
                    )
                )
        )
        repeat(7) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(top = (140 + index * 160).dp)
                    .background(Color.White.copy(alpha = 0.12f))
            )
        }
    }
}
