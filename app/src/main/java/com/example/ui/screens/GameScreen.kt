package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.UserStats
import com.example.ui.viewmodel.SkillViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun GameScreen(
    viewModel: SkillViewModel,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val stats by viewModel.userStatsState.collectAsState()
    val completedLessons by viewModel.completedLessonsState.collectAsState()
    val chatMessages by viewModel.chatMessagesState.collectAsState()

    // Task & Quest States
    var checkedInToday by rememberSaveable { mutableStateOf(false) }
    var q1Claimed by rememberSaveable { mutableStateOf(false) }
    var q2Claimed by rememberSaveable { mutableStateOf(false) }
    var q3Claimed by rememberSaveable { mutableStateOf(false) }

    // Derive Spark Coins and Gems virtually
    // Ensures persistent data synchronization without altering the Room Database
    val virtualCoins = (stats.xp / 8) + (stats.streak * 15)
    var activeCoinsOffset by rememberSaveable { mutableIntStateOf(0) }
    val sparkCoins = (virtualCoins + activeCoinsOffset).coerceAtLeast(0)

    val virtualGems = (stats.level * 12) + (if (stats.isPremium) 50 else 0)

    // Spin Wheel Animation States
    var isSpinning by remember { mutableStateOf(false) }
    var wheelRotation by remember { mutableFloatStateOf(0f) }
    var showPrizeDialog by remember { mutableStateOf(false) }
    var wonPrizeText by remember { mutableStateOf("") }

    val completedIds = completedLessons.map { it.lessonId }.toSet()
    val lessonsDone = completedIds.isNotEmpty()
    val chatDone = chatMessages.any { it.senderType == "user" }

    // Dynamic Level progress calculations
    val (progressPct, currentLevelXp, targetLevelXp) = getLevelProgressForGame(stats.xp)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 40.dp)
    ) {
        // App Custom Game Header
        item {
            GameTopbarHeader(
                stats = stats,
                sparkCoins = sparkCoins,
                sparksGem = virtualGems
            )
        }

        // Gamer Level & Streak Progression Card
        item {
            GamerLevelCard(
                stats = stats,
                progressPct = progressPct,
                currentLevelXp = currentLevelXp,
                targetLevelXp = targetLevelXp,
                checkedInToday = checkedInToday,
                onCheckInClick = {
                    if (!checkedInToday) {
                        checkedInToday = true
                        viewModel.awardBonusXp(15)
                        Toast.makeText(context, "🏆 Checked in successfully! +15 XP rewarded!", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        // Active Challenges Quests Board
        item {
            val multiplier = if (stats.streak > 0) 1.2f else 1.0f
            DailyQuestsPanel(
                checkedInToday = checkedInToday,
                lessonDone = lessonsDone,
                chatDone = chatDone,
                q1Claimed = q1Claimed,
                q2Claimed = q2Claimed,
                q3Claimed = q3Claimed,
                multiplier = multiplier,
                onClaimQuest = { questIndex, xpReward ->
                    viewModel.awardBonusXp(xpReward)
                    when (questIndex) {
                        1 -> q1Claimed = true
                        2 -> q2Claimed = true
                        3 -> q3Claimed = true
                    }
                    Toast.makeText(context, "⚔️ Quest Claimed! +${xpReward} XP earned!", Toast.LENGTH_SHORT).show()
                },
                onGoChat = { onTabSelected(3) }, // Tab 3 is AI Coach now
                onGoLessons = { onTabSelected(0) } // Tab 0 is Home
            )
        }

        // Animated Lucky Reward Spin Wheel
        item {
            LuckyWheelCard(
                sparkCoins = sparkCoins,
                isSpinning = isSpinning,
                rotationDegrees = wheelRotation,
                onSpinClick = {
                    if (sparkCoins < 40) {
                        Toast.makeText(context, "🪙 Not enough Spark Coins! Complete quests or lessons to earn.", Toast.LENGTH_SHORT).show()
                    } else if (!isSpinning) {
                        coroutineScope.launch {
                            isSpinning = true
                            activeCoinsOffset -= 40 // Deduct 40 coins
                            val randomTurns = 4 + Random.nextInt(4)
                            val finalStopDegrees = Random.nextInt(360)
                            val targetRotation = wheelRotation + (randomTurns * 360) + finalStopDegrees
                            
                            animate(
                                initialValue = wheelRotation,
                                targetValue = targetRotation,
                                animationSpec = tween(durationMillis = 3500, easing = FastOutSlowInEasing)
                            ) { valValue, _ ->
                                wheelRotation = valValue
                            }
                            
                            // Determine prize based on degrees
                            val normalizedAngle = (wheelRotation % 360f + 360f) % 360f
                            val prizeIndex = ((normalizedAngle + 30) % 360 / 60).toInt()
                            val prizes = listOf(
                                "🎁 Double Quiz XP Token (Next Lesson)",
                                "👑 Multiplier Boost (+1.5x Multiplier)",
                                "🔥 Golden Streak Shield (Protects active streak)",
                                "⚡ +30 Bonus Experience Points (Granted now!)",
                                "🎨 Sparkling Crown custom theme avatar prefix",
                                "🍀 Extra Lucky Spin Ticket"
                            )
                            wonPrizeText = prizes.getOrElse(prizeIndex) { "Extra Lucky Badge" }
                            if (wonPrizeText.contains("+30 Bonus Experience")) {
                                viewModel.awardBonusXp(30)
                            }
                            isSpinning = false
                            showPrizeDialog = true
                        }
                    }
                }
            )
        }

        // Competitive Arena Leaderboard Widget
        item {
            ArenaLeaderboardCard(userXp = stats.xp, userLevelName = stats.levelName)
        }
    }

    // Modal show reward successful popup
    if (showPrizeDialog) {
        AlertDialog(
            onDismissRequest = { showPrizeDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy win",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Winning Prize unlocked!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = wonPrizeText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    Text(
                        text = "Your prize inventory has been virtually updated! Complete lessons to trigger further high-tier item rewards.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrizeDialog = false }
                ) {
                    Text("Superb!", fontWeight = FontWeight.Black)
                }
            }
        )
    }
}

@Composable
private fun GameTopbarHeader(
    stats: UserStats,
    sparkCoins: Int,
    sparksGem: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App theme Branding Logo Tag
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.SportsEsports,
                contentDescription = "Game Zone",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Gamified Arena",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Track daily goals & spin",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Virtual currency indicators
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Spark Coins
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFFAED))
                    .border(1.dp, Color(0xFFFFB300), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = "Coins",
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = sparkCoins.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFC77C00)
                )
            }

            // Sparks Gems
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8F5E9))
                    .border(1.dp, Color(0xFF4CAF50), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = "Gems",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = sparksGem.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1B5E20)
                )
            }
        }
    }
}

@Composable
private fun GamerLevelCard(
    stats: UserStats,
    progressPct: Float,
    currentLevelXp: Int,
    targetLevelXp: Int,
    checkedInToday: Boolean,
    onCheckInClick: () -> Unit
) {
    val progressAnimated by animateFloatAsState(targetValue = progressPct, label = "progressPct")

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().testTag("gamer_level_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Gamer Level badge & title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stats.level.toString(),
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Gamer Rank: ${stats.levelName}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.OfflineBolt,
                                contentDescription = "Active Energy",
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Daily multiplier activated",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                // Fire Hot Streak Indicator
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFF3E0))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = Color(0xFFFF2D2D),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${stats.streak} DAYS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFC62828)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Level boundaries progress line
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "XP Progress to Next Rank",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$currentLevelXp / $targetLevelXp XP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progressAnimated },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Playful weekly horizontal calendar checkins
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weekly Activity Roll",
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.4.sp
                )

                Text(
                    text = if (checkedInToday) "Checked in today!" else "Unclaimed bonus xp",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (checkedInToday) Color(0xFF4CAF50) else Color(0xFFFF9100)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row containing 5 days checkin marks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 1..5) {
                    val isChecked = i < stats.streak || (i == stats.streak && checkedInToday)
                    val isTodayActive = i == stats.streak && !checkedInToday

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when {
                                    isChecked -> Color(0xFFE8F5E9)
                                    isTodayActive -> MaterialTheme.colorScheme.primaryContainer
                                    else -> MaterialTheme.colorScheme.surface
                                }
                            )
                            .border(
                                width = 1.2.dp,
                                color = when {
                                    isChecked -> Color(0xFF81C784)
                                    isTodayActive -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.outlineVariant
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable(enabled = isTodayActive) { onCheckInClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isChecked) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Day $i Checked Status",
                                tint = Color(0xFF388E3C),
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Day $i",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTodayActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "+15",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isTodayActive) MaterialTheme.colorScheme.primary else Color.Gray
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
private fun DailyQuestsPanel(
    checkedInToday: Boolean,
    lessonDone: Boolean,
    chatDone: Boolean,
    q1Claimed: Boolean,
    q2Claimed: Boolean,
    q3Claimed: Boolean,
    multiplier: Float,
    onClaimQuest: (Int, Int) -> Unit,
    onGoChat: () -> Unit,
    onGoLessons: () -> Unit
) {
    val q1Reward = (15 * multiplier).toInt()
    val q2Reward = (35 * multiplier).toInt()
    val q3Reward = (25 * multiplier).toInt()

    val questsCompleted = (if (checkedInToday) 1 else 0) + (if (lessonDone) 1 else 0) + (if (chatDone) 1 else 0)

    val multiplierText = if (multiplier > 1.0f) " (+${((multiplier-1f)*100).toInt()}% Streak multiplier applied)" else ""

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().border(1.2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(22.dp)).testTag("quests_board")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Title and completed summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timeline,
                        contentDescription = "Quests Title",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Daily Battle Quests",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "$questsCompleted/3 Complete",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Complete these actions daily to claim rewards$multiplierText:",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quest rows
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Quest 1: Daily Check-In
                QuestRowItem(
                    title = "Establish Foundation",
                    subtitle = "Perform your daily streak check-in on the card above.",
                    xpReward = q1Reward,
                    isCompleted = checkedInToday,
                    isClaimed = q1Claimed,
                    onClaim = { onClaimQuest(1, q1Reward) },
                    onNavigate = {}
                )

                // Quest 2: Complete Skill of the Day
                QuestRowItem(
                    title = "Conquer Skill Realms",
                    subtitle = "Complete at least one professional course today.",
                    xpReward = q2Reward,
                    isCompleted = lessonDone,
                    isClaimed = q2Claimed,
                    onClaim = { onClaimQuest(2, q2Reward) },
                    onNavigate = onGoLessons,
                    navLabel = "Lessons"
                )

                // Quest 3: Meet Coach Sparky
                QuestRowItem(
                    title = "Dialogue with Sparky",
                    subtitle = "Send at least one dialogue message to the AI coach today.",
                    xpReward = q3Reward,
                    isCompleted = chatDone,
                    isClaimed = q3Claimed,
                    onClaim = { onClaimQuest(3, q3Reward) },
                    onNavigate = onGoChat,
                    navLabel = "Chat"
                )
            }
        }
    }
}

@Composable
private fun QuestRowItem(
    title: String,
    subtitle: String,
    xpReward: Int,
    isCompleted: Boolean,
    isClaimed: Boolean,
    onClaim: () -> Unit,
    onNavigate: () -> Unit,
    navLabel: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isClaimed) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            if (isCompleted) Color(0xFF4CAF50) else Color(0xFFFFB300)
                        )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = if (isClaimed) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (isClaimed) TextDecoration.LineThrough else TextDecoration.None
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                lineHeight = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Action button (Claim or Go/Locked)
        if (isCompleted && !isClaimed) {
            Button(
                onClick = onClaim,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text(
                    text = "CLAIM +$xpReward XP",
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp,
                    color = Color.White
                )
            }
        } else if (isClaimed) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Claimed Done Status",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(24.dp).padding(end = 4.dp)
            )
        } else {
            if (navLabel != null) {
                OutlinedButton(
                    onClick = onNavigate,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        text = "GO TO $navLabel",
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+$xpReward XP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun LuckyWheelCard(
    sparkCoins: Int,
    isSpinning: Boolean,
    rotationDegrees: Float,
    onSpinClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(22.dp))
            .testTag("lucky_wheel_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.OfflineBolt,
                    contentDescription = "Lucky Spin",
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Doubt to Courage Spin",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Cost: 40 Spark Coins per lucky spin",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Graphical Wheel Visual Representation
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                // Drawing sectors using canvas rotated dynamically
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(rotationDegrees)
                ) {
                    val colors = listOf(
                        Color(0xFFE3F2FD), Color(0xFFFFF3E0),
                        Color(0xFFF1F8E9), Color(0xFFF3E5F5),
                        Color(0xFFFFEBEE), Color(0xFFE0F7FA)
                    )
                    
                    // Draw 6 segments of 60 degrees each
                    for (i in 0 until 6) {
                        drawArc(
                            color = colors[i],
                            startAngle = i * 60f,
                            sweepAngle = 60f,
                            useCenter = true
                        )
                    }
                    
                    // Outer stroke border
                    drawCircle(
                        color = Color.LightGray,
                        radius = size.width / 2f,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }

                // Center spinner arrow/pin
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, Color.Gray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Pointer",
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onSpinClick,
                enabled = !isSpinning && sparkCoins >= 40,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("spin_wheel_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSpinning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Spinning Segment...", fontWeight = FontWeight.Black)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Cached,
                            contentDescription = "Spin",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Lucky Spin! (Costs 40🪙)", fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

@Composable
private fun ArenaLeaderboardCard(
    userXp: Int,
    userLevelName: String
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(22.dp))
            .testTag("leaderboard_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Competitive arena header title and subtitle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Leaderboard,
                    contentDescription = "Leaderboard",
                    tint = Color(0xFFFF9100),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Global Training Arena",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Real-time mock standings of high-growth trainees",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mock rivals list sorted dynamically by XP values
            val rivals = remember(userXp) {
                listOf(
                    Pair("Chloe (Consultant Pro)", 2450),
                    Pair("Sarah (SaaS Marketer)", 1880),
                    Pair("You (Trainee Pioneer)", userXp),
                    Pair("Alex (Freelance Growth)", 950),
                    Pair("Devin (Tech Explorer)", 420)
                ).sortedByDescending { it.second }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                rivals.forEachIndexed { index, rival ->
                    val isCurrentUser = rival.first.startsWith("You")
                    val positionText = when (index) {
                        0 -> "🥇"
                        1 -> "🥈"
                        2 -> "🥉"
                        else -> "  ${index + 1}"
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isCurrentUser) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                else Color.Transparent
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = positionText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(36.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = if (isCurrentUser) "${rival.first} ($userLevelName)" else rival.first,
                                fontWeight = if (isCurrentUser) FontWeight.Black else FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = "${rival.second} XP",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// Utility to calculate leveling boundaries inside the GameScreen
fun getLevelProgressForGame(xp: Int): Triple<Float, Int, Int> {
    return when {
        xp < 500 -> Triple(xp / 500f, xp, 500)
        xp < 1500 -> Triple((xp - 500) / 1000f, xp - 500, 1000)
        xp < 3000 -> Triple((xp - 1500) / 1500f, xp - 1500, 1500)
        xp < 5000 -> Triple((xp - 3000) / 2000f, xp - 3000, 2000)
        else -> Triple(1f, 5000, 5000)
    }
}
