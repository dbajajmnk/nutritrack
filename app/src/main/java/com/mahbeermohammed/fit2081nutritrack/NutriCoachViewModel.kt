package com.mahbeermohammed.fit2081nutritrack.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahbeermohammed.fit2081nutritrack.data.FruitInfo
import com.mahbeermohammed.fit2081nutritrack.data.FruitRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NutriCoachViewModel : ViewModel() {

    private val repository = FruitRepository()

    private val _fruitInfo = MutableStateFlow<FruitInfo?>(null)
    val fruitInfo: StateFlow<FruitInfo?> = _fruitInfo

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _motivation = MutableStateFlow<String?>(null)
    val motivation: StateFlow<String?> = _motivation

    private val generativeModel = GenerativeModel(
        modelName = "models/gemini-2.0-flash",
        apiKey = "AIzaSyBE5fIB1uqV76LD5niUgRgYaGqBBjRa_1Y"
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
                _motivation.value = response.text ?: "Stay strong!"
            } catch (e: Exception) {
                Log.e("GeminiError", "Error generating message", e)
                _motivation.value = "Failed to generate message: ${e.message}"
            }
        }
    }
}
