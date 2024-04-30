package com.example.assignment_1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
    BottomNavigationComponent(navController = navController)
}
