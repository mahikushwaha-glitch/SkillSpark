package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.SkillViewModel

@Composable
fun DashboardScreen(
    viewModel: SkillViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val activeLesson by viewModel.currentLesson.collectAsState()

    if (activeLesson != null) {
        // Full bleed Active Lesson interface taking control over the dashboard, duplicating Duolingo flows
        LessonScreen(viewModel = viewModel)
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("app_navigation_bar")
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        label = { Text("Home") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home Screen"
                            )
                        },
                        modifier = Modifier.testTag("nav_tab_home")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        label = { Text("Roadmaps") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = "Roadmaps Section"
                            )
                        },
                        modifier = Modifier.testTag("nav_tab_roadmaps")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        label = { Text("Quests") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.SportsEsports,
                                contentDescription = "Gamified Quests"
                            )
                        },
                        modifier = Modifier.testTag("nav_tab_quests")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        label = { Text("AI Coach") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = "Coach Sparky Advisor"
                            )
                        },
                        modifier = Modifier.testTag("nav_tab_coach")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        label = { Text("Profile") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "User Profile"
                            )
                        },
                        modifier = Modifier.testTag("nav_tab_profile")
                    )
                }
            },
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Floating Notification bells simulator
                    IconButton(
                        onClick = {
                            // Quick alert tip
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Reminders Alert",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Easy light-dark theme switch triggers
                    IconButton(
                        onClick = { onToggleTheme() },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .testTag("theme_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme Selection",
                            tint = if (isDarkTheme) Color(0xFFFFB300) else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        slideInHorizontally(initialOffsetX = { x -> if (targetState > initialState) x else -x }) + fadeIn() togetherWith
                        slideOutHorizontally(targetOffsetX = { x -> if (targetState > initialState) -x else x }) + fadeOut()
                    },
                    label = "tabScreenTransitions"
                ) { tab ->
                    when (tab) {
                        0 -> HomeScreen(
                            viewModel = viewModel,
                            onTabSelected = { selectedTab = it }
                        )
                        1 -> RoadmapScreen(
                            viewModel = viewModel,
                            onTabSelected = { selectedTab = it }
                        )
                        2 -> GameScreen(
                            viewModel = viewModel,
                            onTabSelected = { selectedTab = it }
                        )
                        3 -> ChatScreen(
                            viewModel = viewModel
                        )
                        4 -> ProfileScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
