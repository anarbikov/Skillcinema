package com.skillcinema.domain.paged

import com.skillcinema.data.FilterGenreDto
import com.skillcinema.data.Repository
import com.skillcinema.entity.FilmsDto
import javax.inject.Inject

class GetPagedPremiereUseCase @Inject constructor(
    private val repository: Repository
): GetPagedFilmInterface {
    override suspend fun execute(genre: FilterGenreDto,page:Int): FilmsDto {
        return repository.getPremieres(page)
    }
}