package com.example.assignment_1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Text(text = "Settings", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(viewModel.settingsItems) { setting ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                        ) {
                            SettingsItem(setting = setting, onItemClick = {
                                if (setting == "Account") {
                                    // Navigate to the profile screen
                                    navController.navigate(USER_PROFILE_SCREEN)
                                }else if(setting == "Log Out"){
                                    Firebase.auth.signOut()
                                    navController.navigate(LOGIN_SCREEN)
                                }
                                else {
                                    // Navigate to the under development screen
                                    navController.navigate(UNDER_DEV)
                                }
                            })
                        }
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
    BottomNavigationComponent(navController = navController)
}

@Composable
fun SettingsItem(setting: String, onItemClick: () -> Unit) {
    Button(
        onClick = onItemClick,
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = setting,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
        )
    }
}


class SettingsViewModel : ViewModel() {
    val settingsItems = listOf(
        "Account",
        "Notification",
        "Sound",
        "Dark Mode",
        "Language",
        "Theme Color",
        "Font Size",
        "Privacy",
        "Security",
        "Data Usage",
        "Log Out",
        "",
        // Add more settings items as needed
    )
}
@Composable
fun UnderDevelopmentScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "This feature is under development",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
    BottomNavigationComponent(navController = navController)
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController(), viewModel = SettingsViewModel())
}
