package com.dicoding.zodiacs.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.zodiacs.data.ZodiacRepository
import com.dicoding.zodiacs.di.Injection
import com.dicoding.zodiacs.ui.components.ItemZodiac
import com.dicoding.zodiacs.ui.theme.ZodiacsTheme
import com.dicoding.zodiacs.ui.viewmodel.FavoriteViewModel
import com.dicoding.zodiacs.ui.viewmodel.ViewModelFactory

@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepo())),
    navigateToDetail: (Int) -> Unit
) {
    val favoriteZodiacs by favoriteViewModel.favoriteZodiacs.collectAsState()

    Scaffold { paddingValues ->
        if (favoriteZodiacs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF6F4EB)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorites found.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF6F4EB))
            ) {
                items(favoriteZodiacs) { zodiac ->
                    ItemZodiac(
                        itemId = zodiac.id,
                        name = zodiac.name,
                        photoUrl = zodiac.photoUrl,
                        birthdate = zodiac.birthdate,
                        modifier = Modifier.clickable { navigateToDetail(zodiac.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFavoriteScreen() {
    ZodiacsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            FavoriteScreen(
                favoriteViewModel = FavoriteViewModel(ZodiacRepository.getInstance()),
                navigateToDetail = {}
            )
        }
    }
}
