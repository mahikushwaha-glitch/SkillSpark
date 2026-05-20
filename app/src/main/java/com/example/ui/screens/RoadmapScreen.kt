package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.UserStats
import com.example.data.database.RoadmapProgress
import com.example.data.model.Roadmap
import com.example.data.model.RoadmapTask
import com.example.ui.viewmodel.SkillViewModel

@Composable
fun RoadmapScreen(
    viewModel: SkillViewModel,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val stats by viewModel.userStatsState.collectAsState()
    val progressList by viewModel.roadmapProgressState.collectAsState()
    val roadmaps = viewModel.getRoadmaps()

    var activeRoadmap by remember { mutableStateOf<Roadmap?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        // Simple animated title shift back/forth
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(vertical = 12.dp)
        ) {
            if (activeRoadmap != null) {
                IconButton(
                    onClick = { activeRoadmap = null },
                    modifier = Modifier.testTag("back_to_roadmaps_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = if (activeRoadmap == null) "Skill Roadmaps" else activeRoadmap!!.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        if (activeRoadmap == null) {
            // Display Grid of available learning paths
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .testTag("roadmaps_list"),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Text(
                        text = "Complete daily path tasks to unlock career badges and build consistent habits.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                items(roadmaps) { roadmap ->
                    val roadProgress = progressList.firstOrNull { it.roadmapId == roadmap.id }
                    val completedCount = roadProgress?.completedDaysText?.split(",")
                        ?.filter { it.isNotEmpty() }?.size ?: 0
                    
                    val mapProgressFraction = (completedCount.toFloat() / roadmap.durationDays.toFloat()).coerceIn(0f, 1f)

                    RoadmapListCard(
                        roadmap = roadmap,
                        progressFraction = mapProgressFraction,
                        completedCount = completedCount,
                        isUserPremium = stats.isPremium,
                        onClick = {
                            if (roadmap.isPremium && !stats.isPremium) {
                                onTabSelected(4) // Direct to Store
                            } else {
                                activeRoadmap = roadmap
                            }
                        }
                    )
                }
            }
        } else {
            val currentRoadProgress = progressList.firstOrNull { it.roadmapId == activeRoadmap!!.id }
            val completedDays = currentRoadProgress?.completedDaysText?.split(",")
                ?.filter { it.isNotEmpty() }?.map { it.toInt() }?.toSet() ?: emptySet()

            // Display active task list for Expanded Roadmap
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .testTag("roadmap_tasks_list"),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Prize",
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "ROADMAP REWARD",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Earn \"${activeRoadmap!!.badgeReward}\" Badge + 50 XP per day action!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(activeRoadmap!!.tasks) { task ->
                    val isDayDone = completedDays.contains(task.day)
                    
                    RoadmapTaskItemRow(
                        task = task,
                        isCompleted = isDayDone,
                        onCheckboxClick = {
                            viewModel.completeRoadmapTask(activeRoadmap!!.id, task.day)
                        },
                        onPlayLesson = {
                            val targetLesson = viewModel.getLessons().firstOrNull { it.id == task.lessonId }
                            if (targetLesson != null) {
                                viewModel.startLesson(targetLesson)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RoadmapListCard(
    roadmap: Roadmap,
    progressFraction: Float,
    completedCount: Int,
    isUserPremium: Boolean,
    onClick: () -> Unit
) {
    val isLocked = roadmap.isPremium && !isUserPremium

    val cardBorderColor = when {
        isLocked -> MaterialTheme.colorScheme.outlineVariant
        progressFraction >= 1f -> Color(0xFF4CAF50).copy(alpha = 0.6f) // Complete green
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, cardBorderColor, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .testTag("roadmap_card_${roadmap.id}")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Category Tag Bubble
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = roadmap.category.uppercase(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Locked vs Premium icon indicators
                if (isLocked) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFFEBEE))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = Color(0xFFC62828),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "PRO PASS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828)
                            )
                        }
                    }
                } else if (roadmap.isPremium) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE0F7FA))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = "Premium Path",
                                tint = Color(0xFF00ACC1),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "PRO PATH",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00838F)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = roadmap.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = roadmap.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Progress bar and indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Path Metrics",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$completedCount/${roadmap.durationDays} Micro Days",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        color = if (progressFraction >= 1f) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isLocked) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLocked) Icons.Default.LockOpen else Icons.Default.ChevronRight,
                        contentDescription = "Explore",
                        tint = if (isLocked) MaterialTheme.colorScheme.onSurfaceVariant else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RoadmapTaskItemRow(
    task: RoadmapTask,
    isCompleted: Boolean,
    onCheckboxClick: () -> Unit,
    onPlayLesson: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp) else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isCompleted) Color(0xFF4CAF50).copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(16.dp)
            )
            .testTag("roadmap_task_${task.day}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Circular Day tag
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (isCompleted) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.primaryContainer
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "D${task.day}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = if (isCompleted) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Titles & Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.taskTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.taskDescription,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Custom Checkbox
                IconButton(
                    onClick = { onCheckboxClick() },
                    modifier = Modifier.testTag("task_checkbox_day_${task.day}")
                ) {
                    Icon(
                        imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Complete Day Checkbox",
                        tint = if (isCompleted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            // Optional direct lesson shortcut
            if (task.lessonId != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onPlayLesson() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompleted) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("roadmap_play_lesson_${task.lessonId}"),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Play",
                            tint = if (isCompleted) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isCompleted) "Replay Matched Lesson" else "Play Micro Boost (+100 XP)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isCompleted) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}
