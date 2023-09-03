package com.skillcinema.domain

import com.skillcinema.data.Repository
import com.skillcinema.room.Collection
import javax.inject.Inject

class GetAllCollectionsUseCase @Inject constructor(
    private val repository: Repository
) {
     fun execute(): List<Collection> {
        return repository.getAllCollections()
    }
}