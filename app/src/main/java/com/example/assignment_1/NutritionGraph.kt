package com.example.assignment_1

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun DailyRecipeScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val argumentValue = navBackStackEntry?.arguments?.getString("argumentKey")

    if (argumentValue != null) {
        // Use argumentValue to display daily recipe
    }
}
@Composable
fun NutritionChartScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val argumentValue = navBackStackEntry?.arguments?.getString("argumentKey")

    if (argumentValue != null) {
        // Use argumentValue to display nutrition chart
    }
}


