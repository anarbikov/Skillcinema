package com.skillcinema.domain

import com.skillcinema.data.Repository
import javax.inject.Inject

class DeleteFilmFromCollectionUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(collection: String,filmId:Int) {
        return repository.deleteFilmFromCollection(filmId =  filmId, collection = collection)
    }
}