package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.UserStats
import com.example.ui.viewmodel.SkillViewModel

@Composable
fun ProfileScreen(
    viewModel: SkillViewModel,
    modifier: Modifier = Modifier
) {
    var activeSubTab by remember { mutableIntStateOf(0) } // 0 = Vault (Profile), 1 = PRO Shop

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Edge-safe top header
        Text(
            text = if (activeSubTab == 0) "My Profile" else "PRO Shop",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        // Custom high-contrast Material 3 tabs to toggle between Vault and PRO Store
        TabRow(
            selectedTabIndex = activeSubTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth().testTag("profile_store_tab_row")
        ) {
            Tab(
                selected = activeSubTab == 0,
                onClick = { activeSubTab = 0 },
                text = { Text("My Vault", fontWeight = FontWeight.Bold) },
                icon = { Icon(imageVector = Icons.Default.FolderZip, contentDescription = "Vault") },
                modifier = Modifier.testTag("subtab_vault")
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = { Text("PRO Store 👑", fontWeight = FontWeight.Bold) },
                icon = { Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = "PRO Shop") },
                modifier = Modifier.testTag("subtab_pro_shop")
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (activeSubTab == 0) {
                ProfileVaultContent(viewModel = viewModel)
            } else {
                ProfileStoreContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ProfileVaultContent(viewModel: SkillViewModel) {
    val context = LocalContext.current
    val stats by viewModel.userStatsState.collectAsState()
    val completedLessons by viewModel.completedLessonsState.collectAsState()
    val progressList by viewModel.roadmapProgressState.collectAsState()
    val roadmaps = viewModel.getRoadmaps()

    // Calculate badges unlocked by completing all days of structured learning roadmaps
    val completedRoadmapBadges = progressList.mapNotNull { progress ->
        val roadmap = roadmaps.firstOrNull { it.id == progress.roadmapId }
        if (roadmap != null) {
            val completedCount = progress.completedDaysText.split(",").filter { it.isNotEmpty() }.size
            if (completedCount >= roadmap.durationDays) {
                roadmap.badgeReward
            } else {
                null
            }
        } else {
            null
        }
    }

    val lessonBadgeSet = completedLessons.mapNotNull { it.completedAt.let { _ -> 
        when (it.lessonId) {
            1 -> "Excel Rookie"
            2 -> "Communication Pro"
            3 -> "AI Wizard"
            4 -> "Charismatic Pitcher"
            5 -> "Wealth Builder"
            6 -> "Career Growth Master"
            else -> null
        }
    }}.toSet()

    val unlockedBadgeSet = (lessonBadgeSet + completedRoadmapBadges).toSet()

    val badgesList = listOf(
        Pair("Excel Rookie", Icons.Default.TrendingUp),
        Pair("Communication Pro", Icons.Default.QuestionAnswer),
        Pair("AI Wizard", Icons.Default.SmartToy),
        Pair("Charismatic Pitcher", Icons.Default.OfflineBolt),
        Pair("Wealth Builder", Icons.Default.AccountBalanceWallet),
        Pair("Career Growth Master", Icons.Default.School),
        Pair("Excel Legend", Icons.Default.Stars),
        Pair("Market-Ready Pro", Icons.Default.Verified),
        Pair("Freelance Hustler", Icons.Default.Storefront),
        Pair("Startup Architect", Icons.Default.LocalFireDepartment),
        Pair("Cognitive Mastermind", Icons.Default.Lightbulb),
        Pair("Growth Sovereign", Icons.Default.Bolt),
        Pair("Wealth Vanguard", Icons.Default.EmojiEvents),
        Pair("Unshakeable Aura", Icons.Default.WorkspacePremium)
    )

    // Calculate level bounds
    val levelXpLimit = when (stats.level) {
        1 -> 500
        2 -> 1500
        3 -> 3000
        4 -> 5000
        else -> 10000
    }
    val previousLevelLimit = when (stats.level) {
        1 -> 0
        2 -> 500
        3 -> 1500
        4 -> 3000
        else -> 5000
    }

    val fractionOfXp = ((stats.xp - previousLevelLimit).toFloat() / (levelXpLimit - previousLevelLimit).toFloat()).coerceIn(0f, 1f)
    val animatedXpFraction by animateFloatAsState(targetValue = fractionOfXp, label = "xpFraction")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Circular Progress avatar block
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(100.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { animatedXpFraction },
                        strokeWidth = 8.dp,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxSize()
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Lvl ${stats.level}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${stats.xp}/$levelXpLimit XP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${stats.levelName} Learner",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (stats.isPremium) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "Active Pro Sub",
                            tint = Color(0xFFFF9100),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = "Completed ${completedLessons.size} micro lessons | ${reapCompletedLessonsInfo(stats, progressList.size)} structured actions",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Streak stats indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatsCard(
                title = "Active Streak",
                value = "🔥 ${stats.streak} Days",
                subtitle = "Consecutive active days",
                modifier = Modifier.weight(1f)
            )

            StatsCard(
                title = "All-Time Record",
                value = "🏆 ${stats.maxStreak} Days",
                subtitle = "Longest daily sequence",
                modifier = Modifier.weight(1f)
            )
        }

        // Badges shelf header
        Text(
            text = "Acquired Badges",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(22.dp))
                .padding(16.dp)
        ) {
            val badgeChunks = badgesList.chunked(3)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                badgeChunks.forEachIndexed { index, chunk ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        chunk.forEach { (badge, icon) ->
                            val levelRequired = if (index <= 1) 1 else if (index <= 3) 2 else 3
                            val isUnlocked = unlockedBadgeSet.contains(badge) || stats.level >= levelRequired
                            BadgeCell(name = badge, icon = icon, isUnlocked = isUnlocked)
                        }
                        if (chunk.size < 3) {
                            repeat(3 - chunk.size) {
                                Spacer(modifier = Modifier.width(64.dp))
                            }
                        }
                    }
                }
            }
        }

        // Referral engine card
        Text(
            text = "Referred Friends Challenges",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.2.dp, Color(0xFF4CAF50), RoundedCornerShape(20.dp))
                .testTag("referral_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GroupAdd,
                            contentDescription = "Add Friends",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Refer 3 Friends = Unlock Lifetime PRO",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = Color(0xFF1B5E20)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Refer friends to test drive micro lessons. Reach 3 successful test accounts to automatically unlock the premium PRO pass!",
                    fontSize = 11.sp,
                    color = Color(0xFF2E7D32),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Refer check status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Invitation challenge progress",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = "${stats.referralsCount}/3 Accounts",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = Color(0xFF1B5E20)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                val referralPct = (stats.referralsCount.toFloat() / 3f).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { referralPct },
                    color = Color(0xFF1B5E20),
                    trackColor = Color(0xFFC8E6C9),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                val context = LocalContext.current
                Button(
                    onClick = {
                        viewModel.referFriend()
                        Toast.makeText(context, "🔗 Referral code shared! New friend test user simulated.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("simulate_referral_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Simulate Friend Sign-up Invitation", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Developer testing desk controls
        Text(
            text = "Testing Playground & Reset",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFC62828)
        )

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.2.dp, Color(0xFFC62828).copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Reset all application data tables or trigger a missed day streak breakdown instantly to test notification rules:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.simulateStreakBreak()
                            Toast.makeText(context, "💔 Missed Day simulated! Streak recalculated.", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("simulate_streak_break_btn")
                    ) {
                        Text("Simulate Miss", fontSize = 11.sp, textAlign = TextAlign.Center)
                    }

                    Button(
                        onClick = {
                            viewModel.resetUserProgress()
                            Toast.makeText(context, "🧹 All Room table rows successfully reset!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("reset_profile_progress_btn")
                    ) {
                        Text("Clean Reset ALL", fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStoreContent(viewModel: SkillViewModel) {
    val stats by viewModel.userStatsState.collectAsState()

    var selectedPlanIndex by remember { mutableStateOf(0) } // 0 = Monthly, 1 = Annual
    var showCheckoutSuccess by remember { mutableStateOf(false) }

    val benefits = listOf(
        Pair("Unlimited Daily Boosts", "Learn any career skill at any time without artificial pacing limits."),
        Pair("Advanced AI Assisting", "Direct ultra-fast dialogue with Coach Sparky, powered by Google Gemini."),
        Pair("Unlock Premium Roadmaps", "Get exclusive templates like public freelancing packs & corporate hacks."),
        Pair("Vibrant PDF Certificates", "Simulate, save, and share physical certificates of micro-skill completions.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Grand header card with high-class gradient colors
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF6750A4), Color(0xFF9E8AD4))
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Golden Crown icon",
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "SkillSpark PRO Pass",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Text(
                    text = if (stats.isPremium) "ACTIVE PASS SUBSCRIBER 👑" else "INVEST IN YOUR PROFESSIONAL REVENUE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFFD54F),
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Benefits display lists
        Text(
            text = "PRO PASS LIFETIME INCLUSIONS:",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 4.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            benefits.forEach { (title, description) ->
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Enabled Perk icon",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = description,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Price Plan selectors if not premium
        if (!stats.isPremium) {
            Text(
                text = "UPGRADE WITH SANDBOX LINK:",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Monthly Plan Box
                PlanSelectionBox(
                    title = "Monthly access",
                    price = "₹149",
                    period = "/month",
                    savingsText = "Standard rate",
                    isSelected = selectedPlanIndex == 0,
                    onClick = { selectedPlanIndex = 0 },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("plan_selector_monthly")
                )

                // Annual Plan Box
                PlanSelectionBox(
                    title = "Annual Gold Pass",
                    price = "₹59",
                    period = "/month",
                    savingsText = "Save 60% (₹699/yr)",
                    isSelected = selectedPlanIndex == 1,
                    onClick = { selectedPlanIndex = 1 },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("plan_selector_annual")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.simulatedPurchasePremium()
                    showCheckoutSuccess = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("buy_premium_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Upgrade with 1-Click Sandbox",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        } else {
            // Already Active Subscriber display
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "Active PRO icon",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You are a fully active SparkPro member!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "No restricted access, unrestricted career paths, and certificate printing are available.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.simulatedCancelPremium() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("cancel_premium_button")
                    ) {
                        Text("Simulate Sandbox Cancellation", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // Certificate Generation section if premium!
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.2.dp, Color(0xFF8BC34A), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF8BC34A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Seal",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Download Career Certificates",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = Color(0xFF33691E)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Your skills deserve validation. Download PDF certificates verifying your command over corporate communication or database design.",
                        fontSize = 11.sp,
                        color = Color(0xFF558B2F),
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.downloadCertificate() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("download_certificate_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF689F38)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download Link",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate & Download PDF Certificate", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    if (stats.certificatesDownloaded > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "✓ Simulated ${stats.certificatesDownloaded} files saved to user local system!",
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            color = Color(0xFF33691E),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Dialogue popup box on Sandbox purchase completion
        if (showCheckoutSuccess) {
            AlertDialog(
                onDismissRequest = { showCheckoutSuccess = false },
                title = { Text("Crown Active! 👑", fontWeight = FontWeight.Black) },
                text = {
                    Text("Successful sandbox payment completed. All SkillSpark Pro features (AI coach, certificates, paths) are unlocked instantly!")
                },
                confirmButton = {
                    Button(onClick = { showCheckoutSuccess = false }) {
                        Text("Let's Go!")
                    }
                }
            )
        }
    }
}

// Helper utility to explain structured course roadmap completions
fun reapCompletedLessonsInfo(stats: UserStats, totalRoadmaps: Int): String {
    return if (totalRoadmaps > 1) "$totalRoadmaps Roadmaps in-action" else "$totalRoadmaps Roadmap in-action"
}

@Composable
private fun StatsCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun BadgeCell(
    name: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isUnlocked: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(
                    if (isUnlocked) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .border(
                    width = 1.dp,
                    color = if (isUnlocked) MaterialTheme.colorScheme.primary else Color.LightGray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = if (isUnlocked) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.6f),
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = name,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 11.sp
        )
    }
}

@Composable
private fun PlanSelectionBox(
    title: String,
    price: String,
    period: String,
    savingsText: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(price, fontSize = 20.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                Text(period, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = savingsText,
                fontSize = 9.sp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
