package com.skillcinema.domain

import com.skillcinema.data.FilmsDto
import com.skillcinema.data.FilterGenreDto
import com.skillcinema.data.Repository
import javax.inject.Inject

class GetPopularUseCase @Inject constructor(
    private val repository: Repository
):GetFilmInterface {
    override suspend fun execute(genre:FilterGenreDto): FilmsDto {
        return repository.getPopular()
    }
}