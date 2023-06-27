package skillcinema.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import skillcinema.data.FilmDto
import skillcinema.data.FilmsDto
import skillcinema.data.PremierePagingSource
import skillcinema.domain.GetPremiereUseCase
import skillcinema.domain.GetSharedPrefsUseCase
import skillcinema.entity.Film
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPremiereUseCase: GetPremiereUseCase,
    private val getSharedPrefsUseCase: GetSharedPrefsUseCase
) : ViewModel() {
    var onboardingShownFlag: Int = 0

    fun checkForOnboarding() {
        onboardingShownFlag = getSharedPrefsUseCase.execute()
    }

    init {
        loadPremieres()
    }

    private fun loadPremieres() {
        viewModelScope.launch (Dispatchers.IO) {
            kotlin.runCatching {
                getPremiereUseCase.execute()
            }.fold(
                onSuccess = {_movies.value = listOf(it) },
                onFailure = { Log.d("mytag",it.message?:"")}
            )

        }

    }

//    val pagedPremiere : Flow<PagingData<Film>> = Pager(
//        config = PagingConfig(pageSize = 4),
//        pagingSourceFactory = { PremierePagingSource(getPremiereUseCase) }
//    ).flow.cachedIn(viewModelScope)

    private val _movies = MutableStateFlow<List<FilmsDto>>(emptyList())

    val movies : StateFlow<List<FilmsDto>> = _movies.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _movies.value
    )

}
