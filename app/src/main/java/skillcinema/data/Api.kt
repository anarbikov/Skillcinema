package skillcinema.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
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
        val searchPremiereApi: SearchPremiereApi = retrofit.create(SearchPremiereApi::class.java)
        val searchPopularApi: SearchPopularApi = retrofit.create(SearchPopularApi::class.java)
        val searchComedyApi: SearchComedyApi = retrofit.create(SearchComedyApi::class.java)
        val searchSeriesApi: SearchSeriesApi = retrofit.create(SearchSeriesApi::class.java)
        val searchCartoonApi: SearchCartoonApi = retrofit.create(SearchCartoonApi::class.java)
    }

    interface SearchPremiereApi {
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

    interface SearchPopularApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films")
        suspend fun getPopularList(
            @Query(value = "ratingFrom") ratingFrom: Int,
        ): FilmsDto
    }

    interface SearchComedyApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films")
        suspend fun getComedyList(
            @Query(value = "genres") genres: Int,
        ): FilmsDto
    }

    interface SearchSeriesApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films")
        suspend fun getSeriesList(
            @Query(value = "type") type: String,
        ): FilmsDto
    }

    interface SearchCartoonApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films")
        suspend fun getCartoonList(
            @Query(value = "genres") genres: Int,
        ): FilmsDto
    }

    suspend fun getPremieres(): FilmsDto {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance()
            .getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        return RetrofitServices.searchPremiereApi.getPremieresList(year, month!!)
    }

    suspend fun getPopular(): FilmsDto {
        return RetrofitServices.searchPopularApi.getPopularList(7)
    }

    suspend fun getComedies(): FilmsDto {
        return RetrofitServices.searchComedyApi.getComedyList(13)
    }

    suspend fun getSeries(): FilmsDto {
        return RetrofitServices.searchSeriesApi.getSeriesList("TV_SERIES")
    }

    suspend fun getCartoons(): FilmsDto {
        return RetrofitServices.searchCartoonApi.getCartoonList(18)
    }

}