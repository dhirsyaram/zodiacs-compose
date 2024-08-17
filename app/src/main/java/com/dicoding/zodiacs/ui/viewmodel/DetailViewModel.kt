package com.dicoding.zodiacs.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.zodiacs.data.ZodiacRepository
import com.dicoding.zodiacs.model.Zodiac
import com.dicoding.zodiacs.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: ZodiacRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Zodiac>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Zodiac>> get() = _uiState

    fun getDetailZodiacById(id: Int) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        _uiState.value = UiState.Success(repo.getZodiacById(id))
    }

    fun updateZodiac(id: Int, newState: Boolean) = viewModelScope.launch {
        repo.updateData(id, !newState)
            .collect { isUpdated ->
                if (isUpdated) getDetailZodiacById(id)
            }
    }
}