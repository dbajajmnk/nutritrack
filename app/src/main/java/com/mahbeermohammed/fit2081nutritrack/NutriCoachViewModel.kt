package com.mahbeermohammed.fit2081nutritrack.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahbeermohammed.fit2081nutritrack.data.FruitInfo
import com.mahbeermohammed.fit2081nutritrack.data.FruitRepository
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
}
