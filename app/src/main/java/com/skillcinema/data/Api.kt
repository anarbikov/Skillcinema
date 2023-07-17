package com.skillcinema.data

import android.content.Context
import android.util.Log
import com.skillcinema.R
import com.skillcinema.entity.FilmInfo
import com.skillcinema.entity.FilmsDto
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
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
        val searchRandomGenreApi: SearchRandomGenreApi = retrofit.create(SearchRandomGenreApi::class.java)
        val searchTop250Api: SearchTop250Api = retrofit.create(SearchTop250Api::class.java)
        val searchFilmInfoByKinopoiskIdApi: SearchFilmByKinopoiskIdApi = retrofit.create(SearchFilmByKinopoiskIdApi::class.java)
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
            @Query(value = "page") page: Int
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
            @Query(value = "page") page: Int
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
            @Query(value = "page") page: Int
        ): FilmsDto
    }
    interface SearchTop250Api {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films/top")
        suspend fun getTop250(
            @Query(value = "type") type: String,
            @Query(value = "page") page: Int
        ): FilmsDto
    }
    interface SearchFilmByKinopoiskIdApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films/{id}")
        suspend fun getFilmByKinopoiskId(
            @Path(value = "id") id: Int
        ): FilmInfo
    }

    suspend fun getPremieres(): FilmsDto {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance()
            .getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
        val premieres = RetrofitServices.searchPremiereApi.getPremieresList(year, month!!)
        premieres.category =  context.getString(R.string.Premieres)
        premieres.filterCategory = 1111
        return premieres
    }

    suspend fun getPopular(page: Int): FilmsDto {
        val popular = RetrofitServices.searchPopularApi.getPopularList(6,page)
        popular.category = context.getString(R.string.Popular)
        popular.filterCategory = 2222
        return popular
    }

    suspend fun getComedies(): FilmsDto {
        val comedy = RetrofitServices.searchComedyApi.getComedyList(13)
        comedy.category = context.getString(R.string.Comedies)
        comedy.filterCategory = 13
        return comedy
    }

    suspend fun getSeries(page: Int): FilmsDto {
        val series = RetrofitServices.searchSeriesApi.getSeriesList("TV_SERIES",page)
        series.category = context.getString(R.string.Series)
        series.filterCategory = 3333
        return series
    }
    suspend fun getTop250Films (page: Int): FilmsDto {
        val top250 = RetrofitServices.searchTop250Api.getTop250(type = "TOP_250_BEST_FILMS",page)
        top250.category = context.getString(R.string.Top250)
        top250.filterCategory = 4444
        top250.items= top250.films
        top250.items?.forEach { it.kinopoiskId = it.filmId }
        return top250
    }

    suspend fun getCartoons(): FilmsDto {
        val cartoon = RetrofitServices.searchCartoonApi.getCartoonList(18)
        cartoon.category = context.getString(R.string.Cartoons)
        cartoon.filterCategory = 18
        return cartoon
    }

    suspend fun getRandomGenreFilms(genres: FilterGenreDto,page:Int): FilmsDto {
        val genre = RetrofitServices.searchRandomGenreApi.getRandomGenreList(genres.id!!,page=page)
        genre.category = genres.genre.toString().replaceFirstChar { it.uppercase() }
        genre.filterCategory = genres.id
        Log.d("mytag","API genres: $genres")
        return genre
    }
    suspend fun getFilmByKinopoiskId(kinopoiskId: Int): FilmInfo {
        return RetrofitServices.searchFilmInfoByKinopoiskIdApi.getFilmByKinopoiskId(kinopoiskId)
    }
}