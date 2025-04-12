import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import com.example.tumbuhnyata.data.model.LoginRequest
import com.example.tumbuhnyata.data.model.LoginResponse

interface AuthApi {
    @POST("auth/login") // Remove the leading slash
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}