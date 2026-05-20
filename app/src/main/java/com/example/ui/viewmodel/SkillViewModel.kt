package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.ChatMessage
import com.example.data.database.UserStats
import com.example.data.database.RoadmapProgress
import com.example.data.model.Lesson
import com.example.data.model.Roadmap
import com.example.data.repository.GeminiRepository
import com.example.data.repository.SkillRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SkillViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val skillRepo = SkillRepository(
        db.userStatsDao(),
        db.completedLessonDao(),
        db.chatMessageDao(),
        db.roadmapProgressDao()
    )
    private val geminiRepo = GeminiRepository()

    // Database Flows
    val userStatsState: StateFlow<UserStats> = skillRepo.userStatsFlow
        .map { it ?: UserStats() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserStats())

    val completedLessonsState = skillRepo.completedLessonsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessagesState = skillRepo.chatMessagesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val roadmapProgressState = skillRepo.roadmapProgressFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Lesson Playing State
    private val _currentLesson = MutableStateFlow<Lesson?>(null)
    val currentLesson: StateFlow<Lesson?> = _currentLesson.asStateFlow()

    private val _activeSlideIndex = MutableStateFlow(0)
    val activeSlideIndex: StateFlow<Int> = _activeSlideIndex.asStateFlow()

    private val _isInQuizMode = MutableStateFlow(false)
    val isInQuizMode: StateFlow<Boolean> = _isInQuizMode.asStateFlow()

    private val _activeQuestionIndex = MutableStateFlow(0)
    val activeQuestionIndex: StateFlow<Int> = _activeQuestionIndex.asStateFlow()

    private val _selectedOptionIndex = MutableStateFlow<Int?>(null)
    val selectedOptionIndex: StateFlow<Int?> = _selectedOptionIndex.asStateFlow()

    private val _showQuizFeedback = MutableStateFlow(false)
    val showQuizFeedback: StateFlow<Boolean> = _showQuizFeedback.asStateFlow()

    private val _isCorrectOption = MutableStateFlow(false)
    val isCorrectOption: StateFlow<Boolean> = _isCorrectOption.asStateFlow()

    private val _isLessonFinished = MutableStateFlow(false)
    val isLessonFinished: StateFlow<Boolean> = _isLessonFinished.asStateFlow()

    private val _unlockedBadge = MutableStateFlow<String?>(null)
    val unlockedBadge: StateFlow<String?> = _unlockedBadge.asStateFlow()

    // Coach Assistant Loading State
    private val _isCoachResponding = MutableStateFlow(false)
    val isCoachResponding: StateFlow<Boolean> = _isCoachResponding.asStateFlow()

    init {
        // Initialize stats if empty
        viewModelScope.launch {
            skillRepo.getOrCreateUserStats()
        }
    }

    // Static Data
    fun getLessons(): List<Lesson> = skillRepo.getLessons()
    fun getRoadmaps(): List<Roadmap> = skillRepo.getRoadmaps()

    // Lesson Control Actions
    fun startLesson(lesson: Lesson) {
        _currentLesson.value = lesson
        _activeSlideIndex.value = 0
        _isInQuizMode.value = false
        _activeQuestionIndex.value = 0
        _selectedOptionIndex.value = null
        _showQuizFeedback.value = false
        _isLessonFinished.value = false
        _unlockedBadge.value = null
    }

    fun nextSlide() {
        val lesson = _currentLesson.value ?: return
        val nextIndex = _activeSlideIndex.value + 1
        if (nextIndex < lesson.slides.size) {
            _activeSlideIndex.value = nextIndex
        } else {
            // Enter Quiz Mode
            if (lesson.quizQuestions.isNotEmpty()) {
                _isInQuizMode.value = true
                _activeQuestionIndex.value = 0
                _selectedOptionIndex.value = null
                _showQuizFeedback.value = false
            } else {
                finishActiveLesson()
            }
        }
    }

    fun selectQuizOption(optionIndex: Int) {
        if (_showQuizFeedback.value) return // Block change once submitted
        val lesson = _currentLesson.value ?: return
        val question = lesson.quizQuestions.getOrNull(_activeQuestionIndex.value) ?: return

        _selectedOptionIndex.value = optionIndex
        _isCorrectOption.value = optionIndex == question.correctAnswerIndex
        _showQuizFeedback.value = true
    }

    fun nextQuizQuestion() {
        val lesson = _currentLesson.value ?: return
        val nextIndex = _activeQuestionIndex.value + 1
        if (nextIndex < lesson.quizQuestions.size) {
            _activeQuestionIndex.value = nextIndex
            _selectedOptionIndex.value = null
            _showQuizFeedback.value = false
        } else {
            finishActiveLesson()
        }
    }

    private fun finishActiveLesson() {
        val lesson = _currentLesson.value ?: return
        viewModelScope.launch {
            // Save inside local Room database
            skillRepo.completeLesson(lesson.id, lesson.category, lesson.xpReward)
            _unlockedBadge.value = lesson.badgeUnlocked
            _isLessonFinished.value = true
        }
    }

    fun exitLesson() {
        _currentLesson.value = null
        _isLessonFinished.value = false
    }

    // AI Coach Chat Actions
    fun sendChatMessage(messageStr: String) {
        if (messageStr.trim().isEmpty()) return
        viewModelScope.launch {
            // 1. Insert user message in database
            skillRepo.insertChatMessage("user", messageStr)

            // 2. Load historical dialog context
            _isCoachResponding.value = true
            val currentMessages = chatMessagesState.value
            val history = currentMessages.takeLast(10).map { Pair(it.senderType, it.content) }

            // 3. Request Gemini reply
            val reply = geminiRepo.getCoachResponse(messageStr, history)

            // 4. Save coach reply in database
            skillRepo.insertChatMessage("assistant", reply)
            _isCoachResponding.value = false
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            skillRepo.clearChat()
        }
    }

    // Roadmap Progress Actions
    fun completeRoadmapTask(roadmapId: String, day: Int) {
        viewModelScope.launch {
            skillRepo.updateRoadmapProgress(roadmapId, day)
        }
    }

    // Monetization Store / Referrals Actions
    fun simulatedPurchasePremium() {
        viewModelScope.launch {
            skillRepo.togglePremium(true)
        }
    }

    fun simulatedCancelPremium() {
        viewModelScope.launch {
            skillRepo.togglePremium(false)
        }
    }

    fun referFriend() {
        viewModelScope.launch {
            skillRepo.incrementReferrals()
        }
    }

    fun downloadCertificate() {
        viewModelScope.launch {
            skillRepo.incrementCertificates()
        }
    }

    // Testing and debug tools
    fun resetUserProgress() {
        viewModelScope.launch {
            // Reset DB tables
            db.completedLessonDao().clearCompletedLessons()
            db.chatMessageDao().clearChatHistory()
            // Reset stats to defaults
            val defaultStats = UserStats()
            db.userStatsDao().insertOrUpdateStats(defaultStats)
            // Empty roadmap progress Table
            db.clearAllTables()
            db.userStatsDao().insertOrUpdateStats(defaultStats)
        }
    }

    fun simulateStreakBreak() {
        viewModelScope.launch {
            skillRepo.skipDayAndSimulateMiss()
        }
    }

    fun awardBonusXp(xpReward: Int) {
        viewModelScope.launch {
            skillRepo.awardBonusXp(xpReward)
        }
    }
}
