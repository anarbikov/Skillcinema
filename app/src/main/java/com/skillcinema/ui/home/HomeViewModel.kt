package com.skillcinema.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.data.FilmFilters
import com.skillcinema.data.FilmsDto
import com.skillcinema.data.FilterGenreDto
import com.skillcinema.domain.GetPopularUseCase
import com.skillcinema.domain.GetPremiereUseCase
import com.skillcinema.domain.GetRandomGenreFilmsUseCase
import com.skillcinema.domain.GetSharedPrefsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPremiereUseCase: GetPremiereUseCase,
    private val getSharedPrefsUseCase: GetSharedPrefsUseCase,
    private val getPopularUseCase: GetPopularUseCase,
//    private val getSeriesUseCase: GetSeriesUseCase,
//    private val getComediesUseCase: GetComediesUseCase,
//    private val getCartoonsUseCase: GetCartoonsUseCase,
    private val getRandomGenreFilmsUseCase: GetRandomGenreFilmsUseCase,
    ) : ViewModel() {
    var onboardingShownFlag: Int = 0
    private val _movies = MutableStateFlow<List<FilmsDto>>(emptyList())
    val movies : StateFlow<List<FilmsDto>> = _movies.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _movies.value
    )
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val allFilms = mutableMapOf<Int,FilmsDto>().toSortedMap()
    init {
        loadAll()
    }

    fun refresh() {
        loadAll()
    }
    fun checkForOnboarding() {
        viewModelScope.launch {
            onboardingShownFlag = getSharedPrefsUseCase.execute()
        }
    }
     private fun loadAll() {
        viewModelScope.launch (Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                allFilms[1] = getPremiereUseCase.execute(FilterGenreDto("",0),1)
                allFilms[2] = getPopularUseCase.execute(FilterGenreDto("",0),1)
                allFilms[3] = getRandomGenreFilmsUseCase.execute(FilmFilters.getRandomGenre(),1)
                allFilms[4] = getRandomGenreFilmsUseCase.execute(FilmFilters.getRandomGenre(),1)
                allFilms[5] = getRandomGenreFilmsUseCase.execute(FilmFilters.getRandomGenre(),1)


            }.fold(
                onSuccess = {
                    allFilms.forEach { it.value.items = it.value.items.shuffled() }
                    _movies.value = allFilms.values.toList()
                },
                onFailure = { Log.d("mytag", it.message ?: "onFailure") }
            )
            _isLoading.value = false
        }
    }
}