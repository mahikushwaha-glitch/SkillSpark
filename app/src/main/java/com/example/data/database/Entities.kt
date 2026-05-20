package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey val id: Int = 1,
    val streak: Int = 0,
    val maxStreak: Int = 0,
    val xp: Int = 0,
    val level: Int = 1,
    val levelName: String = "Beginner",
    val isPremium: Boolean = false,
    val lastActiveDate: String = "",
    val referralsCount: Int = 0,
    val certificatesDownloaded: Int = 0
)

@Entity(tableName = "completed_lessons")
data class CompletedLesson(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lessonId: Int,
    val category: String,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val senderType: String, // "user" or "assistant"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "roadmap_progress")
data class RoadmapProgress(
    @PrimaryKey val roadmapId: String,
    val currentDay: Int = 1,
    val completedDaysText: String = "" // Comma-separated list of completed days: "1,2,3"
)
