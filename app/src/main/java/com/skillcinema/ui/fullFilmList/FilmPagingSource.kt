package com.skillcinema.ui.fullFilmList

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skillcinema.data.FilterGenreDto
import com.skillcinema.domain.GetFilmInterface
import com.skillcinema.entity.FilmDto
import javax.inject.Inject

class FilmPagingSource @Inject constructor(
    private val useCase: GetFilmInterface,
    private val category: String,
    private val filterId:Int
) : PagingSource<Int, FilmDto>() {
    override fun getRefreshKey(state: PagingState<Int, FilmDto>): Int = FIRST_PAGE
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmDto> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            Log.d("mytag","category pagingsource: $category,filterId: $filterId")
            useCase.execute(FilterGenreDto(category,filterId),page)
        }.fold(
            onSuccess = {
                Log.d("mytag","onsuccess")
                LoadResult.Page(
                    data = it.items!!,
                    prevKey = null,
                    nextKey = if(it.items!!.isEmpty())null else page+1
                )
            }, onFailure = {
                Log.d("mytag","onfailure")
                LoadResult.Error(it)
            }
        )
    }

    private companion object {
        private const val FIRST_PAGE = 1
    }
}