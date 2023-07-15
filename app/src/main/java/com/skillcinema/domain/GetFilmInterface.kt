package com.skillcinema.domain

import com.skillcinema.entity.FilmsDto
import com.skillcinema.data.FilterGenreDto

interface GetFilmInterface {
    suspend fun execute(genre: FilterGenreDto,page:Int): FilmsDto
}