package com.skillcinema.domain

import com.skillcinema.data.Repository
import com.skillcinema.room.Collection
import javax.inject.Inject

class InsertCollectionUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(collection: Collection) {
        return repository.insertCollection(collection)
    }
}