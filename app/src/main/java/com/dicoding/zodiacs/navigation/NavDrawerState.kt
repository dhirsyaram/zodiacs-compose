package com.dicoding.zodiacs.navigation

import android.content.Context
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.dicoding.zodiacs.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NavDrawerState (
    val drawerState: DrawerState,
    private val scope: CoroutineScope,
    val snackbarHostState: SnackbarHostState,
    private val context: Context
) {
    private var selectedItem: MenuItem? = null

    fun onMenuClick() {
        scope.launch {
            if (drawerState.isClosed) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }
    }

    fun onItemSelected(item: MenuItem) {
        if (selectedItem != item) {
            selectedItem = item
            scope.launch {
                drawerState.close()
                snackbarHostState.showSnackbar(
                    message = context.resources.getString(R.string.coming_soon, item.title),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    fun onBackPress() {
        if (drawerState.isOpen) {
            scope.launch {
                drawerState.close()
            }
        }
    }
}

@Composable
fun rememberMyNavDrawerState(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    context: Context = LocalContext.current,
): NavDrawerState = remember(drawerState, coroutineScope, snackbarHostState, context) {
    NavDrawerState(drawerState, coroutineScope, snackbarHostState, context)
}