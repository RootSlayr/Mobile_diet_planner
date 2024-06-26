package com.example.assignment_1

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
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
import java.util.Arrays
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.regex.Pattern
import java.util.stream.Collectors
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay


//NavController Variables
const val LOGIN_SCREEN = "login"
const val SIGNUP_SCREEN = "signup"
const val HOME_SCREEN = "home"
const val SETTINGS_SCREEN = "settings"
const val MEAL_RECIPE_SCREEN = "recipe"
const val USER_PROFILE_SCREEN = "userProfile"
const val DIET_PLANNER = "planner"
const val FORGOT_PASSWORD_SCREEN = "FORGOT_PASSWORD"
const val UNDER_DEV = "under_development"
const val NOTIF_SCR = "notification_screen"
const val NUTRITION_CHART = "nutrition_chart"
const val DAILY_RECIPE = "daily_recipe"


//Database Constants
const val DATABASE = "MealMate"
const val DB_ENV = "DEV"
const val DB_USER = "User"


class MainActivity : ComponentActivity() {
    private val viewModel: PersonViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            AppContent(viewModel)
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
fun StartUpAnimation(){
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottie.host/20ec16d7-127f-4c36-a327-756ad24a9551/tjYABykS3s.json"))
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ){
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            alignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CreateAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottie.host/20ec16d7-127f-4c36-a327-756ad24a9551/tjYABykS3s.json"))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                alignment = Alignment.Center,
                modifier = Modifier.padding(10.dp)
            )
        }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppContent(viewModel: PersonViewModel) {
    val navController = rememberNavController()
    val isLoggedIn = remember { mutableStateOf(false) }
    var animationCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        delay(5_000)
        animationCompleted = true
    }

    if(animationCompleted) {
        Firebase.auth.addAuthStateListener { auth ->
            isLoggedIn.value = auth.currentUser != null
        }
        println("Current logged in user is ${Firebase.auth.currentUser.toString()}")
        NavHost(
            navController,
            startDestination = if (isLoggedIn.value) HOME_SCREEN else LOGIN_SCREEN
        ) {
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
            composable(SETTINGS_SCREEN) {
                SettingsScreen(navController = navController, viewModel = SettingsViewModel())
            }
            composable(MEAL_RECIPE_SCREEN) {
                DietSelectionPage(navController = navController)
            }
            composable(DIET_PLANNER) {
//            DisplayDatePicker(navController = navController)
                RecipeScreenPlan(navController)
            }
            composable(USER_PROFILE_SCREEN) {
                MainScreen(navController = navController, viewModel)
            }
            composable(UNDER_DEV) {
                UnderDevelopmentScreen(navController = navController)
            }
            composable(DAILY_RECIPE) {
                DailyRecipeScreen(navController)
            }
            composable(NUTRITION_CHART) {
                NutritionChartScreen(navController)
            }
        }
    }else StartUpAnimation()
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
            label = { Text("Email") }
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
                if(email=="" && password==""){
                    errorMessage = "Email & Password fields both cannot be empty"
                    showError = true

                }else if(email==""){
                    errorMessage = "Email field cannot be empty"
                    showError = true
                }else if(password==""){
                    errorMessage = "Password field cannot be empty"
                    showError = true
                }
                else{
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

        GoogleSignInButton(navController = navController)

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

//@Composable
//fun GoogleSignInButton(navController: NavController) {
//    val googleLogo: Painter =
//        painterResource(id = R.drawable.google_logo) // Replace R.drawable.google_logo with your Google logo resource
//    val context = LocalContext.current
//    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
//    val launcher = rememberFirebaseAuthLauncher(
//        onAuthComplete = { result ->
//            user = result.user
//            UserManagement.instance.findUserByEmail(user!!.email!!)
//                .addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            for (users in dataSnapshot.children) {
//                                val data =
//                                    users.getValue(UserData::class.java)
//                                savePref(DB_USER, data!!, context)
//                                navController.navigate(HOME_SCREEN)
//                                break
//                            }
//                        } else {
//                            user!!.delete()
//                            Firebase.auth.signOut()
//                            navController.navigate(SIGNUP_SCREEN)
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.e("DataBase Error", error.message)
//                        Firebase.auth.signOut()
//                        navController.navigate(SIGNUP_SCREEN)
//                    }
//                })
//
//        },
//        onAuthError = { user = null }
//    )
////    val token = stringResource(id = R.string.your_web_client_id)
//    val token = stringResource(id = R.string.your_web_client_id)
//    Button(
//        onClick = {
//
//            val signInOptions =
//                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(token)
//                    .requestEmail()
//                    .build()
//            val googleSignInClient = GoogleSignIn.getClient(context, signInOptions)
//            launcher.launch(googleSignInClient.signInIntent)
//
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 8.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Image(
//                painter = googleLogo,
//                contentDescription = "Google Logo",
//                modifier = Modifier.size(24.dp) // Adjust size as needed
//            )
//            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between the logo and text
//            Text("Sign In with Google")
//        }
//    }
//}

@Composable
fun GoogleSignInButton(navController: NavController) {
    val googleLogo: Painter =
        painterResource(id = R.drawable.google_logo)
    val context = LocalContext.current

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            val user = result.user
            if (user != null) {
                navController.navigate(HOME_SCREEN)
            } else {
                // Handle null user
                Log.e("Auth Error", "User is null")
                navController.navigate(SIGNUP_SCREEN)
            }
        },
        onAuthError = {
            Log.e("Auth Error", "Authentication error: ${it.message}")
            navController.navigate(SIGNUP_SCREEN)
        }
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
fun FeatureRow(icon: ImageVector, text: String, modifier: Modifier) {
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

