package com.example.ctracker.ApiService

import retrofit2.Call
import retrofit2.http.*

data class FoodResponse(
    val food: String,
    val food_id: Int,
    val calorie: Int,
    val carbs: Float,
    val fats: Float,
    val protein: Float
)

interface FoodApiService {
    @GET("/fooddb/search")
    fun searchFoods(@Query("food") foodName: String): Call<List<FoodResponse>>

    @GET("/fooddb/get_item")
    fun getFoodById(@Query("food_id") foodId: Int): Call<FoodResponse>
}
