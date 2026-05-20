package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Lesson
import com.example.ui.viewmodel.SkillViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LessonScreen(
    viewModel: SkillViewModel,
    modifier: Modifier = Modifier
) {
    val lesson by viewModel.currentLesson.collectAsState()
    val activeSlideIndex by viewModel.activeSlideIndex.collectAsState()
    val isInQuizMode by viewModel.isInQuizMode.collectAsState()
    val activeQuestionIndex by viewModel.activeQuestionIndex.collectAsState()
    val selectedOptionIndex by viewModel.selectedOptionIndex.collectAsState()
    val showFeedback by viewModel.showQuizFeedback.collectAsState()
    val isCorrectOption by viewModel.isCorrectOption.collectAsState()
    val isFinished by viewModel.isLessonFinished.collectAsState()
    val unlockedBadge by viewModel.unlockedBadge.collectAsState()

    val currentLesson = lesson ?: return

    // Calculate overall progress (0.0 to 1.0)
    val totalSteps = currentLesson.slides.size + currentLesson.quizQuestions.size
    val currentStep = if (isInQuizMode) {
        currentLesson.slides.size + activeQuestionIndex
    } else {
        activeSlideIndex
    }
    val progress = (currentStep.toFloat() / totalSteps.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { viewModel.exitLesson() },
                        modifier = Modifier.testTag("exit_lesson_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit Lesson",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Progress Bar mimicking Duolingo
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(CircleShape),
                        color = Color(0xFF4CAF50), // Emerald Green
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Step ${currentStep + 1}/$totalSteps",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (isFinished) {
                        Button(
                            onClick = { viewModel.exitLesson() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("lesson_finish_continue_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                "Continue to Dashboard",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else if (isInQuizMode) {
                        if (!showFeedback) {
                            Button(
                                onClick = { /* Option is self-submitting to give instant feedback */ },
                                enabled = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Select an Answer", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            // High feedback button
                            val btnColor = if (isCorrectOption) Color(0xFF4CAF50) else Color(0xFFE53935)
                            Button(
                                onClick = { viewModel.nextQuizQuestion() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .testTag("quiz_next_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = btnColor),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        if (isCorrectOption) "Awesome! Next Question" else "Got it, Next",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Next",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    } else {
                        Button(
                            onClick = { viewModel.nextSlide() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("lesson_next_slide_btn"),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("Understand & Next", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Next Slide"
                                )
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        AnimatedContent(
            targetState = isFinished,
            transitionSpec = {
                fadeIn(animationSpec = spring()) togetherWith fadeOut(animationSpec = spring())
            },
            label = "lessonContentTransition"
        ) { finishedState ->
            if (finishedState) {
                LessonCompletionScreen(
                    lesson = currentLesson,
                    unlockedBadge = unlockedBadge,
                    modifier = Modifier.padding(innerPadding)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isInQuizMode) {
                        val currentQuestion = currentLesson.quizQuestions.getOrNull(activeQuestionIndex)
                        if (currentQuestion != null) {
                            QuizView(
                                question = currentQuestion,
                                selectedIndex = selectedOptionIndex,
                                showFeedback = showFeedback,
                                isCorrect = isCorrectOption,
                                onOptionSelected = { index -> viewModel.selectQuizOption(index) }
                            )
                        }
                    } else {
                        val currentSlide = currentLesson.slides.getOrNull(activeSlideIndex)
                        if (currentSlide != null) {
                            SlideView(slide = currentSlide)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SlideView(slide: com.example.data.model.LessonSlide) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(slide) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(animationSpec = tween(300)),
        exit = shrinkVertically() + fadeOut(animationSpec = tween(300))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Visual Banner mimicking gamification
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Lesson Topic",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = slide.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Explanation Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = slide.content,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Example Interactive Board
            if (slide.example.isNotEmpty()) {
                Text(
                    text = "REAL-WORLD REFERENCE",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), // Code / High contrast look
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = slide.example,
                            color = Color(0xFF00FFCC), // Radiant mint terminals
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Pro Tip Board
            if (slide.tip.isNotEmpty()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Pro Tip",
                            tint = Color(0xFFFFB300), // Glowing Amber
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "PRO-TIP BONUS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                slide.tip,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizView(
    question: com.example.data.model.QuizQuestion,
    selectedIndex: Int?,
    showFeedback: Boolean,
    isCorrect: Boolean,
    onOptionSelected: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "DAILY CHALLENGE QUIZ",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
        }

        Text(
            text = question.question,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 28.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Radio List Options
        question.options.forEachIndexed { index, option ->
            val isSelected = selectedIndex == index
            
            val borderBrush = when {
                showFeedback && index == question.correctAnswerIndex -> {
                    // Correct green
                    androidx.compose.ui.graphics.SolidColor(Color(0xFF4CAF50))
                }
                showFeedback && isSelected && !isCorrect -> {
                    // Wrong red
                    androidx.compose.ui.graphics.SolidColor(Color(0xFFE53935))
                }
                isSelected -> {
                    // Accent Blue
                    androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary)
                }
                else -> {
                    // Neutral grey border
                    androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outlineVariant)
                }
            }

            val cardColor = when {
                showFeedback && index == question.correctAnswerIndex -> Color(0xFFE8F5E9) // soft green
                showFeedback && isSelected && !isCorrect -> Color(0xFFFFEBEE) // soft red
                isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                else -> MaterialTheme.colorScheme.surface
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(2.dp, borderBrush, RoundedCornerShape(16.dp))
                    .clickable { onOptionSelected(index) }
                    .testTag("quiz_option_$index")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .border(
                                2.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                CircleShape
                            )
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quiz Detailed Explanations / Feedback Panel
        AnimatedVisibility(
            visible = showFeedback,
            enter = slideInVertically(initialOffsetY = { 100 }) + fadeIn()
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCorrect) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE53935)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCorrect) Icons.Default.ThumbUp else Icons.Default.Error,
                            contentDescription = if (isCorrect) "Correct" else "Incorrect",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isCorrect) "EXCELLENT WORK OUTCOME!" else "OPPORTUNITY TO LEARN!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LessonCompletionScreen(
    lesson: Lesson,
    unlockedBadge: String?,
    modifier: Modifier = Modifier
) {
    var animateMetrics by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animateMetrics = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Floating Confetti effect simulated with high-contrast icon animations
        val bounceOffset by animateDpAsState(
            targetValue = if (animateMetrics) 0.dp else 40.dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "bounce"
        )

        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(y = bounceOffset)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFD54F), Color(0xFFFFB300)) // Radiant Gold Trophy Base
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Trophy Cup",
                tint = Color.White,
                modifier = Modifier.size(90.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Lesson Completed!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = lesson.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // XP Gains panel
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Reward Cards
            RewardCardItem(
                title = "XP Gained",
                value = "+${lesson.xpReward} XP",
                icon = Icons.Default.Star,
                tint = Color(0xFFFF9800)
            )

            RewardCardItem(
                title = "Time Spent",
                value = "~5 Mins",
                icon = Icons.Default.Timer,
                tint = Color(0xFF03A9F4)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Badges Section Unlocked Feedback
        unlockedBadge?.let { badge ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF8E24AA).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.OfflineBolt,
                            contentDescription = "Badge Icon",
                            tint = Color(0xFFBA68C8),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            "NEW BADGE UNLOCKED!",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            "\"$badge\"",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RewardCardItem(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        ),
        modifier = Modifier
            .width(140.dp)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
