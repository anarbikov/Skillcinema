package skillcinema.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import skillcinema.data.FilmsDto
import skillcinema.domain.GetPremiereUseCase
import skillcinema.domain.GetSharedPrefsUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPremiereUseCase: GetPremiereUseCase,
    private val getSharedPrefsUseCase: GetSharedPrefsUseCase
) : ViewModel() {
    var onboardingShownFlag: Int = 0
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun checkForOnboarding() {
        onboardingShownFlag = getSharedPrefsUseCase.execute()
    }

    init {
        loadPremieres()
    }

    private fun loadPremieres() {
        viewModelScope.launch (Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                getPremiereUseCase.execute()
            }.fold(
                onSuccess = {_movies.value = listOf(it) },
                onFailure = { Log.d("mytag",it.message?:"")}
            )
            _isLoading.value = false
        }
        _isLoading.value = false
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
