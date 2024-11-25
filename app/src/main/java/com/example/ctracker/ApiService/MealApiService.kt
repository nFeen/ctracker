package com.example.ctracker.ApiService

import retrofit2.Call
import retrofit2.http.*

data class MealResponse(
    val meal_id: Int,
    val food_id: Int,
    val quantity: Int,
    val calories: Float,
    val protein: Float,
    val fats: Float,
    val carbs: Float,
    val date: String,
    val part_of_the_day: Int
)

data class AddMealRequest(
    val user_id: Int,
    val food_id: Int,
    val quantity: Int,
    val date: String,
    val part_of_the_day: Int
)

data class EditMealRequest(
    val meal_id: Int,
    val quantity: Int
)

data class DeleteMealRequest(
    val meal_id: Int
)

interface MealApiService {
    @GET("/meals/meals")
    fun getMeals(
        @Query("user_id") userId: Int,
        @Query("date") date: String
    ): Call<List<MealResponse>>

    @POST("/meals/add_meal")
    fun addMeal(@Body body: AddMealRequest): Call<Map<String, String>>

    @PATCH("/meals/edit_meal")
    fun editMeal(@Body body: EditMealRequest): Call<Map<String, String>>

    @DELETE("/meals/delete_meal")
    fun deleteMeal(@Body body: DeleteMealRequest): Call<Map<String, String>>

    @GET("/meals/get_meal")
    fun getMeal(
        @Query("meal_id") mealId: Int
    ): Call<MealResponse>
}
