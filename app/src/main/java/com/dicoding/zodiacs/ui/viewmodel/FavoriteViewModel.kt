package com.dicoding.zodiacs.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.zodiacs.data.ZodiacRepository
import com.dicoding.zodiacs.model.Zodiac
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repo: ZodiacRepository) : ViewModel() {

    private val _favoriteZodiacs = MutableStateFlow<List<Zodiac>>(emptyList())
    val favoriteZodiacs: StateFlow<List<Zodiac>> = _favoriteZodiacs.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getFavoriteZodiacs()
                .collect { zodiacs ->
                    _favoriteZodiacs.value = zodiacs
                }
        }
    }

    fun toggleFavorite(zodiacId: Int) {
        viewModelScope.launch {
            val currentZodiac = _favoriteZodiacs.value.find { it.id == zodiacId }
            if (currentZodiac != null) {
                val newState = !currentZodiac.isFavorite
                repo.updateData(zodiacId, newState).collect {
                    val updatedList = _favoriteZodiacs.value.map {
                        if (it.id == zodiacId) it.copy(isFavorite = newState) else it
                    }
                    _favoriteZodiacs.value = updatedList
                }
            }
        }
    }
}
