package com.example.assignment_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.*
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.toArgb
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.saveable.rememberSaveable


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment_1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreenPreview()
                }
            }
        }
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
            Text(text = "MealMate - Plan, Cook, Eat.",
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold,
                fontSize = 32.sp, // Adjust size as needed
                color = MaterialTheme.colorScheme.onSurface))
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
            Text(text = "Settings", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(viewModel.settingsItems) {setting ->
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
fun NutritionPage(navController: NavController, foodName: String, ingredients: List<String>, nutritionValues: Map<String, Float>) {
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
}


@Composable
fun NutritionPieChart(nutritionValues: Map<String, Float>, ingredients: List<String>, paint:Paint, nutrientColors: Map<String, Color>) {
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
    NutritionPage(navController,foodName, ingredients, nutritionValues)
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
    DietSelectionPage({},navController)
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
    var selectedDate by rememberSaveable { mutableStateOf(calendar.timeInMillis) }

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
                            style = MaterialTheme.typography.headlineSmall)
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
            Text(text = "Enter Date of target",
                modifier = Modifier.padding(16.dp), // Add the modifier parameter
                style = MaterialTheme.typography.headlineMedium)
        }
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
        Text(
            text = "Date of target: ${formatter.format(Date(selectedDate))}"
        )

        val mealPlans = listOf("Celebration or Seasonal Meal Plans", "Standard Meal Plans", "Specialized Meal Plans", "Cuisine-Based Meal Plans", "Meal Plans for Specific Goals", "Customizable Meal Plans", "Celebration or Seasonal Meal Plans")
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
                items(viewModel.profileItem) {setting ->
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
fun ProfilePreview(){
    val navController = rememberNavController()
    ProfileScreen(navController, viewModel = ProfileViewModel())
}