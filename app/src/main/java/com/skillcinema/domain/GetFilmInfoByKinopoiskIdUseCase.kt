package com.skillcinema.domain

import com.skillcinema.data.Repository
import com.skillcinema.entity.FilmInfo
import javax.inject.Inject

class GetFilmInfoByKinopoiskIdUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute(kinopoiskId:Int): FilmInfo {
        return repository.getFilmByKinopoiskId(kinopoiskId)
    }
}