package com.skillcinema.domain

import com.skillcinema.data.Repository
import com.skillcinema.entity.FilmSimilarsDto
import javax.inject.Inject

class GetSimilarByKinopoiskIdUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute(kinopoiskId:Int): FilmSimilarsDto {
        return repository.getSimilarByKinopoiskId(kinopoiskId)
    }
}