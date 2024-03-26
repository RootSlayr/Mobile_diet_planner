package com.example.assignment_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.assignment_1.ui.theme.Assignment_1Theme
//import androidx.compose.material.Button
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ViewModel
import androidx.compose.material3.BottomAppBar
import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment_1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreenPreview()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Assignment_1Theme {
        Greeting("Android")
    }
}

@Composable
fun LoginScreen(onLoginClick: () -> Unit, onSignUpClick: () -> Unit, onGoogleLoginClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Title: Meal Planner
        Text(
            text = "MealMate",
            fontSize = 32.sp, // Adjust font size as needed
            fontWeight = FontWeight.Bold, // Make text bold
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Subtitle: Get Healthy
        Text(
            text = "Get Healthy Mate",
            fontSize = 18.sp, // Adjust font size as needed
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Username/Email TextField
        TextField(
            value = "", // You need to bind this to a state variable
            onValueChange = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text("Username/Email") }
        )

        // Password TextField
        TextField(
            value = "", // You need to bind this to a state variable
            onValueChange = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
        )

        // Sign In Button
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Sign In")
        }

        // Sign Up Button
        Button(
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }
        GoogleSignInButton(onGoogleLoginClick)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Assignment_1Theme {
        LoginScreen(onLoginClick = {}, //empty lambda for now,
                    onSignUpClick = {}, // Empty lambda for now
                    onGoogleLoginClick = {})
    }
}

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    val googleLogo: Painter = painterResource(id = R.drawable.google_logo) // Replace R.drawable.google_logo with your Google logo resource

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = googleLogo,
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp) // Adjust size as needed
            )
            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between the logo and text
            Text("Sign In with Google")
        }
    }
}
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    data object Search : BottomNavItem("search", Icons.Default.Search, "Search")
    data object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}

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
            Text(text = "Welcome to MealMate, mate!")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("mealList") }) {
                Text(text = "View Meals")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("shoppingList") }) {
                Text(text = "View Shopping List")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("settings") }) {
                Text(text = "Settings")
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            // add your column here (with align modifier)
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                BottomNavigation(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colorScheme.surface
                ) {
                    BottomNavigationItem(
                        selected = navController.currentDestination?.route == "mealList",
                        onClick = { navController.navigate("mealList") },
                        icon = { Icon(Icons.Default.Search, contentDescription = "Meals") },
                        label = { Text("Meals") }
                    )
                    BottomNavigationItem(
                        selected = navController.currentDestination?.route == "shoppingList",
                        onClick = { navController.navigate("shoppingList") },
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping List") },
                        label = { Text("Shopping") }
                    )
                    BottomNavigationItem(
                        selected = navController.currentDestination?.route == "settings",
                        onClick = { navController.navigate("settings") },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") }
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    val navController = rememberNavController()
    HomeScreen(navController)
}

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Settings", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(viewModel.settingsItems) {setting ->
                    SettingsItem(setting = setting)
                    Divider()
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /* Handle save button click */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun SettingsItem(setting: String) {
    Text(
        text = setting,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    )
}
class SettingsViewModel : ViewModel() {
    val settingsItems = listOf(
        "Notification",
        "Sound",
        "Dark Mode",
        // Add more settings items as needed
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController(), viewModel = SettingsViewModel())
}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreenProto() {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "MealMate",
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                },
//                actions = {
//                    IconButton(onClick = { /* Navigate to Settings Screen */ }) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_settings),
//                            contentDescription = "Settings"
//                        )
//                    }
//                    IconButton(onClick = { /* Navigate to Recipe Search Screen */ }) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_search),
//                            contentDescription = "Search"
//                        )
//                    }
//                }
//            )
//        },
//        bottomBar = {
//            BottomAppBar(
//                backgroundColor = MaterialTheme.colorScheme.surface,
//                content = {
//                    BottomNavigation {
//                        // Navigation icons go here
//                    }
//                }
//            )
//        },
//        content = {it
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                // Meal Planning Section
//                Card(
//                    elevation = 4.dp,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//                        Text(
//                            text = "This Week's Meals",
//                            style = MaterialTheme.typography.bodySmall
//                        )
//                        LazyColumn {
//                            items(listOf("Meal 1", "Meal 2", "Meal 3")) { meal ->
//                                MealCard(meal = meal)
//                            }
//                        }
//                    }
//                }
//
//                // Quick Actions Section
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//                    Button(
//                        onClick = { /* Open Diet Selector Screen */ }
//                    ) {
//                        Text(text = "Diet Selector")
//                    }
//                    Button(
//                        onClick = { /* Open Calorie Tracker Screen */ }
//                    ) {
//                        Text(text = "Calorie Tracker")
//                    }
//                }
//
//                // Nutrition Breakdown Section
//                Card(
//                    elevation = 4.dp,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Nutrition at a Glance",
//                            style = MaterialTheme.typography.h6
//                        )
//                        // Pie Chart and text summary go here
//                    }
//                }
//            }
//        }
//    )
//}
//
//@Composable
//fun MealCard(meal: String) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.placeholder),
//            contentDescription = null,
//            modifier = Modifier.size(50.dp)
//        )
//        Column {
//            Text(text = meal, style = MaterialTheme.typography.bodySmall)
//            Text(text = "Monday", style = MaterialTheme.typography.bodyMedium)
//        }
//    }
//}
//
//@Preview
//@Composable
//fun HomeScreenProtoPreview() {
//    HomeScreen()
//}
