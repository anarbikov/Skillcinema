package com.skillcinema.ui.film

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.domain.GetActorsByKinopoiskIdUseCase
import com.skillcinema.domain.GetFilmInfoByKinopoiskIdUseCase
import com.skillcinema.domain.GetImagesByKinopoiskIdUseCase
import com.skillcinema.domain.GetSeasonsByKinopoiskIdUseCase
import com.skillcinema.domain.GetSimilarByKinopoiskIdUseCase
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
class FilmViewModel @Inject constructor(
    private val getFilmInfoByKinopoiskIdUseCase: GetFilmInfoByKinopoiskIdUseCase,
    private val getActorsByKinopoiskIdUseCase: GetActorsByKinopoiskIdUseCase,
    private val getImagesByKinopoiskIdUseCase: GetImagesByKinopoiskIdUseCase,
    private val getSimilarByKinopoiskIdUseCase: GetSimilarByKinopoiskIdUseCase,
    private val getSeasonsByKinopoiskIdUseCase: GetSeasonsByKinopoiskIdUseCase
) : ViewModel() {
    private val _movieInfo = MutableStateFlow<List<Any>>(emptyList())
    val movieInfo : StateFlow<List<Any>> = _movieInfo.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _movieInfo.value
    )
    private val allInfo = mutableMapOf<Int, Any>().toSortedMap()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    fun loadAll(kinopoiskId:Int) {
        viewModelScope.launch (Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                allInfo[1] = getFilmInfoByKinopoiskIdUseCase.execute(kinopoiskId)
                allInfo[2] = getSeasonsByKinopoiskIdUseCase.execute(kinopoiskId)
                val actors = getActorsByKinopoiskIdUseCase.execute(kinopoiskId)
                allInfo[3] = actors.filter { it.professionKey == "ACTOR" }
                allInfo[4] = actors.filter { it.professionKey != "ACTOR" }
                val images = when{
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"STILL",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"STILL",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"SHOOTING",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"SHOOTING",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"POSTER",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"POSTER",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"FAN_ART",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"FAN_ART",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"PROMO",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"PROMO",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"CONCEPT",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"CONCEPT",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"WALLPAPER",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"WALLPAPER",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"COVER",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"COVER",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"SCREENSHOT",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"SCREENSHOT",1)
                    else -> {getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"STILL",1)}
                }
                allInfo[5] = images
                allInfo[6] = getSimilarByKinopoiskIdUseCase.execute(kinopoiskId)
                allInfo[0] = true

            }.fold(
                onSuccess = {
                    Log.d("mytag", "OnSuccess Film")
                    _movieInfo.value = allInfo.values.toList()
                },
                onFailure = {
                    allInfo[0] = false
                    _movieInfo.value = allInfo.values.toList()
                    Log.d("mytag", "onFailureFilmVM: ${it.message}")
                }
            )
            _isLoading.value = false
        }
    }
}