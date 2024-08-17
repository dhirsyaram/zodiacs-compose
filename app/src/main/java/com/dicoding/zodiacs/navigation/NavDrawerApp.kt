package com.dicoding.zodiacs.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dicoding.zodiacs.R
import com.dicoding.zodiacs.di.Injection
import com.dicoding.zodiacs.ui.screen.AboutMeScreen
import com.dicoding.zodiacs.ui.screen.DetailScreen
import com.dicoding.zodiacs.ui.screen.FavoriteScreen
import com.dicoding.zodiacs.ui.screen.HomeScreen
import com.dicoding.zodiacs.ui.theme.ZodiacsTheme
import com.dicoding.zodiacs.ui.viewmodel.ViewModelFactory

@Composable
fun NavDrawerApp() {
    val appState = rememberMyNavDrawerState()
    val navController = rememberNavController()
    val isSearchActive = remember { mutableStateOf(false) }
    val searchQuery = remember { mutableStateOf("") }
    val showTopBar = remember { MutableTransitionState(true) }

    BackPressHandler(enabled = appState.drawerState.isOpen) {
        appState.onBackPress()
    }

    val items = listOf(
        MenuItem(
            title = stringResource(R.string.home),
            icon = Icons.Default.Home,
            route = "home"
        ),
        MenuItem(
            title = stringResource(R.string.favourite),
            icon = Icons.Default.Favorite,
            route = "favourite"
        ),
        MenuItem(
            title = stringResource(R.string.profile),
            icon = Icons.Default.AccountCircle,
            route = "profile"
        ),
    )

    val selectedItem = remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState = appState.drawerState,
        gesturesEnabled = appState.drawerState.isOpen,
        drawerContent = {
            Box(
                modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 2)
            ) {
                ModalDrawerSheet {
                    Spacer(Modifier.height(40.dp))
                    items.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.title) },
                            selected = item == selectedItem.value,
                            onClick = {
                                selectedItem.value = item
                                appState.onItemSelected(item)
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                appState.onItemSelected(item)
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedTextColor = Color(0xFFF6F4EB),
                                selectedIconColor = Color(0xFFF6F4EB),
                                selectedContainerColor = Color(0xFF4682A9),
                                unselectedTextColor = Color(0xFF749BC2),
                                unselectedIconColor = Color(0xFF749BC2)
                            )
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(appState.snackbarHostState) },
            topBar = {
                val fadeInAnimation = fadeIn(animationSpec = tween(durationMillis = 200))
                val fadeOutAnimation = fadeOut(animationSpec = tween(durationMillis = 200))
                AnimatedVisibility(
                    visibleState = showTopBar,
                    enter = fadeInAnimation + slideInVertically(initialOffsetY = { -it }),
                    exit = fadeOutAnimation + slideOutVertically(targetOffsetY = { -it })
                ) {
                    MyTopBar(
                        isSearchActive = isSearchActive.value,
                        searchQuery = searchQuery.value,
                        onMenuClick = appState::onMenuClick,
                        onSearchClick = { isSearchActive.value = true },
                        onSearchQueryChange = { searchQuery.value = it },
                        onCloseSearch = {
                            isSearchActive.value = false
                            searchQuery.value = ""
                        },
                        backgroundColor = Color(0xFF4682A9)
                    )
                }
            },
            content = { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = items.first().route,
                    Modifier.padding(paddingValues)
                ) {
                    composable("home") {
                        showTopBar.targetState = true
                        HomeScreen(
                            viewModel = viewModel(factory = ViewModelFactory(Injection.provideRepo())),
                            searchQuery = searchQuery.value,
                            navigateToDetail = { id ->
                                navController.navigate("detail/$id")
                            }
                        )
                    }
                    composable("favourite") {
                        FavoriteScreen(
                            favoriteViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepo())),
                            navigateToDetail = { id ->
                                navController.navigate("detail/$id")
                            }
                        )
                    }
                    composable("profile") {
                        showTopBar.targetState = true
                        AboutMeScreen()
                    }
                    composable("detail/{id}") { backStackEntry ->
                        showTopBar.targetState = false
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        if (id != null) {
                            DetailScreen(
                                zodiacId = id,
                                detailViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepo())),
                                navigateBack = { navController.popBackStack() })
                        }
                    }
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
    onCloseSearch: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF4682A9)
) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        TopAppBar(
            navigationIcon = {
                if (isSearchActive) {
                    IconButton(onClick = onCloseSearch) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.close_search)
                        )
                    }
                } else {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.menu)
                        )
                    }
                }
            },
            title = {
                if (isSearchActive) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text(text = stringResource(R.string.search)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .background(Color.White, shape = RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(R.string.clear_search)
                                    )
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
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                actionIconContentColor = Color.White,
                navigationIconContentColor = Color.White,
                titleContentColor = Color.White
            )
        )
    }
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