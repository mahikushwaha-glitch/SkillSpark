package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.UserStats
import com.example.data.model.Lesson
import com.example.ui.viewmodel.SkillViewModel

@Composable
fun HomeScreen(
    viewModel: SkillViewModel,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val stats by viewModel.userStatsState.collectAsState()
    val completedLessons by viewModel.completedLessonsState.collectAsState()
    val chatMessages by viewModel.chatMessagesState.collectAsState()
    val lessons = viewModel.getLessons()

    val completedIds = completedLessons.map { it.lessonId }.toSet()

    // Active Skill of the day (First incomplete lesson, or first lesson if all done)
    val skillOfTheDay = lessons.firstOrNull { it.id !in completedIds } ?: lessons.first()

    var selectedCategoryFilter by remember { mutableStateOf("All") }
    val categories = listOf(
        "All", "Productivity", "Tech Skills", "Finance", "Communication",
        "Career Growth", "Business", "Learning", "Growth", "Success", "Self Help"
    )

    val filteredLessons = if (selectedCategoryFilter == "All") {
        lessons
    } else {
        lessons.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
    }

    // Dynamic quest claiming states
    var checkedInToday by rememberSaveable { mutableStateOf(false) }
    var q1Claimed by rememberSaveable { mutableStateOf(false) }
    var q2Claimed by rememberSaveable { mutableStateOf(false) }
    var q3Claimed by rememberSaveable { mutableStateOf(false) }

    // Multiplier for premium users to feel even more gamer-centric
    val multiplier = if (stats.isPremium) 1.5f else 1.0f

    // Calculate level progression
    val (progressPct, currentLevelXp, targetLevelXp) = getLevelProgress(stats.xp)

    // Calculate realm completion metrics
    val categoryMetrics = remember(lessons, completedIds) {
        categories.filter { it != "All" }.map { cat ->
            val total = lessons.count { it.category.equals(cat, ignoreCase = true) }
            val completed = lessons.count { it.category.equals(cat, ignoreCase = true) && it.id in completedIds }
            val color = when (cat) {
                "Productivity" -> Color(0xFF3F51B5)
                "Tech Skills" -> Color(0xFF6750A4)
                "Finance" -> Color(0xFFE65100)
                "Communication" -> Color(0xFF137333)
                "Career Growth" -> Color(0xFF7D5260)
                "Business" -> Color(0xFF00796B)
                "Learning" -> Color(0xFF8E24AA)
                "Growth" -> Color(0xFF0288D1)
                "Success" -> Color(0xFFD81B60)
                "Self Help" -> Color(0xFFE53935)
                else -> Color.Gray
            }
            Triple(cat, "$completed/$total", color)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        // Simple and elegant study dashboard header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Vocational Realms",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Master modern high-growth careers step-by-step",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Spark Points summary
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.OfflineBolt,
                        contentDescription = "Total XP",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${stats.xp} XP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Skill Realm Completion status
        item {
            RealmProgressTracker(metrics = categoryMetrics)
        }

        // Skill Of the Day Challenge
        item {
            SkillOfDayCard(
                lesson = skillOfTheDay,
                isCompleted = completedIds.contains(skillOfTheDay.id),
                onPlay = { viewModel.startLesson(skillOfTheDay) }
            )
        }

        // Monetization store banner trigger
        item {
            if (!stats.isPremium) {
                PremiumPromoBanner(onClick = { onTabSelected(4) })
            } else {
                ActiveProBanner()
            }
        }

        // Exploration heading
        item {
            Column {
                Text(
                    text = "Explore Skill Realms",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable category chip row
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 4.dp)
                ) {
                    items(categories) { categoryName ->
                        val isSelected = categoryName == selectedCategoryFilter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategoryFilter = categoryName },
                            label = { Text(categoryName, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.testTag("filter_chip_$categoryName")
                        )
                    }
                }
            }
        }

        // Lesson Cards
        if (filteredLessons.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No lessons found in this realm yet!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(filteredLessons) { item ->
                val isCompleted = completedIds.contains(item.id)
                LessonGridItemCard(
                    lesson = item,
                    isCompleted = isCompleted,
                    onPlay = { viewModel.startLesson(item) }
                )
            }
        }
    }
}

// Utility to calculate leveling boundaries consistent with repository levels
fun getLevelProgress(xp: Int): Triple<Float, Int, Int> {
    return when {
        xp < 500 -> Triple(xp / 500f, xp, 500)
        xp < 1500 -> Triple((xp - 500) / 1000f, xp - 500, 1000)
        xp < 3000 -> Triple((xp - 1500) / 1500f, xp - 1500, 1500)
        xp < 5000 -> Triple((xp - 3000) / 2000f, xp - 3000, 2000)
        else -> Triple(1f, 5000, 5000)
    }
}

@Composable
private fun TopAppBarHeader(
    stats: UserStats,
    onStreakClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // App logo branding
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF6750A4), Color(0xFFD0BCFF))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Logo",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "SkillSpark",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "MICRO LEARNING GAME",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.2.sp
                )
            }
        }

        // Header Quick HUD
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFFFF3E0).copy(alpha = 0.8f))
                    .border(1.dp, Color(0xFFFF9800).copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .clickable { onStreakClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = Color(0xFFFF5722),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${stats.streak}d",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = Color(0xFFE65100)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFEDE7F6).copy(alpha = 0.8f))
                    .border(1.dp, Color(0xFF673AB7).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = "XP Stars",
                        tint = Color(0xFF673AB7),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${stats.xp} XP",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = Color(0xFF4A148C)
                    )
                }
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
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(28.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Level and Title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFFFC107), Color(0xFFFF5722))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${stats.level}",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Level ${stats.level} Warrior",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "TIER RANK: ${stats.levelName.uppercase()}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Ribbon / Check status badge for premium
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (stats.isPremium) Color(0xFFBA68C8).copy(alpha = 0.15f) else Color(0xFF757575).copy(alpha = 0.1f),
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text(
                        text = if (stats.isPremium) "👑 PRO MULTIPLIER 1.5x" else "Standard pass 1.0x",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = if (stats.isPremium) Color(0xFF8E24AA) else Color(0xFF616161),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Level progress indicator
            LinearProgressIndicator(
                progress = { progressPct },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Level limits numerical text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progress: $currentLevelXp / $targetLevelXp XP to Next Level",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${(progressPct * 100).toInt()}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(14.dp))

            // Daily Check-In mini section tracker
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "📅 Weekly Check-in Streak",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Claim daily bonus to level up faster",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (!checkedInToday) {
                        Button(
                            onClick = onCheckInClick,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("CLAIM +15 XP", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Done",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "CHECKED TODAY",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Weekly 7-day visual calendar check dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    for (i in 0 until 7) {
                        val isAchieved = i < stats.streak
                        val isCheckInTodayIndicator = i == stats.streak && !checkedInToday
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isAchieved -> Color(0xFFE8F5E9)
                                                isCheckInTodayIndicator -> MaterialTheme.colorScheme.primaryContainer
                                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                            }
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = when {
                                                isAchieved -> Color(0xFF4CAF50)
                                                isCheckInTodayIndicator -> MaterialTheme.colorScheme.primary
                                                else -> Color.Transparent
                                            },
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isAchieved) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Checked",
                                            tint = Color(0xFF4CAF50),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.LocalFireDepartment,
                                            contentDescription = "Lock",
                                            tint = if (isCheckInTodayIndicator) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.4f),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = days[i],
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAchieved) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
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
    onGoChat: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1B24) // Dynamic Dark Card theme for extreme standout gamification
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Quests Title",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "⚔️ TODAY'S HERO QUESTS",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2E7D32))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    val questsCompleted = (if (checkedInToday) 1 else 0) + (if (lessonDone) 1 else 0) + (if (chatDone) 1 else 0)
                    Text(
                        text = "$questsCompleted/3 DONE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Track your action goals and extract extra XP bonuses",
                fontSize = 11.sp,
                color = Color.LightGray.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quest Rows
            val q1Reward = (15 * multiplier).toInt()
            val q2Reward = (50 * multiplier).toInt()
            val q3Reward = (20 * multiplier).toInt()

            QuestRowItem(
                title = "Daily Expedition Check-in",
                rewardXp = q1Reward,
                isCompleted = checkedInToday,
                isClaimed = q1Claimed,
                onClaim = { onClaimQuest(1, q1Reward) }
            )

            HorizontalDivider(color = Color.White.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 10.dp))

            QuestRowItem(
                title = "Brain Forge: Finish Skill of the Day",
                rewardXp = q2Reward,
                isCompleted = lessonDone,
                isClaimed = q2Claimed,
                onClaim = { onClaimQuest(2, q2Reward) }
            )

            HorizontalDivider(color = Color.White.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 10.dp))

            QuestRowItem(
                title = "Sage Chat: Send Consult to AI Coach",
                rewardXp = q3Reward,
                isCompleted = chatDone,
                isClaimed = q3Claimed,
                onClaim = { onClaimQuest(3, q3Reward) },
                actionButton = {
                    if (!chatDone) {
                        Button(
                            onClick = onGoChat,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f)),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("GO COACH", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun QuestRowItem(
    title: String,
    rewardXp: Int,
    isCompleted: Boolean,
    isClaimed: Boolean,
    onClaim: () -> Unit,
    actionButton: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) Color(0xFF2E7D32) else Color.White.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.Lock,
                    contentDescription = "Status",
                    tint = if (isCompleted) Color.White else Color.Gray,
                    modifier = Modifier.size(12.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (isClaimed) Color.Gray else Color.White,
                    textDecoration = if (isClaimed) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = "Bonus: +$rewardXp XP",
                    fontSize = 10.sp,
                    color = if (isCompleted && !isClaimed) Color(0xFFFFB300) else Color.Gray
                )
            }
        }

        if (isCompleted) {
            if (!isClaimed) {
                Button(
                    onClick = onClaim,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("CLAIM", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Claimed",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("CLAIMED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                }
            }
        } else {
            actionButton?.invoke()
        }
    }
}

@Composable
fun RealmProgressTracker(
    metrics: List<Triple<String, String, Color>>
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🏰 REALM MASTERY CHART",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Scrollable row of categories progress indicators to avoid crowding on 10 realms!
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(metrics) { (catName, ratioText, tint) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(60.dp)
                    ) {
                        // Round representation representing visual progress levels
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(tint.copy(alpha = 0.1f))
                                .border(1.5.dp, tint.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ratioText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = tint
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (catName == "Career Growth") "Career" else if (catName == "Tech Skills") "Tech" else if (catName == "Self Help") "Self-Help" else catName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SkillOfDayCard(
    lesson: Lesson,
    isCompleted: Boolean,
    onPlay: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlay() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF6750A4), Color(0xFFD0BCFF))
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "SKILL OF THE DAY 🔥",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }

                    if (isCompleted) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50))
                                .padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = lesson.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = lesson.description,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.82f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Time",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${lesson.durationText} daily micro boost",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable { onPlay() }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("start_daily_lesson_btn")
                    ) {
                        Text(
                            text = if (isCompleted) "Review Lesson" else "Boost Now (+${lesson.xpReward} XP)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF6750A4)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumPromoBanner(
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, Color(0xFFBA68C8), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("premium_promo_banner")
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBA68C8)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Premium Status",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Unlock SparkPro Full Pass",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = Color(0xFF4A148C)
                    )
                    Text(
                        text = "Get 1.5x Multiplier, unlimited coaching, verified certificates",
                        fontSize = 12.sp,
                        color = Color(0xFF7B1FA2),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Go",
                tint = Color(0xFF7B1FA2),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ActiveProBanner() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, Color(0xFF00ACC1), RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00ACC1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = "Premium King Cup",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "SkillSpark PRO Pass Active 👑",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color(0xFF006064)
                )
                Text(
                    text = "1.5x quest XP multiplier active! Enjoy unlimited AI coaching & fast path certifications.",
                    fontSize = 12.sp,
                    color = Color(0xFF00838F)
                )
            }
        }
    }
}

@Composable
fun LessonGridItemCard(
    lesson: Lesson,
    isCompleted: Boolean,
    onPlay: () -> Unit
) {
    val (icon, tint) = when (lesson.category) {
        "Productivity" -> Pair(Icons.Default.TrendingUp, Color(0xFF3F51B5))
        "Tech Skills" -> Pair(Icons.Default.Code, Color(0xFF6750A4))
        "Finance" -> Pair(Icons.Default.AccountBalanceWallet, Color(0xFFE65100))
        "Communication" -> Pair(Icons.Default.QuestionAnswer, Color(0xFF137333))
        "Career Growth" -> Pair(Icons.Default.School, Color(0xFF7D5260))
        "Business" -> Pair(Icons.Default.Storefront, Color(0xFF00796B))
        "Learning" -> Pair(Icons.Default.Lightbulb, Color(0xFF8E24AA))
        "Growth" -> Pair(Icons.Default.Bolt, Color(0xFF0288D1))
        "Success" -> Pair(Icons.Default.EmojiEvents, Color(0xFFD81B60))
        "Self Help" -> Pair(Icons.Default.Star, Color(0xFFE53935))
        else -> Pair(Icons.Default.Book, Color(0xFF9E9E9E))
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isCompleted) Color(0xFF4CAF50).copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(24.dp)
            )
            .clickable { onPlay() }
            .testTag("lesson_card_${lesson.id}")
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(tint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = lesson.category,
                    tint = tint,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = lesson.category.uppercase(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp,
                        color = tint,
                        letterSpacing = 0.5.sp
                    )
                    if (isCompleted) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "COMPLETED",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                              )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = lesson.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = lesson.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            if (isCompleted) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = { onPlay() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Lesson",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
