package com.example.assignment_1

import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier

@Composable
fun BottomNavigationComponent(navController: NavController) {
    Surface(
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
    ) {
        BottomNavigation(
//            modifier = Modifier.align(Alignment.BottomCenter),
            backgroundColor = MaterialTheme.colorScheme.surface
        ) {
            BottomNavigationItem(
                selected = navController.currentDestination?.route == "mealList",
                onClick = { navController.navigate(DIET_PLANNER) },
                icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Diet Planner") },
                label = { Text("Planner") }
            )
            BottomNavigationItem(
                selected = navController.currentDestination?.route == "mealList",
                onClick = { navController.navigate(MEAL_RECIPE_SCREEN) },
                icon = { Icon(Icons.Default.LocalPizza, contentDescription = "Meals") },
                label = { Text("Meals") }
            )
            BottomNavigationItem(
                selected = navController.currentDestination?.route == "homePage",
                onClick = { navController.navigate(HOME_SCREEN) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home Page") },
                label = { Text("Home") }
            )
            BottomNavigationItem(
                selected = navController.currentDestination?.route == "settings",
                onClick = { navController.navigate(SETTINGS_SCREEN) },
                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                label = { Text("Settings") }
            )
        }
    }
}

