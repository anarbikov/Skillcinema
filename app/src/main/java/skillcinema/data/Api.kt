package skillcinema.data

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import skillcinema.entity.Film
import skillcinema.entity.Films
import java.sql.Wrapper
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
 //           "Content-Type: application/json"
        )
        @GET("api/v2.2/films/premieres")
        suspend fun getFilmList(
            @Query(value = "year") year: Int,
            @Query(value = "month") month: String,
            @Query(value ="page")page:Int
        ): FilmsDto
    }

    suspend fun getFilms(page: Int): Films {
 //       val year = Calendar.YEAR
//        val month = Calendar.getInstance().getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault())
        try {
            Log.d("mytag",RetrofitServices.searchPremiereApi.getFilmList(2023,"JUNE",1).toString())
        }catch (e:Throwable) {Log.d("mytag",e.toString())}

        return RetrofitServices.searchPremiereApi.getFilmList(2023 , "JUNE" , 1)
    }

}