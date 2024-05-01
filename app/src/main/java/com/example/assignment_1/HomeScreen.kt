package com.example.assignment_1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Phonelink
import androidx.compose.material.icons.filled.RestaurantMenu
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
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Plan your week section
            Text(
                text = "Plan your week in minutes:",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Carousel of cuisines
            // Video clip of drag-and-drop
            // Image of grocery list
            // Features section
            Text(
                text = "Features you'll love:",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Feature icons with text
            FeatureRow(
                icon = Icons.Default.Timer,
                text = "30-minute meals: Quick and easy recipes to fit your busy schedule."
            )
            FeatureRow(
                icon = Icons.Default.Filter,
                text = "Dietary filters: Find meals that meet your specific needs."
            )
            FeatureRow(
                icon = Icons.Default.RestaurantMenu,
                text = "Leftover magic: Repurpose ingredients to avoid food waste."
            )
            FeatureRow(
                icon = Icons.Default.FamilyRestroom,
                text = "Family-friendly options: Keep everyone happy with kid-approved meals."
            )
            FeatureRow(
                icon = Icons.Default.Phonelink,
                text = "Offline access: Take your meal plan anywhere."
            )

            // Get started section
            Text(
                text = "Get started today!",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            MyButton("View Diet Options", {})
            MyButton("View Meal Plans", {})
            MyButton("View Nutrition Chart", {})
            BottomNavigationComponent(navController = navController)
        }
    }
}
