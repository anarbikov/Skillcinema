package com.skillcinema.domain

import com.skillcinema.data.Repository
import com.skillcinema.room.CollectionWIthFilms
import javax.inject.Inject

class GetOneCollectionUseCase @Inject constructor(
    private val repository: Repository
) {
     fun execute(collectionName: String): List<CollectionWIthFilms> {
        return repository.getOneFullCollection(collectionName = collectionName)
    }
}