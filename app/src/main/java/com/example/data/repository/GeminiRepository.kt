package com.example.data.repository

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val mediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun getCoachResponse(userMessage: String, chatHistory: List<Pair<String, String>>): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Hi there! I am Sparky, your AI Learning Coach. 🚀 To enable real-time replies from me, please enter your Google Gemini API Key in the AI Studio Secrets panel!"
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        try {
            val root = JSONObject()

            // Build historical contents
            val contentsArray = JSONArray()

            // 1. Add chat history
            for ((sender, text) in chatHistory) {
                val role = if (sender == "user") "user" else "model"
                val textObj = JSONObject().put("text", text)
                val partsArray = JSONArray().put(textObj)
                val contentObj = JSONObject()
                    .put("role", role)
                    .put("parts", partsArray)
                contentsArray.put(contentObj)
            }

            // 2. Add current message
            val currentParts = JSONArray().put(JSONObject().put("text", userMessage))
            val currentContent = JSONObject()
                .put("role", "user")
                .put("parts", currentParts)
            contentsArray.put(currentContent)

            root.put("contents", contentsArray)

            // 3. Add prompt system instructions
            val systemInstructionText = """
                You are Sparky, an addictive, friendly, and hyper-gamified 5-minute career learning coach. 
                Your purpose is to answer learning questions about corporate and tech skills, Excel formulas, coding, resume writing, communication, and finance.
                Keep answers extremely micro, simple, and action-oriented (max 2-3 short, punchy paragraphs with clear bold titles or bullet points).
                Use gamified terminology! (e.g. "XP unlocked!", "Double streak booster!", "Level Up Tip!").
                Avoid long paragraphs or boring textbook lectures. Everything must be bite-sized.
            """.trimIndent()

            val sysInstructionObj = JSONObject()
                .put("parts", JSONArray().put(JSONObject().put("text", systemInstructionText)))
            root.put("systemInstruction", sysInstructionObj)

            // 4. Add generative configuration for optimal response
            val genConfig = JSONObject()
                .put("temperature", 0.7)
                .put("maxOutputTokens", 500)
            root.put("generationConfig", genConfig)

            val requestBody = root.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string() ?: ""
                    Log.e("GeminiRepository", "API Error: Code ${response.code}, Detail: $errorBody")
                    return@withContext "Whoops! Sparky hit a small network glitch (Error ${response.code}). Let's try again in a second!"
                }

                val responseBody = response.body?.string()
                if (responseBody.isNullOrEmpty()) {
                    return@withContext "Sparky is thinking silently... Try asking again!"
                }

                val responseJson = JSONObject(responseBody)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val contentObj = firstCandidate.optJSONObject("content")
                    if (contentObj != null) {
                        val parts = contentObj.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            return@withContext parts.getJSONObject(0).optString("text", "No text response.")
                        }
                    }
                }
                return@withContext "Sparky couldn't process that text format. Ask me another skill question!"
            }
        } catch (e: Exception) {
            Log.e("GeminiRepository", "Exception calling Gemini API", e)
            return@withContext "Network delay! Check your internet connection and verify your Gemini API key in secrets."
        }
    }
}
