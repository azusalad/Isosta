package com.example.isosta.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.isosta.ui.screens.HomeScreen
import com.example.isosta.ui.screens.IsostaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

// Contains composables that render what the user would see in the Isosta App

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsostaApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { IsostaTopAppBar(scrollBehavior = scrollBehavior) }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val isostaViewModel: IsostaViewModel = viewModel(factory = IsostaViewModel.Factory)
            HomeScreen(
                isostaUiState = isostaViewModel.isostaUiState,
                contentPadding = it,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsostaTopAppBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "Isosta Home Screen",
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        modifier = modifier
    )
}
