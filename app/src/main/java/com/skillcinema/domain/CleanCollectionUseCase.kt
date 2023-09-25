package com.skillcinema.domain

import com.skillcinema.data.Repository
import javax.inject.Inject

class CleanCollectionUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(collectionName: String) = repository.cleanCollection(collectionName = collectionName)
}