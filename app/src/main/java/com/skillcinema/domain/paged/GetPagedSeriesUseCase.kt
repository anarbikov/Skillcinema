package com.skillcinema.domain.paged

import com.skillcinema.data.FilterGenreDto
import com.skillcinema.entity.FilmsDto
import com.skillcinema.data.Repository
import javax.inject.Inject

class GetPagedSeriesUseCase @Inject constructor(
    private val repository: Repository
): GetPagedFilmInterface {
     override suspend fun execute(genre: FilterGenreDto, page:Int): FilmsDto {
        return repository.getSeriesPaged(page)
    }
}