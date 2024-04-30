package com.example.assignment_1

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.math.cos
import kotlin.math.sin

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
        BottomNavigationComponent(navController = navController)
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
