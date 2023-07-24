package com.skillcinema.ui.filmography

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.domain.GetActorInfoByKinopoiskIdUseCase
import com.skillcinema.domain.GetFilmInfoByKinopoiskIdUseCase
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
class FilmographyViewModel @Inject constructor(
    private val getActorInfoByKinopoiskIdUseCase: GetActorInfoByKinopoiskIdUseCase,
    private val getFilmInfoByKinopoiskIdUseCase: GetFilmInfoByKinopoiskIdUseCase
) : ViewModel() {
        private val _actorInfo = MutableStateFlow<List<Any>>(emptyList())
        val actorInfo : StateFlow<List<Any>> = _actorInfo.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = _actorInfo.value
        )
        private val allInfo = mutableMapOf<Int, Any>().toSortedMap()
        private val _isLoading = MutableStateFlow(false)
        val isLoading = _isLoading.asStateFlow()
        fun loadAll(staffId:Int) {
            viewModelScope.launch (Dispatchers.IO) {
                kotlin.runCatching {
//                    _isLoading.value = true
//                    val generalInfo = getActorInfoByKinopoiskIdUseCase.execute(staffId)
//                    allInfo[1] = generalInfo
//                    val filmListBasic = generalInfo.films?.sortedBy { it.rating }?.take(10)
//                    val idList = mutableListOf<Int>()
//                    for (film in filmListBasic!!) {film.filmId?.let { idList.add(it)}}
//                    val filmListDetailed = mutableListOf<FilmInfo>()
//                    for (id in idList) filmListDetailed.add(getFilmInfoByKinopoiskIdUseCase.execute(id))
//                    allInfo[2] = filmListDetailed.toList()
//
//
//                    allInfo[0] = true
                }.fold(
                    onSuccess = {
                        Log.d("mytag", "OnSuccess Film")
                        _actorInfo.value = allInfo.values.toList()
                    },
                    onFailure = {
                        allInfo[0] = false
                        _actorInfo.value = allInfo.values.toList()
                        Log.d("mytag", "onFailureFilmVM: ${it.message}")
                    }
                )
                _isLoading.value = false
            }
        }
    }