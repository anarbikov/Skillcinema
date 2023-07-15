package com.skillcinema.ui.fullFilmList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.skillcinema.entity.FilmDto
import com.skillcinema.domain.GetPopularUseCase
import com.skillcinema.domain.GetPremiereUseCase
import com.skillcinema.domain.GetRandomGenreFilmsUseCase
import com.skillcinema.domain.GetSeriesUseCase
import com.skillcinema.domain.GetTop250UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FullFilmListViewModel @Inject constructor(
    private val getPremiereUseCase: GetPremiereUseCase,
    private val getPopularUseCase: GetPopularUseCase,
    private val getRandomGenreFilmsUseCase: GetRandomGenreFilmsUseCase,
    private val getSeriesUseCase: GetSeriesUseCase,
    private val getTop250UseCase: GetTop250UseCase
): ViewModel() {
    var filterId = 0
    var category = ""

    val pagedPremiere : Flow<PagingData<FilmDto>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { FilmPagingSource(getPremiereUseCase,category,filterId) }
    ).flow.cachedIn(viewModelScope)

    val pagedPopular : Flow<PagingData<FilmDto>> = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { FilmPagingSource(getPopularUseCase,category,filterId) }
    ).flow.cachedIn(viewModelScope)

    val pagedRandom : Flow<PagingData<FilmDto>> = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { FilmPagingSource(getRandomGenreFilmsUseCase,category,filterId) }
    ).flow //cachedIn(viewModelScope) убрал, чтобы не кэшировать старый поток
    val pagedSeries : Flow<PagingData<FilmDto>> = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { FilmPagingSource(getSeriesUseCase,category,filterId) }
    ).flow.cachedIn(viewModelScope)
    val pagedTop250 : Flow<PagingData<FilmDto>> = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { FilmPagingSource(getTop250UseCase,category,filterId) }
    ).flow.cachedIn(viewModelScope)
}