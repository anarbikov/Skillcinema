package com.skillcinema.ui.film

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.domain.GetFilmInfoByKinopoiskIdUseCase
import com.skillcinema.entity.FilmInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(
    private val getFilmInfoByKinopoiskIdUseCase: GetFilmInfoByKinopoiskIdUseCase,
) : ViewModel() {
    private val _movieInfo = MutableStateFlow<List<FilmInfo>>(emptyList())
    val movieInfo : StateFlow<List<FilmInfo>> = _movieInfo.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _movieInfo.value
    )
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val allFilms = mutableMapOf<Int, Any>().toSortedMap()
}