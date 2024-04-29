package com.example.assignment_1


import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.Phonelink
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.assignment_1.entity.UserData
import com.example.assignment_1.enum.Gender
import com.example.assignment_1.service.UserManagement
import com.example.assignment_1.service.rememberFirebaseAuthLauncher
import com.example.assignment_1.ui.theme.Assignment_1Theme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.gson.Gson
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Arrays
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.regex.Pattern
import java.util.stream.Collectors
import kotlin.math.cos
import kotlin.math.sin

//NavController Variables
const val LOGIN_SCREEN = "login"
const val SIGNUP_SCREEN = "signup"
const val HOME_SCREEN = "home"
const val SETTINGS_SCREEN = "settings"
const val MEAL_PLANNER_SCREEN = "planner"
const val USER_PROFILE_SCREEN = "userProfile"
const val FORGOT_PASSWORD_SCREEN = "FORGOT_PASSWORD"

//Database Constants
const val DATABASE = "MealMate"
const val DB_ENV = "DEV"
const val DB_USER = "User"


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            AppContent()
        }
    }
}

// Variable for database
val database = Firebase.database.reference
val tables = database.child(DATABASE)
    .child(DB_ENV)

fun savePref(key: String, value: Any, context: Context) {
    val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putString(key, Gson().toJson(value))
    editor.apply()
}

fun getPref(key: String, type: Class<Any>, context: Context) {
    val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    Gson().fromJson(prefs.getString(key, "{}"), type)
}


@Composable
fun AppContent() {
    val navController = rememberNavController()
    val isLoggedIn = remember { mutableStateOf(false) }

    Firebase.auth.addAuthStateListener { auth ->
        isLoggedIn.value = auth.currentUser != null
    }
    println("Current logged in user is ${Firebase.auth.currentUser.toString()}")
    NavHost(navController, startDestination = if (isLoggedIn.value) HOME_SCREEN else LOGIN_SCREEN) {
        composable(LOGIN_SCREEN) {
            LoginScreen(navController)
        }
        composable(HOME_SCREEN) {
            HomeScreen(navController)
        }
        composable(SIGNUP_SCREEN) {
            SignUpScreen(navController = navController)
        }
        composable(FORGOT_PASSWORD_SCREEN) {
            ForgotPasswordScreen(navController = navController)
        }

    }
}


@Composable
fun MessagePopup(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}



@Preview
@Composable
fun MessagePopupPreview(){
    MessagePopup(
        onDismissRequest = { },
        onConfirmation = { },
        dialogTitle = "Alert dialog example",
        dialogText = "This is an example of an alert dialog with buttons.",
        icon = Icons.Default.Info
    )
}

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("Oops! Something went wrong") }
    if (showError) {
        MessagePopup(
            onDismissRequest = { showError = false },
            onConfirmation = { showError = false },
            dialogTitle = "Error logging in",
            dialogText = errorMessage,
            icon = Icons.Default.Error
        )
    }

    var email by remember{mutableStateOf("")}
    var password by remember{mutableStateOf("")}
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
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text("Username/Email") }
        )

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
        )

        // Sign In Button
        Button(
            onClick = {
                Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            UserManagement.instance.findUserByEmail(email)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (users in dataSnapshot.children) {
                                                val data =
                                                    users.getValue(UserData::class.java)
                                                savePref(DB_USER, data!!, context)
                                                navController.navigate(HOME_SCREEN)
                                                break
                                            }
                                        } else {
                                            task.result.user!!.delete()
                                            Firebase.auth.signOut()
                                            navController.navigate(SIGNUP_SCREEN)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.e("DataBase Error", error.message)
                                        errorMessage = "Error Fetching user Data"
                                        showError = true
                                    }

                                })

                        } else {
                            errorMessage = task.exception?.message!!
                            showError = true
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Sign In")
        }

        // Sign Up Button
        Button(
            onClick = {
                navController.navigate(SIGNUP_SCREEN)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }
        GoogleSignInButton(navController)

//        forgot password
        ClickableText(
            text = AnnotatedString("Forgot Password ?"),
            onClick = {
                navController.navigate(FORGOT_PASSWORD_SCREEN)
            },
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(5.dp)
        )

    }
}
@Composable
fun ForgotPasswordScreen(navController: NavController) {

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("Oops! Something went wrong")}
    if (showError) {
        MessagePopup(
            onDismissRequest = { showError = false },
            onConfirmation = { showError = false},
            dialogTitle = "Error logging in",
            dialogText = errorMessage,
            icon = Icons.Default.Error
        )
    }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("")}
    if (showAlert) {
        MessagePopup(
            onDismissRequest = {
                showAlert = false
                navController.navigate(LOGIN_SCREEN)
            },
            onConfirmation = {
                showAlert = false
                navController.navigate(LOGIN_SCREEN)
            },
            dialogTitle = "Hi User,",
            dialogText = alertMessage,
            icon = Icons.Default.Error
        )
    }
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
        var email by remember{ mutableStateOf("") }
        // Username/Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text("Username/Email") }
        )

        // Sign In Button
        Button(
            onClick = {
                Firebase.auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            alertMessage = "An email with password reset link has been sent to your mail id. \n Thank You "
                            showAlert = true
                        }
                        else {
                            errorMessage = task.exception?.message!!
                            showError = true
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Reset Password")
        }

    }
}

@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current
    val passRegex = "^" +
            "(?=.*[0-9])" +         // should Contain number
            "(?=.*[a-z])" +           // should contain lowercase alphabet
            "(?=.*[A-Z])" +             // should contain uppercase alphabet
            "(?=.*[@#$%^&+=!])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{6,20}" +                // at least 6 characters
            "$"
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by rememberSaveable { mutableLongStateOf(Date().time) }
    var gender by remember { mutableStateOf(Gender.NONE) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("Oops! Something went wrong") }
    if (showError) {
        MessagePopup(
            onDismissRequest = { showError = false },
            onConfirmation = { showError = false },
            dialogTitle = "Error logging in",
            dialogText = errorMessage,
            icon = Icons.Default.Error
        )
    }
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

        // Username TextField
        TextField(
            value = name, // You need to bind this to a state variable
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text("Name") }
        )

        //Email TextField
        TextField(
            value = email, // You need to bind this to a state variable
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text("Email") }
        )
        //Date of Birth Field
        DatePickerField(select = { dob = it }, name = "Date of Birth")

        //Gender Input
        RecurrenceDropdownMenu(
            recurrence = { gender = Gender.valueOf(it) },
            options = Arrays.stream(Gender.entries.toTypedArray()).map(Gender::name).collect(
                Collectors.toList()
            ),
            name = "Select Gender"
        )

        // Password TextField
        TextField(
            value = password, // You need to bind this to a state variable
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
        )

        // Password TextField
        TextField(
            value = confirmPassword, // You need to bind this to a state variable
            onValueChange = { confirmPassword = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
        )

        // Sign In Button
        Button(
            onClick = {
                val pattern = Pattern.compile(passRegex)
                val matcher = pattern.matcher(password)
                if (name.isEmpty() || email.isEmpty()) {
                    errorMessage = "Name and Email cannot be empty."
                }
                if (password.length < 6) {
                    errorMessage = "Password cannot less than 6 characters."
                    showError = true
                } else if (!matcher.matches()) {
                    errorMessage =
                        "Password must contain at least 1 number, 1 upperCase and 1 LowerCase, 1 special character."
                    showError = true
                } else if (password != confirmPassword) {
                    errorMessage = "Password confirmation should matches current user."
                    showError = true
                } else if (Date(dob) == Date()) {
                    errorMessage = "Invalid Date of Birth"
                    showError = true
                }
                if (!showError) {
                    Firebase.auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userData = UserData(
                                    name = name,
                                    email = email,
                                    gender = gender,
                                    dob = Date(dob),
                                    id = UUID.randomUUID().toString()
                                )
                                UserManagement.instance.createUser(userData)
                                savePref(DB_USER, userData, context)
                                navController.navigate(LOGIN_SCREEN)
                            } else {
                                errorMessage = task.exception?.message!!
                                showError = true
                            }

                        }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Sign Up")
        }

        GoogleSignInButton(navController)

    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Assignment_1Theme {
        LoginScreen(navController = rememberNavController()) // Empty lambda for now
    }
}

@Composable
fun GoogleSignInButton(navController: NavController) {
    val googleLogo: Painter =
        painterResource(id = R.drawable.google_logo) // Replace R.drawable.google_logo with your Google logo resource
    val context = LocalContext.current
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
            UserManagement.instance.findUserByEmail(user!!.email!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (users in dataSnapshot.children) {
                                val data =
                                    users.getValue(UserData::class.java)
                                savePref(DB_USER, data!!, context)
                                navController.navigate(HOME_SCREEN)
                                break
                            }
                        } else {
                            user!!.delete()
                            Firebase.auth.signOut()
                            navController.navigate(SIGNUP_SCREEN)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("DataBase Error", error.message)
                        Firebase.auth.signOut()
                        navController.navigate(SIGNUP_SCREEN)
                    }
                })

        },
        onAuthError = { user = null }
    )
    val token = stringResource(id = R.string.your_web_client_id)
    Button(
        onClick = {

            val signInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(token)
                    .requestEmail()
                    .build()
            val googleSignInClient = GoogleSignIn.getClient(context, signInOptions)
            launcher.launch(googleSignInClient.signInIntent)

        },
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

@Composable
fun FeatureRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = text)
    }
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
                            icon = {
                                Icon(
                                    Icons.Default.LocalPizza,
                                    contentDescription = "Diet Planner"
                                )
                            },
                            label = { Text("Diet Planner") }
                        )
                        BottomNavigationItem(
                            selected = navController.currentDestination?.route == "settings",
                            onClick = { navController.navigate("settings") },
                            icon = {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = "Settings"
                                )
                            },
                            label = { Text("Settings") }
                        )
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
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
            Text(text = "Settings", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(viewModel.settingsItems) { setting ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth() // Fill the width of the column
                            .height(72.dp) // Adjust the height as needed
                    ) {
                        SettingsItem(setting = setting)
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
                        icon = {
                            Icon(
                                Icons.Default.LocalPizza,
                                contentDescription = "Diet Planner"
                            )
                        },
                        label = { Text("Diet Planner") }
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

@Composable
fun SettingsItem(setting: String) {
    Text(
        text = setting,
        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth()
    )
}

class SettingsViewModel : ViewModel() {
    val settingsItems = listOf(
        "Notification",
        "Sound",
        "Dark Mode",
        "Language",
        "Theme Color",
        "Font Size",
        "Privacy",
        "Security",
        "Data Usage",
        "Account",
        // Add more settings items as needed
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController(), viewModel = SettingsViewModel())
}

@Composable
fun MyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Shape = ButtonDefaults.elevatedShape,
    shape: Shape = MaterialTheme.shapes.small,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor
        ),
        shape = shape,
        interactionSource = interactionSource
    ) {
        Text(text = text)
    }
}

@Composable
fun NutritionPage(
    navController: NavController,
    foodName: String,
    ingredients: List<String>,
    nutritionValues: Map<String, Float>
) {
    val paint = remember {
        Paint().apply {
            color = Color.Black.toArgb() // Set paint color
            textAlign = Paint.Align.CENTER // Set text alignment
            textSize = 24f
        }
    }
    val nutrientColors = remember {
        val colorPalette = listOf(
            Color.Blue,
            Color.Green,
            Color.Red,
            Color.Yellow,
            Color.Magenta,
            Color.Cyan
        )
        nutritionValues.keys.zip(colorPalette).toMap()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Nutrition Distribution",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        val navController = rememberNavController()
        NutritionPieChart(nutritionValues, ingredients, paint, nutrientColors)
        NutritionLegend(nutritionValues, nutrientColors)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Food Name: $foodName",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ingredients: ${ingredients.joinToString(", ")}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Nutrition Values",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        NutritionTable(nutritionValues)
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
                        icon = {
                            Icon(
                                Icons.Default.LocalPizza,
                                contentDescription = "Diet Planner"
                            )
                        },
                        label = { Text("Diet Planner") }
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


@Composable
fun NutritionPieChart(
    nutritionValues: Map<String, Float>,
    ingredients: List<String>,
    paint: Paint,
    nutrientColors: Map<String, Color>
) {
    val totalValue = nutritionValues.values.sum()
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(width = 1.dp, color = Color.Black)
    ) {
        var startAngle = 0f
        nutritionValues.forEach { (label, value) ->
            val sweepAngle = (value / totalValue) * 360
            val ingredientIndex = ingredients.indexOf(label)
            val ingredientName = if (ingredientIndex != -1) ingredients[ingredientIndex] else ""
            drawArc(
                color = nutrientColors[label] ?: Color.Gray,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(size.width / 4, size.height / 4),
                size = Size(size.width / 2, size.height / 2),
                style = Stroke(width = 40f)
            )
            if (ingredientName.isNotBlank()) {
                val angle = Math.toRadians(startAngle.toDouble() + sweepAngle.toDouble() / 2)
                val textX = center.x + size.width / 4 * cos(angle).toFloat() - 20.dp.toPx()
                val textY = center.y + size.height / 4 * sin(angle).toFloat()
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        ingredientName,
                        textX,
                        textY,
                        paint
                    )
                }
            }
            startAngle += sweepAngle
        }
    }
}


@Composable
fun NutritionTable(nutritionValues: Map<String, Float>) {
    Column {
        nutritionValues.forEach { (nutrient, value) ->
            Row {
                Text(text = nutrient, modifier = Modifier.weight(1f))
                Text(text = ": $value", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
fun PreviewNutritionPage() {
    val nutritionValues = mapOf(
        "Protein" to 20f,
        "Carbohydrates" to 50f,
        "Fat" to 30f
    )
    val foodName = "Sample Food"
    val ingredients = listOf("Ingredient 1", "Ingredient 2", "Ingredient 3")
    val navController = rememberNavController()
    NutritionPage(navController, foodName, ingredients, nutritionValues)
}

@Composable
fun NutritionLegend(
    nutritionValues: Map<String, Float>,
    nutrientColors: Map<String, Color>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        nutritionValues.forEach { (nutrient, _) ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
                    .background(color = nutrientColors[nutrient] ?: Color.Gray)
            )
            Text(text = nutrient, modifier = Modifier.padding(start = 4.dp))
        }
    }
}

@Composable
fun RecipeItem(recipeName: String) {
    Text(
        text = recipeName,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietSelectionPage(onDietSelected: (String) -> Unit, navController: NavController) {
    val veganRecipes = listOf(
        "LENTIL BOLOGNESE",
        "BUTTERNUT SQUASH RISOTTO",
        "BOMBAY BURRITOS"
    )
    val ketoRecipes = listOf(
        "BACON-WRAPPED AVOCADO",
        "KETO CHICKEN ALFREDO",
        "SPINACH AND FETA STUFFED SALMON"
    )
    val vegetarianRecipes = listOf(
        "VEGETARIAN PAD THAI",
        "DOSA WITH SAMBAR",
        "MUSHROOM RISOTTO"
    )
    val highProteinRecipes = listOf(
        "GRILLED CHICKEN BREASTS",
        "TURKEY AND QUINOA STUFFED BELL PEPPERS",
        "SALMON AND ASPARAGUS FOIL PACKS"
    )
    var selectedRecepie = ""
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Diet selection dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "Select Diet for Recepies",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        val mealPlans = listOf(
            "Vegan", "Keto", "Vegetarian", "High Protein"
        )
        var isExpanded by remember { mutableStateOf(false) }
        var selectedMealPlanState by remember { mutableStateOf(mealPlans[0]) }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Choose your Meal Plan",
                style = MaterialTheme.typography.labelLarge
            )

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .focusProperties { canFocus = false }
                        .padding(bottom = 8.dp),
                    readOnly = true,
                    value = selectedMealPlanState,
                    onValueChange = {},
                    label = { Text("Meal Plan") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    mealPlans.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedMealPlanState = selectionOption
                                isExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
            selectedRecepie = selectedMealPlanState
//            Text(selectedRecepie)
            Spacer(modifier = Modifier.height(16.dp))
            when (selectedRecepie) {
                "Vegan" -> {
                    LazyColumn {
                        items(veganRecipes) { recipe ->
                            RecipeItem(recipeName = recipe)
                        }
                    }
                }

                "Keto" -> {
                    LazyColumn {
                        items(ketoRecipes) { recipe ->
                            RecipeItem(recipeName = recipe)
                        }
                    }
                }

                "Vegetarian" -> {
                    LazyColumn {
                        items(vegetarianRecipes) { recipe ->
                            RecipeItem(recipeName = recipe)
                        }
                    }
                }

                "High Protein" -> {
                    LazyColumn {
                        items(highProteinRecipes) { recipe ->
                            RecipeItem(recipeName = recipe)
                        }
                    }
                }

            }
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
                    icon = { Icon(Icons.Default.LocalPizza, contentDescription = "Diet Planner") },
                    label = { Text("Diet Planner") }
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


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewDietSelectionPage() {
    val navController = rememberNavController()
    DietSelectionPage({}, navController)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayDatePicker(navController: NavController) {
    val calendar = Calendar.getInstance()
    calendar.set(2024, 0, 1) // month (0) is January
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by rememberSaveable { mutableLongStateOf(calendar.timeInMillis) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Meal Planner", fontSize = 30.sp, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Set your target, Follow your Diet",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                        selectedDate = datePickerState.selectedDateMillis!!
                    }) {
                        Text(
                            text = "OK",
                            modifier = Modifier.padding(16.dp), // Add the modifier parameter
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(
                            text = "Cancel",
                            modifier = Modifier.padding(16.dp), // Add the modifier parameter
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        Button(
            onClick = { showDatePicker = true }
        ) {
            Text(
                text = "Enter Date of target",
                modifier = Modifier.padding(16.dp), // Add the modifier parameter
                style = MaterialTheme.typography.headlineMedium
            )
        }
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
        Text(
            text = "Date of target: ${formatter.format(Date(selectedDate))}"
        )

        val mealPlans = listOf(
            "Celebration or Seasonal Meal Plans",
            "Standard Meal Plans",
            "Specialized Meal Plans",
            "Cuisine-Based Meal Plans",
            "Meal Plans for Specific Goals",
            "Customizable Meal Plans",
            "Celebration or Seasonal Meal Plans"
        )
        var isExpanded by remember { mutableStateOf(false) }
        var selectedMealPlanState by remember { mutableStateOf(mealPlans[0]) }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Choose your Meal Plan",
                style = MaterialTheme.typography.labelLarge
            )

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .focusProperties { canFocus = false }
                        .padding(bottom = 8.dp),
                    readOnly = true,
                    value = selectedMealPlanState,
                    onValueChange = {},
                    label = { Text("Meal Plan") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    mealPlans.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedMealPlanState = selectionOption
                                isExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
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
                    icon = { Icon(Icons.Default.LocalPizza, contentDescription = "Diet Planner") },
                    label = { Text("Diet Planner") }
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun PreviewDisplayDatePicker() {
    val navController = rememberNavController()
    DisplayDatePicker(navController)
}

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Profile", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(viewModel.profileItem) { setting ->
                    ProfileItem(setting = setting)
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
                    icon = { Icon(Icons.Default.LocalPizza, contentDescription = "Diet Planner") },
                    label = { Text("Diet Planner") }
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

@Composable
fun ProfileItem(setting: String) {
    Text(
        text = setting,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    )
}


class ProfileViewModel : ViewModel() {
    val profileItem = listOf(
        "User Information Display",
        "Edit Profile",
        "Dietary Preferences",
        "Fitness Goals",
        "Meal Plans",
        "Social Features"
    )
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    ProfileScreen(navController, viewModel = ProfileViewModel())
}

// TopBar Component
@Composable
fun DatePickerField(select: (Long) -> Unit, name: String) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    val currentDate = Date().toFormattedString()
    var selectDate by rememberSaveable { mutableStateOf(currentDate) }

    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    var yearBase by remember { mutableIntStateOf(calendar.get(Calendar.YEAR)) }
    var monthBase by remember { mutableIntStateOf(calendar.get(Calendar.MONTH)) }
    var dayBase by remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    calendar.time = Date()

    val datePickerDialog =
        DatePickerDialog(context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            yearBase = year
            monthBase = month
            dayBase = dayOfMonth
            val newDate = Calendar.getInstance()
            newDate.set(year, month, dayOfMonth)
            selectDate = "${month.toMonthName()} $dayOfMonth, $year"
            select(newDate.timeInMillis)
        }, yearBase, monthBase, dayBase)

    TextField(
        value = selectDate,
        onValueChange = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        label = { Text(name) },
        interactionSource = interactionSource
    )
    if (isPressed) datePickerDialog.show()

}

fun Int.toMonthName(): String {
    return DateFormatSymbols().months[this]
}

fun Date.toFormattedString(): String {
    val simpleDateFormat = SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault())
    return simpleDateFormat.format(this)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceDropdownMenu(recurrence: (String) -> Unit, options: List<String>, name: String) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Select Gender") }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        TextField(
            value = selectedOption,
            onValueChange = { },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            label = { Text(name) },
            readOnly = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { selection ->
                DropdownMenuItem(text = { Text(selection) }, onClick = {
                    selectedOption = selection
                    recurrence(selection)
                    expanded = false
                })
            }
        }
    }
}

