package com.skillcinema.domain

import com.skillcinema.data.Repository
import com.skillcinema.entity.FilmSeasonsDto
import javax.inject.Inject

class GetSeasonsByKinopoiskIdUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute(kinopoiskId:Int): FilmSeasonsDto {
        return repository.getSeasonsByKinopoiskId(kinopoiskId)
    }
}