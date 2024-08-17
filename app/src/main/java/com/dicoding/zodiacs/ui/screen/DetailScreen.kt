package com.dicoding.zodiacs.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.zodiacs.di.Injection
import com.dicoding.zodiacs.model.Zodiac
import com.dicoding.zodiacs.ui.common.UiState
import com.dicoding.zodiacs.ui.theme.ZodiacsTheme
import com.dicoding.zodiacs.ui.viewmodel.DetailViewModel
import com.dicoding.zodiacs.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    zodiacId: Int,
    detailViewModel: DetailViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepo())),
    navigateBack: () -> Unit
) {

    val uiState by detailViewModel.uiState.collectAsState()

    LaunchedEffect(zodiacId) {
        detailViewModel.getDetailZodiacById(zodiacId)
    }

    when (uiState) {
        is UiState.Loading -> {
            CircularProgressIndicator()
        }

        is UiState.Success -> {
            val zodiac = (uiState as UiState.Success<Zodiac>).data

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Detail") },
                        navigationIcon = {
                            IconButton(onClick = navigateBack) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF4682A9),
                            titleContentColor = Color.White,
                            actionIconContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        )
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            detailViewModel.updateZodiac(zodiac.id, zodiac.isFavorite)
                        },
                        containerColor = Color(0xFF91C8E4)
                    ) {
                        Icon(
                            imageVector = if (zodiac.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Toggle Favorite",
                            tint = Color.White
                        )
                    }
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFFF6F4EB))
                        .verticalScroll(rememberScrollState())
                ) {
                    Image(
                        painter = painterResource(id = zodiac.photoUrl),
                        contentDescription = zodiac.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = zodiac.name,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(start = 14.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = zodiac.birthdate,
                        style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                        modifier = Modifier.padding(start = 14.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = zodiac.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 14.dp, end = 68.dp, bottom = 30.dp)
                    )
                }
            }
        }

        is UiState.Error -> {
            Text(
                text = "Error loading data",
                modifier = Modifier.fillMaxSize(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetail() {
    ZodiacsTheme {
        DetailScreen(zodiacId = 1, navigateBack = {})
    }
}