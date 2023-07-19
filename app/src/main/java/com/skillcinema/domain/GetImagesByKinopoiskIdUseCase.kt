package com.skillcinema.domain

import com.skillcinema.data.Repository
import com.skillcinema.entity.FilmGalleryDto
import com.skillcinema.entity.FilmInfo
import javax.inject.Inject

class GetImagesByKinopoiskIdUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute(kinopoiskId:Int): FilmGalleryDto {
        return repository.getImagesByKinopoiskId(kinopoiskId)
    }
}