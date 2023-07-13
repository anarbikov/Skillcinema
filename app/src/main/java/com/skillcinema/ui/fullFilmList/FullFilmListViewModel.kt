package com.skillcinema.ui.fullFilmList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.skillcinema.R
import com.skillcinema.data.FilmDto
import com.skillcinema.data.FilmPagingSource
import com.skillcinema.domain.GetPopularUseCase
import com.skillcinema.domain.GetPremiereUseCase
import com.skillcinema.domain.GetRandomGenreFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FullFilmListViewModel @Inject constructor(
    private val getPremiereUseCase: GetPremiereUseCase,
    private val getPopularUseCase: GetPopularUseCase,
    private val getRandomGenreFilmsUseCase: GetRandomGenreFilmsUseCase,
    private val application: Application
): AndroidViewModel(application) {
    var filterId = 0
    var category = ""
    val pagedPremiere : Flow<PagingData<FilmDto>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { FilmPagingSource(getPremiereUseCase,application.getString(R.string.Premieres),0) }
    ).flow.cachedIn(viewModelScope)

    val pagedPopular : Flow<PagingData<FilmDto>> = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { FilmPagingSource(getPopularUseCase,application.getString(R.string.Popular),0) }
    ).flow.cachedIn(viewModelScope)

    val pagedRandom : Flow<PagingData<FilmDto>> = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { FilmPagingSource(getRandomGenreFilmsUseCase,category,filterId) }
    ).flow.cachedIn(viewModelScope)
}