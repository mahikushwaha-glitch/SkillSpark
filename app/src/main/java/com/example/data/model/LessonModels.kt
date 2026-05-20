package com.example.data.model

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

data class LessonSlide(
    val title: String,
    val content: String,
    val example: String,
    val tip: String = ""
)

data class Lesson(
    val id: Int,
    val title: String,
    val category: String, // "Productivity", "Tech Skills", "Finance", "Communication", "Career Growth"
    val durationText: String = "5 Min",
    val description: String,
    val slides: List<LessonSlide>,
    val quizQuestions: List<QuizQuestion>,
    val xpReward: Int = 100,
    val badgeUnlocked: String? = null
)

data class RoadmapTask(
    val day: Int,
    val taskTitle: String,
    val taskDescription: String,
    val lessonId: Int? = null
)

data class Roadmap(
    val id: String,
    val title: String,
    val description: String,
    val category: String, // "Productivity", "Tech Skills", "Finance", "Communication", "Career Growth"
    val durationDays: Int,
    val tasks: List<RoadmapTask>,
    val badgeReward: String,
    val isPremium: Boolean = false
)
