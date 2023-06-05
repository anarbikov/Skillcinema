package skillcinema.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import skillcinema.data.PremierePagingSource
import skillcinema.domain.GetPremiereUseCase
import skillcinema.domain.GetSharedPrefsUseCase
import skillcinema.entity.Film
import skillcinema.entity.Films
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
//    val pagedPremiere : Flow<PagingData<Film>> = Pager(
//        config = PagingConfig(pageSize = 20),
//        pagingSourceFactory = {PremierePagingSource(getPremiereUseCase)}
//    ).flow.cachedIn(viewModelScope)
    val pagedPremiere : Flow<PagingData<Film>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { PremierePagingSource(getPremiereUseCase) }
    ).flow.cachedIn(viewModelScope)

}