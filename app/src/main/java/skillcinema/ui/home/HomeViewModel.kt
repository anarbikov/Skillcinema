package skillcinema.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import skillcinema.data.FilmsDto
import skillcinema.domain.GetCartoonsUseCase
import skillcinema.domain.GetComediesUseCase
import skillcinema.domain.GetPopularUseCase
import skillcinema.domain.GetPremiereUseCase
import skillcinema.domain.GetSeriesUseCase
import skillcinema.domain.GetSharedPrefsUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPremiereUseCase: GetPremiereUseCase,
    private val getSharedPrefsUseCase: GetSharedPrefsUseCase,
    private val getPopularUseCase: GetPopularUseCase,
    private val getSeriesUseCase: GetSeriesUseCase,
    private val getComediesUseCase: GetComediesUseCase,
    private val getCartoonsUseCase: GetCartoonsUseCase,
    application: Application
    ) : AndroidViewModel(application) {
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
    fun checkForOnboarding() {
        onboardingShownFlag = getSharedPrefsUseCase.execute()
    }
    private fun loadAll() {
        viewModelScope.launch (Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                allFilms[1] = getPremiereUseCase.execute()
                allFilms[2] = getPopularUseCase.execute()
                allFilms[3] = getSeriesUseCase.execute()
                allFilms[4] = getComediesUseCase.execute()
                allFilms[5] = getCartoonsUseCase.execute()
            }.fold(
                onSuccess = {
                    allFilms[1]?.category = getApplication<Application?>().getString(R.string.Premieres)
                    allFilms[2]?.category = getApplication<Application?>().getString(R.string.Popular)
                    allFilms[3]?.category = getApplication<Application?>().getString(R.string.Series)
                    allFilms[4]?.category = getApplication<Application?>().getString(R.string.Comedies)
                    allFilms[5]?.category = getApplication<Application?>().getString(R.string.Cartoons)
                    _movies.value = allFilms.values.toList() },
                onFailure = { Log.d("mytag",it.message?:"")}
            )
            _isLoading.value = false
        }
    }
}

//    val pagedPremiere : Flow<PagingData<Film>> = Pager(
//        config = PagingConfig(pageSize = 4),
//        pagingSourceFactory = { PremierePagingSource(getPremiereUseCase) }
//    ).flow.cachedIn(viewModelScope)