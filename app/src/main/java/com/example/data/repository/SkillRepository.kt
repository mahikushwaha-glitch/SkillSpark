package com.example.data.repository

import com.example.data.database.*
import com.example.data.model.Lesson
import com.example.data.model.Roadmap
import com.example.data.model.StaticData
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class SkillRepository(
    private val userStatsDao: UserStatsDao,
    private val completedLessonDao: CompletedLessonDao,
    private val chatMessageDao: ChatMessageDao,
    private val roadmapProgressDao: RoadmapProgressDao
) {
    val userStatsFlow: Flow<UserStats?> = userStatsDao.getUserStatsFlow()
    val completedLessonsFlow: Flow<List<CompletedLesson>> = completedLessonDao.getAllCompletedLessonsFlow()
    val chatMessagesFlow: Flow<List<ChatMessage>> = chatMessageDao.getAllMessagesFlow()
    val roadmapProgressFlow: Flow<List<RoadmapProgress>> = roadmapProgressDao.getAllProgressFlow()

    fun getLessons(): List<Lesson> = StaticData.lessonsList
    fun getRoadmaps(): List<Roadmap> = StaticData.roadmapsList

    suspend fun getOrCreateUserStats(): UserStats {
        val stats = userStatsDao.getUserStats()
        if (stats == null) {
            val defaultStats = UserStats()
            userStatsDao.insertOrUpdateStats(defaultStats)
            return defaultStats
        }
        return stats
    }

    suspend fun completeLesson(lessonId: Int, category: String, xpReward: Int) {
        // 1. Record the completed lesson
        completedLessonDao.insertCompletedLesson(
            CompletedLesson(lessonId = lessonId, category = category)
        )

        // 2. Fetch and update user statistics
        val stats = getOrCreateUserStats()
        
        // Calculate streak
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayStr = sdf.format(Date())
        var newStreak = stats.streak
        var newMaxSaved = stats.maxStreak

        if (stats.lastActiveDate != todayStr) {
            if (stats.lastActiveDate == getYesterdayDateString()) {
                newStreak += 1
            } else {
                // First action or missed days
                newStreak = 1
            }
            if (newStreak > newMaxSaved) {
                newMaxSaved = newStreak
            }
        } else {
            // Already did something today, streak remains unchanged but increment if it was zero
            if (newStreak == 0) newStreak = 1
        }

        // Calculate XP and level
        val newXp = stats.xp + xpReward
        val (newLevel, levelName) = calculateLevelDetails(newXp)

        val updatedStats = stats.copy(
            streak = newStreak,
            maxStreak = newMaxSaved,
            xp = newXp,
            level = newLevel,
            levelName = levelName,
            lastActiveDate = todayStr
        )
        userStatsDao.insertOrUpdateStats(updatedStats)
    }

    suspend fun awardBonusXp(xpReward: Int) {
        val stats = getOrCreateUserStats()
        val newXp = stats.xp + xpReward
        val (newLevel, levelName) = calculateLevelDetails(newXp)
        val updatedStats = stats.copy(
            xp = newXp,
            level = newLevel,
            levelName = levelName
        )
        userStatsDao.insertOrUpdateStats(updatedStats)
    }

    suspend fun skipDayAndSimulateMiss() {
        val stats = getOrCreateUserStats()
        // Force reset streak to 0 or simulate a streak break for demo/testing notifications
        val updatedStats = stats.copy(
            streak = 0,
            lastActiveDate = "2026-05-18" // Set past date
        )
        userStatsDao.insertOrUpdateStats(updatedStats)
    }

    suspend fun togglePremium(isPremium: Boolean) {
        userStatsDao.updatePremiumStatus(isPremium)
    }

    suspend fun incrementReferrals() {
        userStatsDao.incrementReferrals()
        // Premium gift check: 3 referrals unlock premium!
        val stats = getOrCreateUserStats()
        if (stats.referralsCount >= 2 && !stats.isPremium) {
            userStatsDao.updatePremiumStatus(true)
        }
    }

    suspend fun incrementCertificates() {
        userStatsDao.incrementCertificates()
    }

    suspend fun insertChatMessage(senderType: String, content: String) {
        chatMessageDao.insertMessage(
            ChatMessage(senderType = senderType, content = content)
        )
    }

    suspend fun clearChat() {
        chatMessageDao.clearChatHistory()
    }

    suspend fun updateRoadmapProgress(roadmapId: String, day: Int) {
        val progress = roadmapProgressDao.getProgress(roadmapId)
        if (progress == null) {
            roadmapProgressDao.insertProgress(
                RoadmapProgress(roadmapId = roadmapId, currentDay = 1, completedDaysText = day.toString())
            )
        } else {
            val completedList = progress.completedDaysText.split(",").filter { it.isNotEmpty() }.map { it.toInt() }.toMutableSet()
            completedList.add(day)
            val sortedList = completedList.sorted()
            val nextDay = if (sortedList.contains(progress.currentDay) && progress.currentDay == day) {
                progress.currentDay + 1
            } else {
                progress.currentDay
            }
            roadmapProgressDao.insertProgress(
                progress.copy(
                    currentDay = nextDay,
                    completedDaysText = sortedList.joinToString(",")
                )
            )
        }
        
        // Award XP for roadmap day completion
        val stats = getOrCreateUserStats()
        val newXp = stats.xp + 50 // 50 XP bonus for completing structured roadmap task
        val (newLevel, levelName) = calculateLevelDetails(newXp)
        userStatsDao.insertOrUpdateStats(
            stats.copy(xp = newXp, level = newLevel, levelName = levelName)
        )
    }

    private fun getYesterdayDateString(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(cal.time)
    }

    private fun calculateLevelDetails(xp: Int): Pair<Int, String> {
        return when {
            xp < 500 -> Pair(1, "Beginner")
            xp < 1500 -> Pair(2, "Intermediate")
            xp < 3000 -> Pair(3, "Advanced")
            xp < 5000 -> Pair(4, "Expert")
            else -> Pair(5, "Mentor")
        }
    }
}
