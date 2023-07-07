package com.skillcinema.domain

import com.skillcinema.data.FilmsDto
import com.skillcinema.data.Repository
import javax.inject.Inject

class GetRandomGenreFilmsUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute(genre:Int): FilmsDto {
        return repository.getRandomGenreFilms(genre)
    }
}