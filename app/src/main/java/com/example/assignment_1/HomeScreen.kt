package com.example.assignment_1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController



@Composable
fun HomeScreen(navController: NavController) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        CreateAnimation()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MealMate - Plan, Cook, Eat.",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp, // Adjust size as needed
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = "Streamline your meals, save time & money, and eat healthy!",
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Plan your week section
            Text(
                text = "Plan your week in minutes:",
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Carousel of cuisines
            // Video clip of drag-and-drop
            // Image of grocery list
            // Features section
            Text(
                text = "Features you'll love:",
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Feature icons with text
            FeatureRow(
                icon = Icons.Default.Timer,
                text = "30-minute meals: Quick and easy recipes to fit your busy schedule.",
                modifier = Modifier.align(Alignment.Start)
            )
            FeatureRow(
                icon = Icons.Default.AddChart,
                text = "Nutrition Chart: Know what is in what you eat.",
                modifier = Modifier.align(Alignment.Start),
            )
            FeatureRow(
                icon = Icons.Default.AdsClick,
                text = "Diet Planner: Follow a Diet Schedule with just a click.",
                modifier = Modifier.align(Alignment.Start)
            )

            // Get started section
            Text(
                text = "Get started today!",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            MyButton("Follow a Diet!, See what it offers", onClick = {navController.navigate(MEAL_RECIPE_SCREEN)})
            MyButton("Tired?, Plan your Day", onClick = {navController.navigate(DIET_PLANNER)})
        }
    }
    BottomNavigationComponent(navController = navController)
}
