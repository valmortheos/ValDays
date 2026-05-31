package com.valdays.journal.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainTab(val title: String, val icon: ImageVector) {
    Notes("Notes", Icons.Filled.List),
    Library("Library", Icons.Filled.Place), // Using placeholder icon for Gallery/Library
    Tasks("Tasks", Icons.Filled.Notifications)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold() {
    var currentTab by remember { mutableStateOf(MainTab.Notes) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentTab.title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        },
        bottomBar = {
            NavigationBar {
                MainTab.values().forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        icon = { Icon(imageVector = tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
                MainTab.Notes -> NotesScreen()
                MainTab.Library -> LibraryScreen()
                MainTab.Tasks -> TasksScreen()
            }
        }
    }
}

// Placeholder screens for compilation
@Composable
fun NotesScreen() {
    Text("Notes Screen")
}

@Composable
fun LibraryScreen() {
    Text("Library Screen")
}

@Composable
fun TasksScreen() {
    Text("Tasks Screen")
}
