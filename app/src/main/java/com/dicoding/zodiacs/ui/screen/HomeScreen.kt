package com.dicoding.zodiacs.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.zodiacs.di.Injection
import com.dicoding.zodiacs.ui.components.ItemZodiac
import com.dicoding.zodiacs.ui.viewmodel.HomeViewModel
import com.dicoding.zodiacs.ui.viewmodel.ViewModelFactory

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepo())),
    searchQuery: String,
    navigateToDetail: (Int) -> Unit
) {
    val zodiacs by viewModel.zodiacs.collectAsState()
    val query by viewModel.query.collectAsState()

    LaunchedEffect(searchQuery) {
        viewModel.search(searchQuery)
    }

    if (query != searchQuery) {
        viewModel.search(searchQuery)
    }

    when {
        zodiacs.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(zodiacs) { zodiac ->
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
