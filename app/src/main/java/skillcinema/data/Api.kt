package skillcinema.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import skillcinema.entity.Films
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

private const val BASE_URL = "https://kinopoiskapiunofficial.tech/"
private const val API_KEY = "f6570363-6b4d-4775-acad-cb324cb8366b"

class Api @Inject constructor() {
    object RetrofitServices {
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val searchPremiereApi:SearchPremiereApi = retrofit.create(SearchPremiereApi::class.java)
    }
    interface SearchPremiereApi{
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films/premieres")
        suspend fun getPremieresList(
            @Query(value = "year") year: Int,
            @Query(value = "month") month: String,
        ): FilmsDto
    }

    suspend fun getPremieres(): FilmsDto {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault())
        return RetrofitServices.searchPremiereApi.getPremieresList(year , month!!)
    }

}