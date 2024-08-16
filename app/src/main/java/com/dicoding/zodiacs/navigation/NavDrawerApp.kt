package com.dicoding.zodiacs.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.zodiacs.R
import com.dicoding.zodiacs.ui.theme.ZodiacsTheme

@Composable
fun NavDrawerApp() {
    val appState = rememberMyNavDrawerState()
    val isSearchActive = remember { mutableStateOf(false) }
    val searchQuery = remember { mutableStateOf("") }


    BackPressHandler(enabled = appState.drawerState.isOpen) {
        appState.onBackPress()
    }

    val items = listOf(
        MenuItem(
            title = stringResource(R.string.home),
            icon = Icons.Default.Home
        ),
        MenuItem(
            title = stringResource(R.string.favourite),
            icon = Icons.Default.Favorite
        ),
        MenuItem(
            title = stringResource(R.string.profile),
            icon = Icons.Default.AccountCircle
        ),
    )

    val selectedItem = remember { mutableStateOf(items[0]) }

    Scaffold(
        snackbarHost = { SnackbarHost(appState.snackbarHostState) },
        topBar = {
            MyTopBar(
                isSearchActive = isSearchActive.value,
                searchQuery = searchQuery.value,
                onMenuClick = appState::onMenuClick,
                onSearchClick = { isSearchActive.value = true },
                onSearchQueryChange = { searchQuery.value = it },
                onCloseSearch = {
                    isSearchActive.value = false
                    searchQuery.value = ""
                }
            )
        },
    ) { paddingValues ->
        ModalNavigationDrawer(
            modifier = Modifier.padding(paddingValues),
            drawerState = appState.drawerState,
            gesturesEnabled = appState.drawerState.isOpen,
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(Modifier.height(12.dp))
                    items.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.title) },
                            selected = item == selectedItem.value,
                            onClick = {
                                appState.onItemSelected(item)
                                selectedItem.value = item
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }
            },
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        if (appState.drawerState.isClosed) {
                            stringResource(R.string.swipe_to_open)
                        } else {
                            stringResource(R.string.swipe_to_close)
                        }
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    isSearchActive: Boolean,
    searchQuery: String,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCloseSearch: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            if (isSearchActive) {
                IconButton(onClick = onCloseSearch) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(
                        R.string.close_search
                    ))
                }
            } else {
                IconButton(onClick = onMenuClick) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = stringResource(R.string.menu))
                }
            }
        },
        title = {
            if (isSearchActive) {
                androidx.compose.material3.TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text(text = stringResource(R.string.search)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(
                                    R.string.clear_search
                                ))
                            }
                        }
                    }
                )
            } else {
                Text(text = stringResource(R.string.app_name))
            }
        },
        actions = {
            if (!isSearchActive) {
                IconButton(onClick = onSearchClick) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(R.string.search))
                }
            }
        }
    )
}

@Composable
fun BackPressHandler(enabled: Boolean = true, onBackPressed: () -> Unit) {
    val currentOnBackPressed by rememberUpdatedState(onBackPressed)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    SideEffect {
        backCallback.isEnabled = enabled
    }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ZodiacsTheme {
        NavDrawerApp()
    }
}