package com.example.assignment_1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.sp


data class RecipeResponse(val hits: List<Hit>)
data class Hit(val recipe: Recipe)
//data class Recipe(val label: String)
data class Recipe(val label: String, val ingredientLines: List<String>)

interface EdamamApi {
    @GET("api/recipes/v2")
    fun getRecipes(
        @Query("type") type: String = "public",
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("q") query: String,
        @Query("health") healthLabel: String
    ): Call<RecipeResponse>
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

class RecipeViewModel : ViewModel() {
    var recipes = mutableStateOf<List<Recipe>>(listOf())

    private val api: EdamamApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.edamam.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EdamamApi::class.java)
    }

    fun fetchRecipes(diet: String) {
        api.getRecipes(
            appId = "2b40ce51",
            appKey = "11c938d04157cb74c6b4c900e2551a5e",
            query = "",  // or any default you consider necessary
            healthLabel = diet.lowercase()
        ).enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                    recipes.value = (response.body()?.hits?.map { hit ->
                        Recipe(
                            label = hit.recipe.label,
                            ingredientLines = hit.recipe.ingredientLines
                        )
                    } ?: listOf())
                } else {
                    recipes.value = listOf()
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                // handle failure to execute the call
            }
        })
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietSelectionPage(navController: NavController, viewModel: RecipeViewModel= viewModel()) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val recipes by viewModel.recipes
            var selectedRecipe by remember { mutableStateOf("") }
            var expandedRecipe by remember { mutableStateOf<String?>(null) }

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
                    "Vegan", "Keto", "Vegetarian", "Gluten Free"
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

                    selectedRecipe = selectedMealPlanState

                    Spacer(modifier = Modifier.height(16.dp))
                    when (selectedRecipe) {
                        "Vegan" -> {
                            viewModel.fetchRecipes("Vegan")

                            LazyColumn (modifier = Modifier.padding(vertical = 8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ){

                                items(recipes) { recipe ->
                                    val isExpandeddisp = remember { mutableStateOf(false) }
                                    var boxHeight = if (isExpandeddisp.value) 200.dp else 100.dp
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = 1.dp,
                                                color = Color.Gray,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                isExpandeddisp.value = !isExpandeddisp.value
                                            }
                                            .height(boxHeight)
                                    ){
                                        Column {
                                            Text(
                                                text = recipe.label,
                                                modifier = Modifier.padding(8.dp),
                                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
                                            )
                                            if (isExpandeddisp.value) {
                                                recipe.ingredientLines.forEach { ingredient ->
                                                    Text(
                                                        text = "- $ingredient",
                                                        modifier = Modifier.padding(
                                                            start = 16.dp,
                                                            top = 4.dp
                                                        ),
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                            }
                                        }

                                    }
                                }
                            }

                        }

                        "Keto" -> {
                            viewModel.fetchRecipes("Keto-Friendly")
                            LazyColumn (modifier = Modifier.padding(vertical = 8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                items(recipes) { recipe ->
                                    val isExpandeddisp = remember { mutableStateOf(false) }
                                    var boxHeight = if (isExpandeddisp.value) 200.dp else 100.dp
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = 1.dp,
                                                color = Color.Gray,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                isExpandeddisp.value = !isExpandeddisp.value
                                            }
                                            .height(boxHeight)
                                    ){
                                        Column {
                                            Text(
                                                text = recipe.label,
                                                modifier = Modifier.padding(8.dp),
                                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
                                            )
                                            if (isExpandeddisp.value) {
                                                recipe.ingredientLines.forEach { ingredient ->
                                                    Text(
                                                        text = "- $ingredient",
                                                        modifier = Modifier.padding(
                                                            start = 16.dp,
                                                            top = 4.dp
                                                        ),
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                        "Vegetarian" -> {
                            viewModel.fetchRecipes("Vegetarian")
                            LazyColumn (modifier = Modifier.padding(vertical = 8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                items(recipes) { recipe ->
                                    val isExpandeddisp = remember { mutableStateOf(false) }
                                    var boxHeight = if (isExpandeddisp.value) 200.dp else 100.dp
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = 1.dp,
                                                color = Color.Gray,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                isExpandeddisp.value = !isExpandeddisp.value
                                            }
                                            .height(boxHeight)
                                    ){
                                        Column {
                                            Text(
                                                text = recipe.label,
                                                modifier = Modifier.padding(8.dp),
                                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
                                            )
                                            if (isExpandeddisp.value) {
                                                recipe.ingredientLines.forEach { ingredient ->
                                                    Text(
                                                        text = "- $ingredient",
                                                        modifier = Modifier.padding(
                                                            start = 16.dp,
                                                            top = 4.dp
                                                        ),
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                        "Gluten Free" -> {
                            viewModel.fetchRecipes("Gluten-free")
                            LazyColumn (modifier = Modifier.padding(vertical = 8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                items(recipes) { recipe ->
                                    val isExpandeddisp = remember { mutableStateOf(false) }
                                    var boxHeight = if (isExpandeddisp.value) 200.dp else 100.dp
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = 1.dp,
                                                color = Color.Gray,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                isExpandeddisp.value = !isExpandeddisp.value
                                            }
                                            .height(boxHeight)
                                    ){
                                        Column {
                                            Text(
                                                text = recipe.label,
                                                modifier = Modifier.padding(8.dp),

                                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
                                            )
                                            if (isExpandeddisp.value) {
                                                recipe.ingredientLines.forEach { ingredient ->
                                                    Text(
                                                        text = "- $ingredient",
                                                        modifier = Modifier.padding(
                                                            start = 16.dp,
                                                            top = 4.dp
                                                        ),
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
    BottomNavigationComponent(navController = navController)
}