package com.mahbeermohammed.fit2081nutritrack.screens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mahbeermohammed.fit2081nutritrack.data.FruitInfo
import com.mahbeermohammed.fit2081nutritrack.data.FruitRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.mahbeermohammed.fit2081nutritrack.AppDatabase
import com.mahbeermohammed.fit2081nutritrack.MotivationalMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NutriCoachViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FruitRepository()
    private val database = AppDatabase.getDatabase(application)
    private val messageDao = database.motivationalMessageDao()

    private val _fruitInfo = MutableStateFlow<FruitInfo?>(null)
    val fruitInfo: StateFlow<FruitInfo?> = _fruitInfo

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _motivation = MutableStateFlow<String?>(null)
    val motivation: StateFlow<String?> = _motivation

    private val _allTips = MutableStateFlow<List<MotivationalMessage>>(emptyList())
    val allTips: StateFlow<List<MotivationalMessage>> = _allTips

    private val generativeModel = GenerativeModel(
        modelName = "models/gemini-2.0-flash",
        apiKey = ""
    )

    fun fetchFruit(name: String) {
        _loading.value = true
        _error.value = null
        _fruitInfo.value = null

        viewModelScope.launch {
            try {
                val fruit = repository.getFruitInfo(name.lowercase())
                _fruitInfo.value = fruit
            } catch (e: Exception) {
                _error.value = "Fruit not found or error occurred"
            }
            _loading.value = false
        }
    }

    fun generateMotivation() {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat()
                val response = chat.sendMessage("Give me a short motivational message under 15 words.")
                val messageText = response.text ?: "Stay strong!"

                // Save to database
                messageDao.insert(MotivationalMessage(message = messageText))
                _motivation.value = messageText
            } catch (e: Exception) {
                Log.e("GeminiError", "Error generating message", e)
                _motivation.value = "Failed to generate message: ${e.message}"
            }
        }
    }

    fun loadAllTips() {
        viewModelScope.launch {
            try {
                _allTips.value = messageDao.getAllMessages().reversed() // Show newest first
            } catch (e: Exception) {
                Log.e("NutriCoachVM", "Error loading tips", e)
                _allTips.value = emptyList()
            }
        }
    }
}
