package com.skillcinema.domain

import com.skillcinema.entity.FilmsDto
import com.skillcinema.data.FilterGenreDto
import com.skillcinema.data.Repository
import javax.inject.Inject

class GetPremiereUseCase @Inject constructor(
    private val repository: Repository
):GetFilmInterface {
    override suspend fun execute(genre: FilterGenreDto,page:Int): FilmsDto {
        return repository.getPremieres()
    }
}