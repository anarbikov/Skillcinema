package com.skillcinema.ui.search

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skillcinema.domain.paged.GetFilmsByFiltersUseCase
import com.skillcinema.entity.FilmDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getFilmsByFiltersUseCase: GetFilmsByFiltersUseCase
) : ViewModel() {
    var countries: List<Int>? = null
    var genres: List<Int>? = null
    var order: String = "RATING"
    var type: String = "ALL"
    var ratingFrom: Int = 0
    var ratingTo: Int = 10
    var yearFrom: Int = 1000
    var yearTo: Int = 3000
    var keyword: String = ""

    val pagedFilm : Flow<PagingData<FilmDto>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {FilteredFilmsPagingSource(
            getFilmsByFiltersUseCase,
            countries = countries,
            genres =  genres,
            order =  order,
            type = type,
            ratingFrom = ratingFrom,
            ratingTo = ratingTo,
            yearFrom = yearFrom,
            yearTo = yearTo,
            keyword = keyword,
        )}
    ).flow
}