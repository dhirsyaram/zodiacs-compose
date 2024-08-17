package com.dicoding.zodiacs.data

import com.dicoding.zodiacs.model.Zodiac
import com.dicoding.zodiacs.model.ZodiacsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class ZodiacRepository {

    private val zodiacs = mutableListOf<Zodiac>()

    init {
        if (zodiacs.isEmpty()) {
            ZodiacsData.zodiacs.forEach {
                zodiacs.add(it)
            }
        }
    }

    fun getAllZodiacs(): Flow<List<Zodiac>> {
        return flowOf(zodiacs)
    }

    fun getZodiacById(zodiacsId: Int): Zodiac {
        return zodiacs.first { it.id == zodiacsId }
    }

    fun getFavoriteZodiacs(): Flow<List<Zodiac>> {
        return flowOf(zodiacs.filter { it.isFavorite })
    }

    fun searchItem(q: String) = flow {
        val data = zodiacs.filter {
            it.name.contains(q, ignoreCase = true)
        }
        emit(data)
    }

    fun updateData(zodiacsId: Int, newState: Boolean): Flow<Boolean> {
        val index = zodiacs.indexOfFirst { it.id == zodiacsId }
        val result = if (index >= 0) {
            val zodiac = zodiacs[index]
            zodiacs[index] = zodiac.copy(isFavorite = newState)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    companion object {
        @Volatile
        private var instance: ZodiacRepository? = null

        fun getInstance(): ZodiacRepository =
            instance ?: synchronized(this) {
                ZodiacRepository().apply {
                    instance = this
                }
            }
    }
}