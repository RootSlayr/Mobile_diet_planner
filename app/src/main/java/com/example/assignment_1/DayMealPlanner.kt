package com.example.assignment_1

import android.util.Log
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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


data class RecipeResponsePlan(val hits: List<HitPlan>)
data class HitPlan(val recipe: RecipePlan)
//data class Recipe(val label: String)
data class RecipePlan(
    val label: String,
    val ingredientLines: List<String>,
    val calories: Double,
    val totalWeight: Double,
    val totalNutrients: Map<String, Nutrient>
)

data class Nutrient(
    val label: String,
    val quantity: Double,
    val unit: String
)
interface EdamamApiPlan {
    @GET("api/recipes/v2")
    fun getRecipes(
        @Query("type") type: String = "public",
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("q") query: String,
        @Query("health") healthLabel: String,
        @Query("mealType") mealType: String
    ): Call<RecipeResponsePlan>
}


@Composable
fun RecipeItemPlan(recipeName: String) {
    Text(
        text = recipeName,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

class RecipeViewModelPlan : ViewModel() {
    var recipes = mutableStateOf<List<RecipePlan>>(listOf())
    var breakfastRecipe by mutableStateOf<RecipePlan?>(null)
        private set
    var lunchRecipe by mutableStateOf<RecipePlan?>(null)
        private set
    var dinnerRecipe by mutableStateOf<RecipePlan?>(null)
        private set
    private val fetchedMeals = mutableSetOf<String>()


    var recipesFetched = false
    private var mealTimeGlb = ""



    private val api: EdamamApiPlan by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.edamam.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EdamamApiPlan::class.java)
    }

    fun fetchRecipes(diet: String) {
        if (!recipesFetched) {
            fetchBreakfastRecipe(diet)
            fetchLunchRecipe(diet)
            fetchDinnerRecipe(diet)
            recipesFetched = true
        }
    }
    fun fetchBreakfastRecipe(diet: String) {
        api.getRecipes(
            appId = "2b40ce51",
            appKey = "11c938d04157cb74c6b4c900e2551a5e",
            query = "",
            healthLabel = diet.lowercase(),
            mealType = "breakfast"
        ).enqueue(object : Callback<RecipeResponsePlan> {
            override fun onResponse(call: Call<RecipeResponsePlan>, response: Response<RecipeResponsePlan>) {
                if (response.isSuccessful) {
                    val recipeResponse = response.body()
                    if (recipeResponse != null && recipeResponse.hits.isNotEmpty()) {
                        val recipe = recipeResponse.hits.random().recipe
                        breakfastRecipe = recipe
                        Log.d("API_Response", "Breakfast recipe: $recipe")
                        Log.d("API_Response", "Nutrients: ${recipe?.totalNutrients}")
                    } else {
                        Log.e("API_Response", "Empty or null response body")
                    }
                } else {
                    Log.e("API_Response", "Unsuccessful response: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<RecipeResponsePlan>, t: Throwable) {
                Log.e("API_Response", "Failed to fetch breakfast recipe: ${t.message}", t)
            }
        })
    }

//    fun fetchBreakfastRecipe(diet: String) {
//        api.  getRecipes(
//            appId = "2b40ce51",
//            appKey = "11c938d04157cb74c6b4c900e2551a5e",
//            query = "",  // or any default you consider necessary
//            healthLabel = diet.lowercase(),
//            mealType = "breakfast"
//        ).enqueue(object : Callback<RecipeResponsePlan> {
//            override fun onResponse(call: Call<RecipeResponsePlan>, response: Response<RecipeResponsePlan>) {
//                if (response.isSuccessful) {
//                    val recipe = response.body()?.hits?.random()?.recipe
//                    breakfastRecipe = recipe
//                } else {
//                    breakfastRecipe = null
//                }
//
//            }
//
//            override fun onFailure(call: Call<RecipeResponsePlan>, t: Throwable) {
//                // handle failure to execute the call
//            }
//        })
//    }
    fun fetchLunchRecipe(diet: String) {
        api.  getRecipes(
            appId = "2b40ce51",
            appKey = "11c938d04157cb74c6b4c900e2551a5e",
            query = "",  // or any default you consider necessary
            healthLabel = diet.lowercase(),
            mealType = "lunch"
        ).enqueue(object : Callback<RecipeResponsePlan> {
            override fun onResponse(call: Call<RecipeResponsePlan>, response: Response<RecipeResponsePlan>) {
                if (response.isSuccessful) {
                    val recipe = response.body()?.hits?.random()?.recipe
                    lunchRecipe = recipe
                } else {
                    lunchRecipe = null
                }

            }

            override fun onFailure(call: Call<RecipeResponsePlan>, t: Throwable) {
                // handle failure to execute the call
            }
        })
    }
    fun fetchDinnerRecipe(diet: String) {
        api.  getRecipes(
            appId = "2b40ce51",
            appKey = "11c938d04157cb74c6b4c900e2551a5e",
            query = "",  // or any default you consider necessary
            healthLabel = diet.lowercase(),
            mealType = "dinner"
        ).enqueue(object : Callback<RecipeResponsePlan> {
            override fun onResponse(call: Call<RecipeResponsePlan>, response: Response<RecipeResponsePlan>) {
                if (response.isSuccessful) {
                    val recipe = response.body()?.hits?.random()?.recipe
                    dinnerRecipe = recipe
                } else {
                    dinnerRecipe = null
                }

            }

            override fun onFailure(call: Call<RecipeResponsePlan>, t: Throwable) {
                // handle failure to execute the call
            }
        })
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreenPlan(navController: NavController, viewModel: RecipeViewModelPlan= viewModel()) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),

            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var selectedRecipe by remember { mutableStateOf("") }

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
                        text = "What are you planning eating today?",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }

                val mealPlans = listOf(
                    "Vegan", "Paleo", "Vegetarian", "Kosher"
                )
                var isExpanded by remember { mutableStateOf(false) }
                var selectedMealPlanState by remember { mutableStateOf(mealPlans[0]) }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "What is special today?",
                        style = MaterialTheme.typography.headlineSmall
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
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        selectedMealPlanState = selectionOption
                                        isExpanded = false
                                        if (!viewModel.recipesFetched) {
                                            viewModel.fetchRecipes(selectedMealPlanState)
                                        }
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

                            viewModel.breakfastRecipe?.let { MealCard(navController,"Breakfast", it) }
                            viewModel.lunchRecipe?.let { MealCard(navController,"Lunch", it) }
                            viewModel.dinnerRecipe?.let { MealCard(navController,"Dinner", it) }
                            viewModel.recipesFetched = false

                        }

                        "Paleo" -> {

                            viewModel.breakfastRecipe?.let { MealCard(navController,"Breakfast", it) }
                            viewModel.lunchRecipe?.let { MealCard(navController,"Lunch", it) }
                            viewModel.dinnerRecipe?.let { MealCard(navController,"Dinner", it) }
                            viewModel.recipesFetched = false
                        }
//
                        "Vegetarian" -> {

                            viewModel.breakfastRecipe?.let { MealCard(navController,"Breakfast", it) }
                            viewModel.lunchRecipe?.let { MealCard(navController,"Lunch", it) }
                            viewModel.dinnerRecipe?.let { MealCard(navController,"Dinner", it) }
                            viewModel.recipesFetched = false
                        }
//
                        "Kosher" -> {

                            viewModel.breakfastRecipe?.let { MealCard(navController,"Breakfast", it) }
                            viewModel.lunchRecipe?.let { MealCard(navController,"Lunch", it) }
                            viewModel.dinnerRecipe?.let { MealCard(navController,"Dinner", it) }
                            viewModel.recipesFetched = false
                        }

                    }
                }
            }
        }
        BottomNavigationComponent(navController = navController)
    }
}

@Composable
fun MealCard(navController: NavController,mealType: String, recipe: RecipePlan) {
//    InitializeVariables(recipe.ingredientLines.joinToString(), recipe)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = mealType,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            RecipeItemPlan(recipe.label)
            MyButton(text = "View Ingredients",
                onClick = {
                    InitializeVariables(recipe.ingredientLines, recipe)
                    navController.navigate(DAILY_RECIPE)
            })
            MyButton(text = "View Nutrition Graph",
                onClick = {
                    InitializeVariables(recipe.ingredientLines, recipe)
                    navController.navigate(NUTRITION_CHART)
            })
        }
    }
}
