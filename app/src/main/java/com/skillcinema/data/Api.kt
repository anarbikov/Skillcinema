package com.skillcinema.data

import android.content.Context
import com.skillcinema.R
import dagger.hilt.android.qualifiers.ApplicationContext
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

//KEYS:
//"f6570363-6b4d-4775-acad-cb324cb8366b"
//"10041426-d719-4995-92a1-2c970a2b95fd"


class Api @Inject constructor(
    @ApplicationContext
    val context: Context
) {
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
        val searchRandomGenreApi:SearchRandomGenreApi = retrofit.create(SearchRandomGenreApi::class.java)
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
    interface SearchRandomGenreApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films")
        suspend fun getRandomGenreList(
            @Query(value = "genres") genres: Int,
        ): FilmsDto
    }

    suspend fun getPremieres(): FilmsDto {

        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance()
            .getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
        val premieres = RetrofitServices.searchPremiereApi.getPremieresList(year, month!!)
        premieres.category =  context.getString(R.string.Premieres)
        return premieres
    }

    suspend fun getPopular(): FilmsDto {
        val popular = RetrofitServices.searchPopularApi.getPopularList(7)
        popular.category = context.getString(R.string.Popular)
        return popular
    }

    suspend fun getComedies(): FilmsDto {
        val comedy = RetrofitServices.searchComedyApi.getComedyList(13)
        comedy.category = context.getString(R.string.Comedies)
        return comedy
    }

    suspend fun getSeries(): FilmsDto {
        val series = RetrofitServices.searchSeriesApi.getSeriesList("TV_SERIES")
        series.category = context.getString(R.string.Series)
        return series
    }

    suspend fun getCartoons(): FilmsDto {
        val cartoon = RetrofitServices.searchCartoonApi.getCartoonList(18)
        cartoon.category = context.getString(R.string.Cartoons)
        return cartoon
    }

    suspend fun getRandomGenreFilms(genres: FilterGenreDto): FilmsDto{
        val genre = RetrofitServices.searchRandomGenreApi.getRandomGenreList(genres.id!!)
        genre.category = genres.genre.toString().replaceFirstChar { it.uppercase() }
        return genre
    }
}