package com.skillcinema.domain

import com.skillcinema.data.FilmsDto
import com.skillcinema.data.FilterGenreDto

interface GetFilmInterface {
    suspend fun execute(genre: FilterGenreDto): FilmsDto
}