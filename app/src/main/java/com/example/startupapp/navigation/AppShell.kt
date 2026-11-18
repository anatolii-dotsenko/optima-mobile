package com.example.startupapp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.startupapp.LoginScreen
import com.example.startupapp.StartupApp
import com.example.startupapp.ui.content.ContentScreen
import kotlinx.coroutines.launch

private const val ROUTE_LOGIN = "login"
private const val ROUTE_CONTENT = "content"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppShell(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ROUTE_LOGIN

    val app = LocalContext.current.applicationContext as StartupApp
    val registeredEmail by app.sessionRepository.registeredUserEmail.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navigateTo: (String) -> Unit = { route ->
        scope.launch { drawerState.close() }
        navController.navigateSingleTopTo(route)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = modifier,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
                    Text(
                        text = "StartupApp",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = registeredEmail ?: "Гість (не зареєстровано)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                NavigationDrawerItem(
                    label = { Text("Головна (Реєстрація)") },
                    selected = currentRoute == ROUTE_LOGIN,
                    onClick = { navigateTo(ROUTE_LOGIN) },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Інформаційна сторінка") },
                    selected = currentRoute == ROUTE_CONTENT,
                    onClick = { navigateTo(ROUTE_CONTENT) },
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (currentRoute == ROUTE_CONTENT) {
                                "Інформаційна сторінка"
                            } else {
                                "Перевірка реєстрації"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Відкрити меню"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = ROUTE_LOGIN,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(ROUTE_LOGIN) {
                    LoginScreen(
                        onNavigateToContent = {
                            navController.navigateSingleTopTo(ROUTE_CONTENT)
                        }
                    )
                }
                composable(ROUTE_CONTENT) {
                    ContentScreen(
                        onNavigateToLogin = {
                            navController.navigateSingleTopTo(ROUTE_LOGIN)
                        }
                    )
                }
            }
        }
    }
}

private fun NavHostController.navigateSingleTopTo(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
