package com.example.hotbedagrocontrolapp.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.domain.viewModel.statistics.StatisticsViewModel
import kotlinx.coroutines.launch

/**
 * Доступные странички в бургер-меню.
 */
enum class Screens(val title: String) {
    ELEMENTS("Показатели"), STATISTICS("Статистика"),
    EVENT_LOG("Журнал событий"), DEVICES("Управление устройством"),
    MQTT_SETTINGS("Параметры конфигурации"), OPTIMAL_VALUES("Оптимальные значения датчиков")
}

/**
 * Основной экран приложения.
 */
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    agroControlViewModel: AgroControlViewModel = hiltViewModel(),
    statisticsViewModel: StatisticsViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    var selectedScreen by remember { mutableStateOf(Screens.ELEMENTS) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.width(320.dp)
            ) {
                listOf(Screens.ELEMENTS, Screens.STATISTICS, Screens.EVENT_LOG, Screens.DEVICES).forEach { screen ->
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
                            selectedContainerColor = MaterialTheme.colorScheme.background
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
                    title = {
                        Text(
                            text = "HotBed Agro Control App",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = "Меню")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->

            val isDeviceAdded by agroControlViewModel.isDeviceAdded.collectAsState()

            NavHost(navController = navController, startDestination = Screens.ELEMENTS.title) {
                composable(Screens.ELEMENTS.title) {
                    if (isDeviceAdded) {
                        ElementsScreen(
                            viewModel = agroControlViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        Text(
                            text = "Нет подключенных устройств. Для добавления устройства перейдите во вкладку \"Устройства\".",
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = modifier.padding(innerPadding).padding(20.dp)
                        )
                    }
                }

                composable(Screens.STATISTICS.title) {
                    if (isDeviceAdded) {
                        StatisticsGraphScreen(
                            viewModel = statisticsViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        Text(
                            text = "Нет подключенных устройств. Для добавления устройства перейдите во вкладку \"Устройства\".",
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = modifier.padding(innerPadding).padding(20.dp)
                        )
                    }
                }

                composable(Screens.EVENT_LOG.title) {
                    if (isDeviceAdded) {
                        EventLogScreen(
                            viewModel = statisticsViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        Text(
                            text = "Нет подключенных устройств. Для добавления устройства перейдите во вкладку \"Устройства\".",
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = modifier.padding(innerPadding).padding(20.dp)
                        )
                    }
                }

                composable(Screens.DEVICES.title) {
                    DevicesScreen(
                        viewModel = agroControlViewModel,
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                composable(Screens.MQTT_SETTINGS.title) {
                    MqttSettingsScreen(
                        viewModel = agroControlViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                composable(Screens.OPTIMAL_VALUES.title) {
                    OptimalValuesScreen(
                        viewModel = agroControlViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}