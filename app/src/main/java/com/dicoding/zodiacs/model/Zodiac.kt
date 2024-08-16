package com.dicoding.zodiacs.model

data class Zodiac(
    val id: Int,
    val name: String,
    val photoUrl: Int,
    val birthdate: String,
    val description: String,
    val isFavorite: Boolean = false
)
