package com.dicoding.zodiacs.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.zodiacs.data.ZodiacRepository
import com.dicoding.zodiacs.di.Injection
import com.dicoding.zodiacs.ui.components.ItemZodiac
import com.dicoding.zodiacs.ui.theme.ZodiacsTheme
import com.dicoding.zodiacs.ui.viewmodel.FavoriteViewModel
import com.dicoding.zodiacs.ui.viewmodel.ViewModelFactory

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FavoriteScreen(
//    favoriteViewModel: FavoriteViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepo())),
//    navigateToDetail: (Int) -> Unit
//) {
//    val favoriteZodiacs by favoriteViewModel.favoriteZodiacs.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("Favorite Zodiacs") })
//        }
//    ) { paddingValues ->
//        LazyColumn(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//        ) {
//            items(favoriteZodiacs) { zodiac ->
//                ItemZodiac(
//                    zodiac = zodiac,
//                    navigateToDetail = navigateToDetail
//                )
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewFavoriteScreen() {
//    ZodiacsTheme {
//        Surface(modifier = Modifier.fillMaxSize()) {
//            FavoriteScreen(
//                favoriteViewModel = FavoriteViewModel(ZodiacRepository.getInstance()),
//                navigateToDetail = {}
//            )
//        }
//    }
//}
