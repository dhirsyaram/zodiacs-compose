package com.dicoding.zodiacs.di

import com.dicoding.zodiacs.data.ZodiacRepository

object Injection {

    fun provideRepo(): ZodiacRepository {
        return ZodiacRepository.getInstance()
    }
}