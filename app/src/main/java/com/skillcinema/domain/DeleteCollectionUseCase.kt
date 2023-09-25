package com.skillcinema.domain

import com.skillcinema.data.Repository
import javax.inject.Inject

class DeleteCollectionUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(collectionName: String) = repository.deleteCollection(collection = collectionName)
}