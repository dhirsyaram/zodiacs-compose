package com.dicoding.zodiacs.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.zodiacs.data.ZodiacRepository
import com.dicoding.zodiacs.model.Zodiac
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: ZodiacRepository) : ViewModel() {

    private val _zodiacs = MutableStateFlow<List<Zodiac>>(emptyList())
    val zodiacs: StateFlow<List<Zodiac>> = _zodiacs

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    init {
        loadZodiacs()
    }

    private fun loadZodiacs() {
        viewModelScope.launch {
            repo.getAllZodiacs().collect { data ->
                _zodiacs.value = data
            }
        }
    }

    fun search(newQuery: String) {
        _query.value = newQuery
        viewModelScope.launch {
            repo.searchItem(newQuery).collect { data ->
                _zodiacs.value = data
            }
        }
    }
}
