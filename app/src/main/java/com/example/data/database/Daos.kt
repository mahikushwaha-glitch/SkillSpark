package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    fun getUserStatsFlow(): Flow<UserStats?>

    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    suspend fun getUserStats(): UserStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStats(stats: UserStats)

    @Query("UPDATE user_stats SET isPremium = :premium WHERE id = 1")
    suspend fun updatePremiumStatus(premium: Boolean)

    @Query("UPDATE user_stats SET referralsCount = referralsCount + 1 WHERE id = 1")
    suspend fun incrementReferrals()

    @Query("UPDATE user_stats SET certificatesDownloaded = certificatesDownloaded + 1 WHERE id = 1")
    suspend fun incrementCertificates()
}

@Dao
interface CompletedLessonDao {
    @Query("SELECT * FROM completed_lessons ORDER BY completedAt DESC")
    fun getAllCompletedLessonsFlow(): Flow<List<CompletedLesson>>

    @Query("SELECT * FROM completed_lessons WHERE lessonId = :lessonId LIMIT 1")
    suspend fun getCompletedLesson(lessonId: Int): CompletedLesson?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedLesson(lesson: CompletedLesson)

    @Query("DELETE FROM completed_lessons")
    suspend fun clearCompletedLessons()
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessagesFlow(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatHistory()
}

@Dao
interface RoadmapProgressDao {
    @Query("SELECT * FROM roadmap_progress")
    fun getAllProgressFlow(): Flow<List<RoadmapProgress>>

    @Query("SELECT * FROM roadmap_progress WHERE roadmapId = :roadmapId LIMIT 1")
    suspend fun getProgress(roadmapId: String): RoadmapProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: RoadmapProgress)
}
