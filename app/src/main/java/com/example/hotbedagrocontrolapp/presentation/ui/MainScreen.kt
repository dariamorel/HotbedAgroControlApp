package com.example.hotbedagrocontrolapp.presentation.ui

import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hotbedagrocontrolapp.presentation.ui.components.StatisticsScreen
import com.example.hotbedagrocontrolapp.presentation.viewModel.AgroControlViewModel
import kotlinx.coroutines.launch

enum class Screens(val title: String) {
    ELEMENTS("Показатели"), STATISTICS("Статистика"),
    EVENT_LOG("Журнал событий"), DEVICES("Устройства"),
    SETTINGS("Настройки")
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    agroControlViewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    var selectedScreen by remember { mutableStateOf(Screens.ELEMENTS) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background
            ) {
                Spacer(Modifier.padding(8.dp))
                Screens.entries.forEach { screen ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = screen.title,
                                style = MaterialTheme.typography.titleMedium
                            ) },
                        selected = screen == selectedScreen,
                        onClick = {
                            selectedScreen = screen
                            navController.navigate(screen.title) },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = ShapeDefaults.Small
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = "Меню")
                        }
                    },
                )
            },
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->

            NavHost(navController = navController, startDestination = Screens.ELEMENTS.title) {
                composable(Screens.ELEMENTS.title) {
                    ElementsScreen(
                        viewModel = agroControlViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                composable(Screens.STATISTICS.title) {
                    StatisticsScreen(
                        viewModel = agroControlViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}