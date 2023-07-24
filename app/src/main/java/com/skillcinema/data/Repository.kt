package com.skillcinema.data

import android.content.Context
import android.content.SharedPreferences
import com.skillcinema.entity.ActorDto
import com.skillcinema.entity.ActorGeneralInfoDto
import com.skillcinema.entity.FilmGalleryDto
import com.skillcinema.entity.FilmInfo
import com.skillcinema.entity.FilmSeasonsDto
import com.skillcinema.entity.FilmSimilarsDto
import com.skillcinema.entity.FilmsDto
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: Api,
    @ApplicationContext
    val context: Context
) {
    private lateinit var prefs: SharedPreferences
    private var onboardingShown = 0
    fun getOnBoardingFlag(): Int {
        return when {
            onboardingShown == 1 -> {
                1
            }

            getDataFromSharedPreference() == 1 -> {
                onboardingShown = 1
                1
            }
            else -> {
                saveOnboardingFlag()
                0
            }
        }
    }

    private fun getDataFromSharedPreference(): Int {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_INT_NAME, 0)

    }
    private fun saveOnboardingFlag() {
        onboardingShown = 1
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = prefs.edit()
        editor.putInt(KEY_INT_NAME,1)
        editor.apply()
    }

    suspend fun getPremieres(): FilmsDto {
        return api.getPremieres()
    }
    suspend fun getPopular(): FilmsDto {
        return api.getPopular(1)
    }
    suspend fun getPopularPaged(page:Int): FilmsDto {
        return api.getPopular(page)
    }
    suspend fun getSeries(): FilmsDto {
        return api.getSeries(1)
    }
    suspend fun getSeriesPaged(page:Int): FilmsDto {
        return api.getSeries(page)
    }
    suspend fun getRandomGenreFilms(genres:FilterGenreDto): FilmsDto {
        return api.getRandomGenreFilms(genres,page=1)
    }
    suspend fun getRandomGenreFilmsPaged(genres:FilterGenreDto, page: Int): FilmsDto {
        return api.getRandomGenreFilms(genres,page)
    }
    suspend fun getFilmByKinopoiskId (kinopoiskId: Int): FilmInfo {
        return api.getFilmByKinopoiskId(kinopoiskId)
    }
    suspend fun getTop250(): FilmsDto {
        return api.getTop250Films(1)
    }
    suspend fun getTop250Paged(page: Int): FilmsDto {
        return api.getTop250Films(page)
    }
    suspend fun getActorsByKinopoiskId (kinopoiskId: Int): List<ActorDto> {
        return api.getActorsByKinopoiskId(kinopoiskId)
    }
    suspend fun getImagesByKinopoiskId (kinopoiskId: Int): FilmGalleryDto {
        return api.getImagesByKinopoiskId(kinopoiskId)
    }
    suspend fun getSimilarByKinopoiskId (kinopoiskId: Int): FilmSimilarsDto {
        return api.getSimilarByKinopoiskId(kinopoiskId)
    }
    suspend fun getSeasonsByKinopoiskId (kinopoiskId: Int): FilmSeasonsDto {
        return api.getSeasonsByKinopoiskId(kinopoiskId)
    }
    suspend fun getActorInfoByKinopoiskId (staffId: Int): ActorGeneralInfoDto {
        return api.getActorInfoByKinopoiskId(staffId)
    }
    companion object{
        private  const val PREFERENCE_NAME = "prefs_name"
        private const val KEY_INT_NAME = "KEY_STRING"
    }
}