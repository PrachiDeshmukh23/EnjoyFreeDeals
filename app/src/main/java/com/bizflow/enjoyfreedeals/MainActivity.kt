package com.bizflow.enjoyfreedeals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bizflow.enjoyfreedeals.navigation.EnjoyFreeDealsNavGraph
import com.bizflow.enjoyfreedeals.navigation.EnjoyScaffold
import com.bizflow.enjoyfreedeals.theme.EnjoyFreeDealsTheme
import com.bizflow.enjoyfreedeals.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AppViewModel = viewModel()
            val state by viewModel.uiState.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }
            EnjoyFreeDealsTheme(darkTheme = state.darkMode) {
                EnjoyScaffold(snackbarHostState) {
                    EnjoyFreeDealsNavGraph(state, viewModel, snackbarHostState)
                }
            }
        }
    }
}
