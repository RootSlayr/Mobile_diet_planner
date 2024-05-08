package com.example.assignment_1

import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.Random


val ingredientLinesState = mutableStateOf<List<String>>(emptyList())
val recipeState = mutableStateOf<RecipePlan?>(null)

fun InitializeVariables(ingredientLines: List<String>, recipe: RecipePlan) {
    ingredientLinesState.value = ingredientLines
    recipeState.value = recipe
}

@Composable
fun DailyRecipeScreen(navController: NavController) {
    val ingredientLines = ingredientLinesState.value
    val recipeName = recipeState.value?.label

    if (ingredientLines.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Ingredients of this meal:\n$recipeName",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(ingredientLines) { ingredient ->
                    Text(
                        text = ingredient,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(), // Ensure text takes up full width
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Start, // Align text to the start of the column
                        overflow = TextOverflow.Clip,
                    )
                }
            }

        }
    }
    BottomNavigationComponent(navController = navController)
}

@Composable
fun NutritionChartScreen(navController: NavController) {
    // Prepare data for pie chart
    val nutrients = recipeState.value?.totalNutrients?.map { it.value.label to it.value.quantity } ?: emptyList()

    // Create a pie chart with the prepared data
    PieChart(nutrients)

    // Bottom navigation component
    BottomNavigationComponent(navController = navController)
}

fun generateUniqueColors(count: Int): List<Int> {
    val colors = mutableListOf<Int>()
    val random = Random()

    while (colors.size < count) {
        val color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        if (!colors.contains(color)) {
            colors.add(color)
        }
    }

    return colors
}
@Composable
fun PieChart(nutrients: List<Pair<String, Double>>) {
    val excludedNutrients = setOf(
        "Energy",
        "Water",
        "Folate equivalent (total)",
        "Carbohydrates (net)",
        "Polyunsaturated",
        "Monounsaturated",
        "Trans",
        "Saturated"
    )
    // Convert data into entries required for the chart
    val filteredNutrients = nutrients.filter { (label, value) ->
        !excludedNutrients.contains(label) && value > 0.0 && value >= 0.1 // Filter out nutrients with value less than 0.1
    }

    val entries = filteredNutrients.map { (label, value) ->
        PieEntry(value.toFloat(), label)
    }
    var selectedEntry: PieEntry? by remember { mutableStateOf(null) }

    val colors = generateUniqueColors(filteredNutrients.size)

    // Create a PieDataSet using the entries
    val dataSet = PieDataSet(entries, "Nutrients").apply {
        setDrawValues(selectedEntry == null) // Hide values for unselected entries
//        valueTextSize = if (selectedEntry == null) 12f else 16f // Increase text size for highlighted entry
        setColors(colors)
    }
//    dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
//    dataSet.valueTextSize = 12f
    dataSet.valueTextColor = Color.BLACK

    // Set value position inside slice
    dataSet.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
    dataSet.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    // Set value formatter to add "%" sign
    dataSet.valueFormatter = PercentValueFormatter()


    // Create a PieData using the dataSet
    val data = PieData(dataSet)
    Column(modifier = Modifier.fillMaxSize()) {
        // Title at the top of the screen
        Text(
            text = "Nutrition Graph",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PieChart(context).apply {
                    setUsePercentValues(true)
                    description.isEnabled = false
                    centerText = "Nutrient Distribution"
                    setDrawCenterText(true)
//                legend.isEnabled = true
                    legend.apply {
                        isEnabled = true
                        verticalAlignment = Legend.LegendVerticalAlignment.TOP
                        horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                        orientation = Legend.LegendOrientation.HORIZONTAL
                        setDrawInside(false)
                        setDrawEntryLabels(false)
                        isWordWrapEnabled = true
                        textSize = 12f
                    }

                    setData(data)
                    animateY(1400, Easing.EaseInOutQuad)

                    // Highlight selected entry when clicked
                    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(entry: Entry?, highlight: Highlight?) {
                            selectedEntry = entry as? PieEntry
                        }

                        override fun onNothingSelected() {
                            selectedEntry = null
                        }
                    })

                    // Highlight selected entry
                    selectedEntry?.let { entry ->
                        val index = dataSet.getEntryIndex(entry)
                        highlightValue(index.toFloat(), 0)
                    }
                }
            }
        )
    }
}

class PercentValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()}%"
    }
}
