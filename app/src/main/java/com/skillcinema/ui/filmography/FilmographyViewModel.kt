package com.skillcinema.ui.filmography

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.domain.GetActorInfoByKinopoiskIdUseCase
import com.skillcinema.domain.GetFilmInfoByKinopoiskIdUseCase
import com.skillcinema.entity.FilmInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
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
    private val _films = Channel<List<Any>>()
    val films : Channel<List<Any>> = _films
    private val allFilms = mutableMapOf<Int, Any>().toSortedMap()

        fun loadInitial(staffId:Int) {
            viewModelScope.launch (Dispatchers.IO) {
                kotlin.runCatching {
                    _isLoading.value = true
                    val generalInfo = getActorInfoByKinopoiskIdUseCase.execute(staffId)
                    allInfo[1] = generalInfo
                    allInfo[0] = true
                }.fold(
                    onSuccess = {
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
    fun loadByChip(staffId: Int,profession:String){
        viewModelScope.launch (Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                val generalInfo = getActorInfoByKinopoiskIdUseCase.execute(staffId)
                    val filmListBasic = generalInfo.films?.filter { it.professionKey == profession }?.distinctBy { it.filmId }?.take(3)
                    val idList = mutableListOf<Int>()
                    for (film in filmListBasic!!) {film.filmId?.let { idList.add(it)}}
                    val filmListDetailed = mutableListOf<FilmInfo>()
                    for (id in idList) filmListDetailed.add(getFilmInfoByKinopoiskIdUseCase.execute(id))
                    allFilms[1] = filmListDetailed.toList()
                allFilms[0] = true
            }.fold(
                onSuccess = {

                    _films.send(allFilms.values.toList())
  //                  Log.d("mytag","Sender _films.value.size${_films.}")
                },
                onFailure = {
                    allFilms[0] = false
                    _films.send(allFilms.values.toList())
                    Log.d("mytag", "onFailureFilmVM: ${it.message}")
                }
            )
            _isLoading.value = false
        }
    }
    }