

import retrofit2.Call
import retrofit2.http.*

data class FoodResponse(
    val name: String,
    val foodId: Int,
    val calorie: Float,
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
