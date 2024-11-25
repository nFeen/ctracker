import retrofit2.Call
import retrofit2.http.*

data class LoginResponse(val user_id: Int)
data class RegisterResponse(val user_id: Int)
data class UserProfile(
    val login: String,
    val weight: Int,
    val calorieGoal: Int,
    val height: Int,
    val profile_picture: String // Переименовано с avatarBase64
)
data class UpdateWeightRequest(val user_id: Int, val weight: Int)
data class UpdateHeightRequest(val user_id: Int, val height: Int)
data class UpdatePictureRequest(val user_id: Int, val profile_picture: String)
data class UpdateCalorieGoalRequest(val user_id: Int, val caloriegoal: Int)

interface UserApiService {
    @GET("/user/login")
    fun login(
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<LoginResponse>

    @POST("/user/register")
    fun register(@Body body: Map<String, String>): Call<RegisterResponse>

    @GET("/user/getprofile")
    fun getProfile(@Query("user_id") userId: Int): Call<UserProfile>

    @PATCH("/user/weight")
    fun updateWeight(@Body body: UpdateWeightRequest): Call<RegisterResponse>

    @PATCH("/user/height")
    fun updateHeight(@Body body: UpdateHeightRequest): Call<RegisterResponse>

    @PATCH("/user/profile_picture")
    fun updateProfilePicture(@Body body: UpdatePictureRequest): Call<RegisterResponse>

    @PATCH("/user/caloriegoal")
    fun updateCalorieGoal(@Body body: UpdateCalorieGoalRequest): Call<RegisterResponse>
}
