package com.example.assignment_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.assignment_1.ui.theme.Assignment_1Theme
//import androidx.compose.material.Button
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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
                    LoginScreenPreview()
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
            text = "Meal Planner",
            fontSize = 32.sp, // Adjust font size as needed
            fontWeight = FontWeight.Bold, // Make text bold
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Subtitle: Get Healthy
        Text(
            text = "Get Healthy",
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
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
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