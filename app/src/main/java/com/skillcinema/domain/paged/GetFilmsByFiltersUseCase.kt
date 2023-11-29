package com.skillcinema.domain.paged

import com.skillcinema.data.Repository
import com.skillcinema.entity.FilmsDto
import javax.inject.Inject

class GetFilmsByFiltersUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute(
         countries: List<Int>?,
         genres: List<Int>?,
         order: String,
         type: String,
         ratingFrom: Int,
         ratingTo: Int,
         yearFrom: Int,
         yearTo: Int,
         keyword: String,
         page:Int
     ): FilmsDto {
        return repository.getFilmsByFilters(countries, genres, order, type, ratingFrom, ratingTo, yearFrom, yearTo, keyword, page)
    }
}