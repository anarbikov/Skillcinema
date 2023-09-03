package com.skillcinema.domain

import com.skillcinema.data.Repository
import com.skillcinema.room.Film
import javax.inject.Inject

class InsertFilmToDbUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(collection: String,film:Film) = repository.insertFilmToDb(film = film,collection = collection)
}